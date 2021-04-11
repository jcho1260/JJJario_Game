package ooga.view.factories;

import java.io.File;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import ooga.controller.Controller;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SceneFactory {
  private final Controller controller;

  public SceneFactory(Controller controller) {
    this.controller = controller;
  }

  public Scene make(String filePath) throws Exception {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(new File(filePath));
    doc.getDocumentElement().normalize();

    ActionFactory af = new ActionFactory(controller);
    ParentComponentFactory pcf = new ParentComponentFactory(af);
    Pane root = (Pane) pcf.make(doc.getDocumentElement());

    return new Scene(root);
  }
}
