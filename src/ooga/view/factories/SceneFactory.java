package ooga.view.factories;

import java.io.File;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import ooga.controller.Controller;
import org.w3c.dom.Document;

/**
 *
 */
public class SceneFactory {

  private final Controller controller;
  private final HandlerFactory hf;

  /**
   * @param controller
   */
  public SceneFactory(Controller controller) {
    this.controller = controller;
    this.hf = new HandlerFactory(controller);
  }

  /**
   * @param filePath
   * @return
   * @throws ViewFactoryException
   */
  public Scene make(String filePath) throws ViewFactoryException {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    Document doc;
    try {
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      doc = dBuilder.parse(new File(filePath));
    } catch (Exception exception) {
      throw new ViewFactoryException(exception.getMessage());
    }
    doc.getDocumentElement().normalize();

    ParentComponentFactory pcf = new ParentComponentFactory(hf);
    Pane root = (Pane) pcf.make(doc.getDocumentElement());

    return new Scene(root);
  }
}
