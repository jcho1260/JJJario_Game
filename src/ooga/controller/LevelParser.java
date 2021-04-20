package ooga.controller;

import ooga.model.GameWorld;
import ooga.model.gameobjects.Destroyable;
import ooga.model.gameobjects.GameObject;
import ooga.model.gameobjects.MovingDestroyable;
import ooga.model.gameobjects.Player;
import ooga.model.util.MethodBundle;
import ooga.model.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelParser {

  private final Document doc;

  public LevelParser(File file) throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    doc = db.parse(file);
  }

  public GameWorld createGameWorld(Map<String, Map<String, List<MethodBundle>>> collisions, Vector frameSize, double frameRate) throws ClassNotFoundException {

    NodeList objects = ((Element) doc.getElementsByTagName("GameObjects").item(0).getChildNodes()).getElementsByTagName("GameObject");

    Map<String, GameObjectInfo> gameObjectMap = getObjectMap(objects);
    List<GameObject> actors = new ArrayList<>();
    List<GameObject> gameObjects =  new ArrayList<>();
    NodeList entities = ((Element) doc.getElementsByTagName("Layout").item(0).getChildNodes()).getElementsByTagName("Entity");
    Player player = null;

    for (int i = 0; i < entities.getLength(); i++) {

      Element entity = (Element) entities.item(i);
      String name = entity.getElementsByTagName("Name").item(0).getTextContent();
      GameObjectInfo info = gameObjectMap.get(name);

      switch (gameObjectMap.get(name).type) {
        case "Player" -> {
          player = createPlayer(entity, info, i, doc);
        }
        case "MovingDestroyable" -> {
          MovingDestroyable m = createMovingDestroyable(entity, info, i);
          actors.add(m);
          gameObjects.add(m);
        }
        case "Destroyable" -> {
          Destroyable d = createDestroyable(entity, info, i);
          actors.add(d);
          gameObjects.add(d);
        }
        case "GameObject" -> gameObjects.add(createGameObject(entity, info, i));
      };
    }

    Vector screenMin = getScreenLim(doc, "ScreenLimitsMin");
    Vector screenMax = getScreenLim(doc, "ScreenLimitsMax");

    return new GameWorld(player, collisions, gameObjects, actors, frameSize, 3, getGlobalGravity(doc), frameRate, screenMin, screenMax);
  }

  private Player createPlayer(Element entity, GameObjectInfo info, int id, Document doc)
      throws ClassNotFoundException {
    Vector pos = getVectorAttribute(entity, "Location");
    Vector vel = getVectorAttribute(entity, "Velocity");
    int startLife = (int) getNumberAttribute(entity, "StartLife");
    int startHealth = (int) getNumberAttribute(entity, "StartHealth");
    double jumpTime = getNumberAttribute(entity, "JumpTime");
    int jumpLimit = (int) getNumberAttribute(entity, "ContinuousJumpLimit");
    boolean vis = getVisibility(entity);
    // 2 is the cooldown
    return new Player(info.tags, pos, id, info.size, startLife, startHealth, jumpTime, vel, info.gravity, getDrivingVelocity(doc), jumpLimit, 2, vis, -1);

  }

  private MovingDestroyable createMovingDestroyable(Element entity, GameObjectInfo info, int id) {
    Vector pos = getVectorAttribute(entity, "Location");
    Vector vel = getVectorAttribute(entity, "Velocity");
    int startLife = (int) getNumberAttribute(entity, "StartLife");
    int startHealth = (int) getNumberAttribute(entity, "StartHealth");
    Vector finalPos = getVectorAttribute(entity, "FinalLocation");
    boolean vis = getVisibility(entity);
    int score = (int) getNumberAttribute(entity, "Score");
    return new MovingDestroyable(info.tags, pos, id, info.size, startLife, startHealth, score, vel, finalPos, info.gravity, vis);
  }

  private Destroyable createDestroyable(Element entity, GameObjectInfo info, int id) {
    Vector pos = getVectorAttribute(entity, "Location");
    int startLife = (int) getNumberAttribute(entity, "StartLife");
    int startHealth = (int) getNumberAttribute(entity, "StartHealth");
    boolean vis = getVisibility(entity);
    int score = (int) getNumberAttribute(entity, "Score");
    return new Destroyable(info.tags, pos, id, info.size, startLife, startHealth, score, vis);

  }

  private GameObject createGameObject(Element entity, GameObjectInfo info, int id) {
    Vector pos = getVectorAttribute(entity, "Location");
    boolean vis = getVisibility(entity);
    return  new GameObject(info.tags, pos, id, info.size, vis);
  }

  private boolean getVisibility(Element entity) {
    NodeList visList = entity.getElementsByTagName("Visible");
    if (visList.getLength() == 0) return true;
    return Boolean.parseBoolean(visList.item(0).getTextContent());
  }

  private Vector getVectorAttribute(Element entity, String name) {
    Element location = (Element) entity.getElementsByTagName(name).item(0);
    if (location == null) return new Vector(0, 0);
    double x = Double.parseDouble(location.getElementsByTagName("x").item(0).getTextContent());
    double y = Double.parseDouble(location.getElementsByTagName("y").item(0).getTextContent());
    return new Vector(x, y);
  }

  private double getNumberAttribute(Element entity, String name) {
    if (entity.getElementsByTagName(name).getLength() == 0) {
      return 0;
    }
    return Double.parseDouble(entity.getElementsByTagName(name).item(0).getTextContent());
  }

  private Map<String, GameObjectInfo> getObjectMap(NodeList objects) {
    HashMap<String, GameObjectInfo> gameObjects = new HashMap<>();
    for (int i = 0; i < objects.getLength(); i++) {
      String name = ((Element) objects.item(i)).getElementsByTagName("Name").item(0).getTextContent();
      String type = ((Element) objects.item(i)).getElementsByTagName("Type").item(0).getTextContent();
      double gravity = Double.parseDouble(((Element) objects.item(i)).getElementsByTagName("Gravity").item(0).getTextContent());

      Vector size = getVectorAttribute((Element) objects.item(i), "Size");

      ArrayList<String> tags = new ArrayList<>();
      Element tagsElement = (Element) ((Element) objects.item(i)).getElementsByTagName("Tags").item(0);
      NodeList tagElement = tagsElement.getElementsByTagName("Tag");
      for (int j = 0; j < tagElement.getLength(); j++) {
        tags.add(tagElement.item(j).getTextContent());
      }

      GameObjectInfo gameObjectInfo = new GameObjectInfo(type, tags, gravity, size.getX(), size.getY());
      gameObjects.put(name, gameObjectInfo);
    }
    return gameObjects;
  }

  private double getGlobalGravity(Document doc) {
    return Double.parseDouble(doc.getElementsByTagName("GlobalGravity").item(0).getTextContent());
  }

  private Vector getDrivingVelocity(Document doc) {
    Element root = (Element) doc.getElementsByTagName("Level").item(0);
    return getVectorAttribute(root, "DrivingVelocity");
  }

  private Vector getScreenLim(Document doc, String name) {
    Element root = (Element) doc.getElementsByTagName("Level").item(0);
    return getVectorAttribute(root, name);
  }

  public String getBackground(File file) {
    try {
      DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse(file);
      return doc.getElementsByTagName("BackgroundImage").item(0).getTextContent();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "";
  }

  private class GameObjectInfo {

    final String type;
    final List<String> tags;
    final double gravity;
    final Vector size;

    public GameObjectInfo(String type, List<String> tags, double gravity, double sizeX, double sizeY) {
      this.type = type;
      this.tags = tags;
      this.gravity = gravity;
      this.size =  new Vector(sizeX, sizeY);
    }
  }
}
