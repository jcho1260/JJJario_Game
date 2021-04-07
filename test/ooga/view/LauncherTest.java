package ooga.view;

import java.io.File;
import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import ooga.view.factories.ActionFactory;
import ooga.view.factories.ParentComponentFactory;
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
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
    Document doc = dBuilder.parse(new File("resources/view_resources/launcher/SideBar.XML"));
    doc.getDocumentElement().normalize();

    ActionFactory af = new ActionFactory(stage);
    ParentComponentFactory pcf = new ParentComponentFactory(af);
    Element rootE = (Element) doc.getElementsByTagName("VBox").item(0);
    ids = getIds(rootE, new ArrayList<>());
    VBox vbox = (VBox) pcf.make(rootE);
    Scene scene = new Scene(vbox, 300, 750);
    stage.setScene(scene);
    stage.show();
  }

  private ArrayList<String> getIds(Element e, ArrayList<String> ids) {
    if (e.hasAttribute("id")) {
      ids.add("#"+e.getAttribute("id"));
    }

    NodeList nl = e.getChildNodes();
    for (int i = 0; i < nl.getLength(); i++) {
      if (nl.item(0).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
        ids.addAll(getIds((Element) nl.item(0), ids));
      }
    }

    return ids;
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
