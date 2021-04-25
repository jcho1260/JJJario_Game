package ooga.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import ooga.controller.Controller;
import ooga.model.util.Vector;
import ooga.util.DukeApplicationTest;
import ooga.view.launcher.ExceptionView;
import ooga.view.launcher.LauncherView;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Feel free to completely change this code or delete it entirely.
 */
class LauncherTest extends DukeApplicationTest {

  // how close do real valued numbers need to be to count as the same
  static final double TOLERANCE = 0.0005;
  private ArrayList<String> ids;
  private Controller controller;

  /**
   * Start test version of application
   */
  @Override
  public void start(Stage stage) {
    ids = new ArrayList<>();
    getFileIds("resources/view_resources/launcher/LauncherRoot.XML", ids);
    controller = new Controller(30, new ExceptionView());
    new LauncherView(stage).start(controller);
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
      ids.add("#" + e.getAttribute("id"));
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
  void testXMLObjectDisplay() {
    for (String id : ids) {
      assertNotNull(lookup(id).query());
    }
  }

  /**
   * Test to see if the user can open the JJJario Game
   */
  @Test
  void GameLibraryButton() {
    assertNotNull(lookup("#GameLibraryButton").query());
    Button gameLibraryButton = lookup("#GameLibraryButton").query();
    clickOn(gameLibraryButton);
    assertNotNull(lookup("#GLScrollPane").query());
  }

  /**
   * Test to see if the user can open the JJJario Game
   */
  @Test
  void JJJarioGameButton() {
    GameLibraryButton();
    assertNotNull(lookup("#Game1Button").query());
    Button game1Button = lookup("#Game1Button").query();
    clickOn(game1Button);
    assertNotNull(lookup("#SuperMarioMenu").query());
  }

  /**
   * Test to see if the user can open the JJJario Level 1
   */
  @Test
  void JJJarioLevel1Button() {
    JJJarioGameButton();
    Button levelLibrary = lookup("#LevelLibraryButton").query();
    clickOn(levelLibrary);
    assertNotNull(lookup("#Level1Button").query());
    Button level1Button = lookup("#Level1Button").query();
    clickOn(level1Button);
    assertNotNull(lookup("#JJJarioLevelView").query());
  }

  /**
   * Test to see if the user can open the JJJario Level 1
   */
  @Test
  void JJJarioMoveGuyOnKey() {
    JJJarioLevel1Button();
    assertNotNull(lookup("#Player").query());
    ImageView playerImg = lookup("#Player").query();
    double prevX = playerImg.getX();
    press(KeyCode.D);
    double currX = playerImg.getX();
    assertNotEquals(prevX, currX);
    controller.endGame();
  }

  @Test
  void ProfileLoginTest() {
    try {
      controller.setActiveProfile("");
    } catch (IOException e) {
      e.printStackTrace();
    }
    Button pfButton = lookup("#ProfileButton").query();
    assertNotNull(pfButton);
    clickOn(pfButton);
    TextField tf = lookup("#UsernameInputBox").query();
    assertNotNull(tf);
    clickOn(tf);
    type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T, KeyCode.ENTER);
    assertNotNull(lookup("#ProfileMenuVBox1").query());
  }

  @Test
  void ProfileEditTest() {
    ProfileLoginTest();
    TextField upMenu = lookup("#UPMenuInput").query();
    assertNotNull(upMenu);
    clickOn(upMenu);
    type(KeyCode.Q);
    assertEquals("Q", upMenu.getPromptText());
  }
}
