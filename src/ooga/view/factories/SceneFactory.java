package ooga.view.factories;

import java.io.File;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import ooga.controller.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SceneFactory {
  private final Stage stage;
  private final Controller controller;

  public SceneFactory(Stage stage, Controller controller) {
    this.stage = stage;
    this.controller = controller;
  }

  public Scene make(String filePath) throws Exception {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(new File(filePath));
    doc.getDocumentElement().normalize();

    ActionFactory af = new ActionFactory(stage, controller);
    ParentComponentFactory pcf = new ParentComponentFactory(af);
    Element sceneElem = (Element) doc.getElementsByTagName("Scene").item(0);
    Element rootElem = (Element) sceneElem.getElementsByTagName("Root").item(0);
    Pane root = (Pane) pcf.make((Element) rootElem.getElementsByTagName("*").item(0));
    NodeList nl = sceneElem.getElementsByTagName("Parent");

    for (int i = 0; i < nl.getLength(); i++) {
      if (nl.item(i).getNodeType() == Node.ELEMENT_NODE) {
        Element tempElem = (Element) nl.item(i);
        Document tempDoc = dBuilder.parse(new File(tempElem.getTextContent()));
        tempDoc.getDocumentElement().normalize();
        Parent tempParent = (Parent) pcf.make(tempDoc.getDocumentElement());
        root.getChildren().add(tempParent);
      }
    }

    return new Scene(root);
  }
}
