package ooga.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class LevelParser {
  public void createLevel(File file) throws ParserConfigurationException, IOException, SAXException {

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.parse(file);
    NodeList objects = ((Element) doc.getElementsByTagName("GameObjects").item(0).getChildNodes()).getElementsByTagName("GameObject");

    Map<String, String> gameObjectMap = getObjectMap(file, objects);
    NodeList entities = ((Element) doc.getElementsByTagName("Layout").item(0).getChildNodes()).getElementsByTagName("Entity");
    for (int i = 0; i < entities.getLength(); i++) {

      String name = ((Element) entities.item(i)).getElementsByTagName("Name").item(0).getTextContent();
      String clazz = gameObjectMap.get(name);

      Element location = (Element) ((Element) entities.item(i)).getElementsByTagName("Location").item(0);
      int xPos = Integer.parseInt(location.getElementsByTagName("x").item(0).getTextContent());
      int yPos = Integer.parseInt(location.getElementsByTagName("y").item(0).getTextContent());

      Element velocity = (Element) ((Element) entities.item(i)).getElementsByTagName("Velocity").item(0);
      int xVel = Integer.parseInt(velocity.getElementsByTagName("x").item(0).getTextContent());
      int yVel = Integer.parseInt(velocity.getElementsByTagName("y").item(0).getTextContent());

      
    }
  }

  private Map<String, String> getObjectMap(File file, NodeList objects) {
    HashMap<String, String> gameObjects = new HashMap<>();
    for (int i = 0; i < objects.getLength(); i++) {
      String name = ((Element) objects.item(i)).getElementsByTagName("Name").item(0).getTextContent();
      String type = ((Element) objects.item(i)).getElementsByTagName("Type").item(0).getTextContent();
      gameObjects.put(name, type);
    }
    return gameObjects;
  }
}
