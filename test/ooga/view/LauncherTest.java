package ooga.view;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import ooga.controller.Controller;
import ooga.model.util.Vector;
import ooga.util.DukeApplicationTest;
import ooga.view.launcher.LauncherView;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Feel free to completely change this code or delete it entirely.
 */
class LauncherTest extends DukeApplicationTest {
  // how close do real valued numbers need to be to count as the same
  static final double TOLERANCE = 0.0005;
  private ArrayList<String> ids;

  /**
   * Start test version of application
   */
  @Override
  public void start(Stage stage) {
    ids = new ArrayList<>();
    getFileIds("resources/view_resources/launcher/LauncherRoot.XML", ids);
    new LauncherView(stage).start(new Controller(new Vector(1440, 810),30));
  }

  private ArrayList<String> getFileIds(String filePath, ArrayList<String> ids) {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = null;
    try {
      dBuilder = dbFactory.newDocumentBuilder();
    } catch (ParserConfigurationException e) {
      e.printStackTrace();
    }
    Document doc = null;
    try {
      assert dBuilder != null;
      doc = dBuilder.parse(new File(filePath));
    } catch (SAXException | IOException e) {
      e.printStackTrace();
    }
    assert doc != null;
    doc.getDocumentElement().normalize();

    ids.addAll(getElementIds(doc.getDocumentElement(), ids));
    return ids;
  }

  private ArrayList<String> getElementIds(Element e, ArrayList<String> ids) {
    if (e.hasAttribute("id")) {
      ids.add("#"+e.getAttribute("id"));
    } else if (e.getNodeName().equals("Parent")) {
      ids.addAll(getFileIds(e.getTextContent(), ids));
    }

    NodeList nl = e.getChildNodes();
    for (int i = 0; i < nl.getLength(); i++) {
      if (nl.item(0).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
        ids.addAll(getElementIds((Element) nl.item(0), ids));
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


  /**
   * Test to see if the user can open the JJJario Game
   */
  @Test
  void JJJarioGameButton () {
    assertNotNull(lookup("#Game1Button").query());
    Button game1Button = lookup("#Game1Button").query();
    clickOn(game1Button);
    assertNotNull(lookup("#SuperMarioMenu").query());
  }

  /**
   * Test to see if the user can open the JJJario Level 1
   */
  @Test
  void JJJarioLevel1Button () {
    JJJarioGameButton();
    assertNotNull(lookup("#Level1Button").query());
    Button level1Button = lookup("#Level1Button").query();
    clickOn(level1Button);
    assertNotNull(lookup("#JJJarioLevelView").query());
  }

  /**
   * Test to see if the user can open the JJJario Level 1
   */
  @Test
  void JJJarioMoveGuyOnKey () {
    JJJarioLevel1Button();
    assertNotNull(lookup("#Player").query());
    ImageView playerImg = lookup("#Player").query();
    double prevX = playerImg.getX();
    press(KeyCode.D);
    double currX = playerImg.getX();
    assertNotEquals(prevX, currX);
  }
}
