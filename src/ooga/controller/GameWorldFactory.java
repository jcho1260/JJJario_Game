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

public class GameWorldFactory {

  public GameWorld createGameWorld(File file, Map<String, Map<String, List<MethodBundle>>> collisions, Vector frameSize, double frameRate)
      throws ParserConfigurationException, IOException, SAXException, ClassNotFoundException {

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.parse(file);
    NodeList objects = ((Element) doc.getElementsByTagName("GameObjects").item(0).getChildNodes()).getElementsByTagName("GameObject");

    Map<String, GameObjectInfo> gameObjectMap = getObjectMap(file, objects);
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
          player = createPlayer(entity, info, i);
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
    return new GameWorld(player, collisions, gameObjects, actors, frameSize, 3, getGlobalGravity(doc), frameRate);
  }

  private Player createPlayer(Element entity, GameObjectInfo info, int id)
      throws ClassNotFoundException {
    Vector pos = getVectorAttribute(entity, "Location");
    Vector vel = getVectorAttribute(entity, "Velocity");
    int startLife = (int) getNumberAttribute(entity, "StartLife");
    int startHealth = (int) getNumberAttribute(entity, "StartHealth");
    double jumpTime = getNumberAttribute(entity, "JumpTime");
    return new Player(info.tags, pos, id, info.size, startLife, startHealth, jumpTime, vel, info.gravity);
  }

  private MovingDestroyable createMovingDestroyable(Element entity, GameObjectInfo info, int id) {
    Vector pos = getVectorAttribute(entity, "Location");
    Vector vel = getVectorAttribute(entity, "Velocity");
    int startLife = (int) getNumberAttribute(entity, "StartLife");
    int startHealth = (int) getNumberAttribute(entity, "StartHealth");
    Vector finalPos = getVectorAttribute(entity, "FinalLocation");
    return new MovingDestroyable(info.tags, pos, id, info.size, startLife, startHealth, vel, finalPos, info.gravity);
  }

  private Destroyable createDestroyable(Element entity, GameObjectInfo info, int id) {
    Vector pos = getVectorAttribute(entity, "Location");
    int startLife = (int) getNumberAttribute(entity, "StartLife");
    int startHealth = (int) getNumberAttribute(entity, "StartHealth");
    return new Destroyable(info.tags, pos, id, info.size, startLife, startHealth);
  }

  private GameObject createGameObject(Element entity, GameObjectInfo info, int id) {
    Vector pos = getVectorAttribute(entity, "Location");
    return  new GameObject(info.tags, pos, id, info.size);
  }

  private Vector getVectorAttribute(Element entity, String name) {
    Element location = (Element) entity.getElementsByTagName(name).item(0);
    if (location == null) return new Vector(0, 0);
    double x = Double.parseDouble(location.getElementsByTagName("x").item(0).getTextContent());
    double y = Double.parseDouble(location.getElementsByTagName("y").item(0).getTextContent());
    return new Vector(x, y);
  }

  private double getNumberAttribute(Element entity, String name) {
    return Double.parseDouble(entity.getElementsByTagName(name).item(0).getTextContent());
  }

  private Map<String, GameObjectInfo> getObjectMap(File file, NodeList objects) {
    HashMap<String, GameObjectInfo> gameObjects = new HashMap<>();
    for (int i = 0; i < objects.getLength(); i++) {
      String name = ((Element) objects.item(i)).getElementsByTagName("Name").item(0).getTextContent();
      String type = ((Element) objects.item(i)).getElementsByTagName("Type").item(0).getTextContent();
      double gravity = Double.parseDouble(((Element) objects.item(i)).getElementsByTagName("Gravity").item(0).getTextContent());

      Element size = (Element) ((Element) objects.item(i)).getElementsByTagName("Size").item(0);
      double sizeX = Double.parseDouble(size.getElementsByTagName("SizeX").item(0).getTextContent());
      double sizeY = Double.parseDouble(size.getElementsByTagName("SizeY").item(0).getTextContent());

      ArrayList<String> tags = new ArrayList<>();
      Element tagsElement = (Element) ((Element) objects.item(i)).getElementsByTagName("Tags").item(0);
      NodeList tagElement = tagsElement.getElementsByTagName("Tag");
      for (int j = 0; j < tagElement.getLength(); j++) {
        tags.add(tagElement.item(j).getTextContent());
      }

      GameObjectInfo gameObjectInfo = new GameObjectInfo(type, tags, gravity, sizeX, sizeY);
      gameObjects.put(name, gameObjectInfo);
    }
    return gameObjects;
  }

  private double getGlobalGravity(Document doc) {
    return Double.parseDouble(doc.getElementsByTagName("GlobalGravity").item(0).getTextContent());
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