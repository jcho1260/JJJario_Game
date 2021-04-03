package ooga.view;

import java.io.File;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import ooga.view.factories.ComponentFactory;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Feel free to completely change this code or delete it entirely.
 */
class LauncherTest extends ApplicationTest {
  // how close do real valued numbers need to be to count as the same
  static final double TOLERANCE = 0.0005;
  ArrayList<String> ids;

  /**
   * Start test version of application
   */
  @Override
  public void start(Stage stage) throws Exception {
    ids = new ArrayList<>();
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(new File("resources/view/launcher/SideBar.XML"));
    doc.getDocumentElement().normalize();
    ComponentFactory bFactory = new ComponentFactory();
    NodeList nl = doc.getElementsByTagName("Scene").item(0).getChildNodes();
    VBox vbox = new VBox();
    vbox.setPadding(new Insets(10,10,10,10));
    vbox.setSpacing(10);
    vbox.setBackground(new Background(new BackgroundFill(Color.DARKSLATEBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    vbox.getStylesheets().add("view/launcher/css/SideBarButton.css");
    Group root = new Group();
    for (int i = 0; i < nl.getLength(); i++) {
      if (nl.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
        Element el = (Element) nl.item(i);
        ids.add("#"+el.getAttribute("id"));
        vbox.getChildren().add((Node) bFactory.makeComponent(el));
      }
    }
    root.getChildren().add(vbox);
    Scene scene = new Scene(root, 300, 750);
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Test that the view creates all objects described by the XML document
   */
  @Test
  void testXMLObjectDisplay () {
    for (String id : ids) {
      assertNotNull(lookup(id).query());
    }
  }
}
