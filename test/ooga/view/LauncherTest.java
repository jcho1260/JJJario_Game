package ooga.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
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
    controller = new Controller(30);
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
    double prevX = playerImg.getLayoutX();
    System.out.println(prevX);
    press(KeyCode.D);
    double currX = playerImg.getLayoutX();
    System.out.println(currX);
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
    tf.setText("test");
    clickOn(tf);
    type(KeyCode.ENTER);
    assertNotNull(lookup("#ProfileMenuVBox1").query());
  }

  @Test
  void FailedProfileLoginTest() {
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
    type(KeyCode.ENTER);
    Node errorPane = lookup(".dialog-pane").query();
    assertNotNull(errorPane);
  }

  @Test
  void ProfileEditTest() {
    ProfileLoginTest();
    TextField upMenu = lookup("#UPMenuInput").query();
    assertNotNull(upMenu);
    clickOn(upMenu);
    type(KeyCode.Q);
    assertEquals("Q", upMenu.getPromptText());
    TextField usernameMenu = lookup("#UsernameMenuInput").query();
    assertNotNull(usernameMenu);
    usernameMenu.setText("test");
    clickOn(usernameMenu);
    type(KeyCode.ENTER);
    assertEquals("test", usernameMenu.getPromptText().toLowerCase());
  }

  @Test
  void ProfileFailedEditTest() {
    ProfileLoginTest();
    TextField usernameMenu = lookup("#UsernameMenuInput").query();
    assertNotNull(usernameMenu);
    clickOn(usernameMenu);
    type(KeyCode.ENTER);
    Node warningPane = lookup(".dialog-pane").query();
    assertNotNull(warningPane);
  }

  @Test
  void ProfileHighScoreTest() {
    ProfileLoginTest();
    assertTrue(((Pane) lookup("#JJJarioHighScores").query()).getChildren().size() > 1);
    assertTrue(((Pane) lookup("#FlappyBirdHighScores").query()).getChildren().size() == 1);
  }

  @Test
  void BuilderStartScreenTest() {
    assertNotNull(lookup("#StageBuilderButton").query());
    Button gameLibraryButton = lookup("#StageBuilderButton").query();
    clickOn(gameLibraryButton);
    assertNotNull(lookup("#StageBuilderInfoVBox").query());
  }

  @Test
  void BuilderFailedStartScreen() {
    BuilderStartScreenTest();
    assertNotNull(lookup("#StartBuilderButton").query());
    Button startBuilderButton = lookup("#StartBuilderButton").query();
    clickOn(startBuilderButton);
    Node errorPane = lookup(".dialog-pane").query();
    assertNotNull(errorPane);
  }

  @Test
  void BuilderCorrectStartScreen() {
    BuilderStartScreenTest();

    ((TextField) lookup("#GameNameInput").query()).setText("JJJario");
    ((TextField) lookup("#LevelNameInput").query()).setText("testlevel");
    ((TextField) lookup("#ViewWidthInput").query()).setText("500");
    ((TextField) lookup("#ViewHeightInput").query()).setText("500");
    ((TextField) lookup("#LevelWidthInput").query()).setText("500");
    ((TextField) lookup("#LevelHeightInput").query()).setText("500");

    assertNotNull(lookup("#StartBuilderButton").query());
    Button startBuilderButton = lookup("#StartBuilderButton").query();
    clickOn(startBuilderButton);
    assertNotNull(lookup("#BuilderVBox").query());
  }

  @Test
  void BuilderAddPlayer() {
    BuilderCorrectStartScreen();

    clickOn("#BuilderVBox", MouseButton.SECONDARY);
//    ContextMenu cm = ((ScrollPane) lookup("#BuilderScrollable").query()).getContextMenu();
//    MenuItem player = cm.getItems().stream().filter(menuItem -> menuItem.getText().equals("Mario")).collect(
//        Collectors.toCollection(ArrayList::new)).get(0);
//    player.getOnAction().handle(new ActionEvent());
    clickOn("#PlayerMenuItem");
    ((TextField) lookup("#PositionInput").query()).setText("50,50");
    ((TextField) lookup("#SizeInput").query()).setText("50,50");
    ((Button) lookup("#MakeObjectButton").query()).fire();
    ImageView playerImage = lookup("#Player").query();
    assertNotNull(playerImage);
    assertEquals(50, playerImage.getLayoutX());
    assertEquals(50, playerImage.getLayoutY());
    assertEquals(50, playerImage.getFitHeight());
    assertEquals(50, playerImage.getFitWidth());
  }
}
