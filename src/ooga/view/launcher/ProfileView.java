package ooga.view.launcher;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Pair;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import ooga.controller.Controller;
import ooga.model.util.Action;
import ooga.view.factories.ParentComponentFactory;
import org.w3c.dom.Document;

/**
 * This class provides the functionality to display user profiles to the user in the user interface.
 * It is dependent on ooga.controller.Controller, ooga.model.util.Action,
 * and ooga.view.factories.ParentComponentFactory.
 *
 * @author Adam Hufstetler
 */
public class ProfileView {

  private final ParentComponentFactory pcf;
  private final Controller controller;
  private final PropertyChangeListener pcl;
  private Pane currMenu;

  /**
   * Constructs a ProfileView that can be used to display profile menus to the user.
   *
   * @param controller Controller
   * @param pcf ParentComponentFactory
   * @param pcl PropertyChangeListener used to communicate
   */
  public ProfileView(Controller controller, ParentComponentFactory pcf,
      PropertyChangeListener pcl) {
    this.controller = controller;
    this.pcf = pcf;
    this.pcl = pcl;
  }

  /**
   * Constructs and displays the user's profile.
   *
   * @param name String
   * @param imagePath String
   * @param keyCodeActionMap Map defining the current user keybindings
   * @param highScoresMap Map defining the current user high scores
   */
  public void makeMenu(String name, String imagePath, Map<KeyCode, Action> keyCodeActionMap,
      Map<String, Map<String, Integer>> highScoresMap) {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    Document doc;
    try {
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      doc = dBuilder.parse(new File("resources/view_resources/launcher/profile/ProfileMenu.XML"));
      doc.getDocumentElement().normalize();
      currMenu = (Pane) pcf.make(doc.getDocumentElement());
      makeTextFieldInput("Username", name);
      editImageChoseButton();
      editProfile(imagePath);
      for (KeyCode kc : keyCodeActionMap.keySet()) {
        makeTextFieldInput(keyCodeActionMap.get(kc).toString(), kc.toString());
      }
      for (String game : highScoresMap.keySet()) {
        for (String level : highScoresMap.get(game).keySet()) {
          makeHighScores(game, level, highScoresMap.get(game).get(level));
        }
      }
    } catch (Exception exception) {
      new ExceptionView().displayError(exception);
    }
  }

  private void editProfile(String imagePath) {
    ((ImageView) currMenu.lookup("#ProfileImage")).setImage(new Image(
        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath))));
  }

  /**
   * Returns the current Pane that everything is displayed on. This method is assumed to only be
   * called after a call to makeMenu().
   *
   * @return the current Pane that everything is displayed on
   */
  public Parent getParent() {
    return this.currMenu;
  }

  private void makeHighScores(String game, String level, Integer score) {
    ((Pane) ((ScrollPane) currMenu.lookup("#HighScoreScrollPane")).getContent()
        .lookup("#" + game + "HighScores")).getChildren()
        .add(new Text(level.replaceAll("([A-Za-z])(\\d)", "$1 $2") + ": " + score));
  }

  private void makeTextFieldInput(String type, String prompt) {
    TextField tf = new TextField();
    tf.setId(type + "MenuInput");
    tf.setPromptText(prompt);
    if (type.equals("Username")) {
      tf.setOnKeyPressed(makePCLHandler(tf, type));
    } else {
      tf.setOnKeyPressed(makePCLKeyBindHandler(tf, type));
    }
    ((Pane) currMenu.lookup("#ProfileMenuTextFieldVBox")).getChildren().add(tf);
    Text t = new Text();
    t.setText(type + ":");
    ((Pane) currMenu.lookup("#ProfileMenuLabelVBox")).getChildren().add(t);
  }

  private void editImageChoseButton() {
    ((Button) currMenu.lookup("#ProfileImageButton")).setOnAction(event -> {
      FileChooser fileChooser = new FileChooser();

      fileChooser.getExtensionFilters().addAll(
          new FileChooser.ExtensionFilter("PNG Files", "*.png")
          , new FileChooser.ExtensionFilter("JPG Files", "*.jpg")
          , new FileChooser.ExtensionFilter("GIF Files", "*.gif")
      );
      fileChooser.setInitialDirectory(new File("resources/view_resources/images/profiles"));

      File selectedFile = fileChooser.showOpenDialog(currMenu.getScene().getWindow());
      if (selectedFile == null) {
        new ExceptionView().displayWarning("Invalid File", "Please choose an image!");
      } else {
        String imagePath = selectedFile.toURI().toString().split("/resources/")[1];
        pcl.propertyChange(new PropertyChangeEvent(this, "setPicture", null, imagePath));
        editProfile(imagePath);
      }
    });
  }

  private EventHandler<KeyEvent> makePCLHandler(TextField component, String label) {
    return event -> {
      if (event.getCode() == KeyCode.ENTER) {
        if (component.getText().length() == 0) {
          new ExceptionView()
              .displayWarning("Invalid Input", "Please provide a non-empty username");
          return;
        }
        pcl.propertyChange(new PropertyChangeEvent(this, "set" + label, null, component.getText()));
        try {
          controller.setActiveProfile(component.getText());
        } catch (IOException e) {
          new ExceptionView().displayError(e);
        }
        component.setPromptText(component.getText());
      }
    };
  }

  private EventHandler<KeyEvent> makePCLKeyBindHandler(TextField component, String s) {
    return event -> {
      if (event.getCode().isLetterKey()) {
        pcl.propertyChange(
            new PropertyChangeEvent(this, "setKeyBind", null, new Pair<>(event.getCode(), s)));
        component.setPromptText(event.getCode().toString());
      }
    };
  }
}
