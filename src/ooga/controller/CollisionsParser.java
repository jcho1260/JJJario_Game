package ooga.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CollisionsParser {

  public Map<String, Map<String, List<MethodBundle>>> parseCollisions(File file)
      throws ParserConfigurationException, IOException, SAXException {

    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    Document doc = db.parse(file);
    NodeList actors = ((Element) doc.getElementsByTagName("Collisions").item(0).getChildNodes()).getElementsByTagName("Actor");

    Map<String, Map<String, List<MethodBundle>>> collisions = new HashMap<>();
    for (int i = 0; i < actors.getLength(); i++) {
      String name = ((Element) actors.item(i)).getElementsByTagName("Name").item(0).getTextContent();
      collisions.put(name, new HashMap<>());

      NodeList gameObjects = ((Element) actors.item(i)).getElementsByTagName("GameObject");
      for(int j = 0; j < gameObjects.getLength(); j++) {
        String objName = ((Element) gameObjects.item(j)).getElementsByTagName("Name").item(0).getTextContent();
        collisions.get(name).put(objName, getMethods(gameObjects.item(j)));
      }
    }

    System.out.println(collisions);
    return collisions;
  }

  private List<MethodBundle> getMethods(Node gameObject) {
    NodeList methodNodes = ((Element) gameObject).getElementsByTagName("Method");

    List<MethodBundle> methods = new ArrayList<>();
    for (int i = 0; i < methodNodes.getLength(); i++) {
      Element methodNode = (Element) methodNodes.item(i);
      String name = ((Element) methodNode).getElementsByTagName("Name").item(0).getTextContent();

      NodeList arguments = ((Element) ((Element) methodNode).getElementsByTagName("Args").item(0)).getElementsByTagName("Args");
      double[] args = new double[arguments.getLength()];
      for (int j = 0; j < arguments.getLength(); j++) {
        args[i] = Double.parseDouble(arguments.item(i).getTextContent());
      }

      MethodBundle method = new MethodBundle(name, args);
      methods.add(method);
    }
    return methods;
  }
}
