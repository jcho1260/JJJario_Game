package ooga.view.factories;

import java.io.File;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import ooga.controller.Controller;
import org.w3c.dom.Document;

/**
 * This class serves as the main entrance to the ooga.factories framework. It provides the
 * functionality to construct a javafx scene by recursively parsing an XML data file in a very
 * declarative manner. Example data files can be found in resources/view_resources. This class
 * depends on ooga.controller.Controller.
 *
 * @author Adam Hufstetler
 */
public class SceneFactory {

  private final Controller controller;
  private final HandlerFactory hf;

  /**
   * Constructs a new SceneFactory with the given controller
   *
   * @param controller Controller
   */
  public SceneFactory(Controller controller) {
    this.controller = controller;
    this.hf = new HandlerFactory(controller);
  }

  /**
   * Returns a Scene constructed form the given XML data file
   *
   * @param filePath the path to the XML file to be parsed
   * @return Scene constructed form the given XML data file
   * @throws ViewFactoryException when the XML data file is not properly formatted
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
