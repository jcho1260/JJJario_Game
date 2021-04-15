package ooga.view.launcher;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import ooga.controller.Controller;
import ooga.model.util.Action;
import ooga.view.factories.ParentComponentFactory;
import ooga.view.factories.ViewFactoryException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ProfileView {
  private final ParentComponentFactory pcf;
  private final Controller controller;
  private Pane currMenu;
  private PropertyChangeListener pcl;

  public ProfileView(Controller controller, ParentComponentFactory pcf, PropertyChangeListener pcl) {
    this.controller = controller;
    this.pcf = pcf;
    this.pcl = pcl;
  }

  public void makeMenu(String name, String imagePath, Map<KeyCode, Action> keyCodeActionMap, Map<String, Map<String, Integer>> highScoresMap) {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    Document doc;
    try {
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      doc = dBuilder.parse(new File("resources/view_resources/launcher/profile/ProfileMenu.XML"));
      doc.getDocumentElement().normalize();
      currMenu = (Pane) pcf.make(doc.getDocumentElement());
      makeTextFieldInput("Username", name);
      editImageChoseButton();
      ((ImageView) currMenu.lookup("#ProfileImage")).setImage(new Image(
          Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath))));
      for (KeyCode kc : keyCodeActionMap.keySet()) {
        makeTextFieldInput(keyCodeActionMap.get(kc).toString(), kc.toString());
      }
      for (String game : highScoresMap.keySet()) {
        for (String level : highScoresMap.get(game).keySet()) {
          System.out.println(highScoresMap.get(game).get(level));
          makeHighScores(game, level, highScoresMap.get(game).get(level));
        }
      }
    } catch (Exception exception) {
      exception.printStackTrace();
      new ViewFactoryException(exception.getMessage()).printStackTrace();
    }
  }

  public Parent getParent() {
    return this.currMenu;
  }

  private void makeHighScores(String game, String level, Integer score) {
    ((Pane) currMenu.lookup("#"+game+"HighScores")).getChildren().add(new Text("Level "+level+": "+score));
  }

  private void makeTextFieldInput(String type, String prompt) {
    TextField tf = new TextField();
    tf.setId(type+"MenuInput");
    tf.setPromptText(prompt);
    if (type.equals("Username")) {
      tf.setOnKeyPressed(makePCLHandler(tf, type));
    } else {
      tf.setOnKeyPressed(makePCLKeyBindHandler(tf, type));
    }
    ((Pane) currMenu.lookup("#ProfileMenuTextFieldVBox")).getChildren().add(tf);
    Text t = new Text();
    t.setText(type+":");
    ((Pane) currMenu.lookup("#ProfileMenuLabelVBox")).getChildren().add(t);
  }

  private void editImageChoseButton() {
    ((Button) currMenu.lookup("#ProfileImageButton")).setOnAction(event -> {
      FileChooser fileChooser = new FileChooser();

      fileChooser.getExtensionFilters().addAll(
          new FileChooser.ExtensionFilter("PNG Files", "*.png")
          ,new FileChooser.ExtensionFilter("JPG Files", "*.jpg")
          ,new FileChooser.ExtensionFilter("GIF Files", "*.gif")
      );
      File selectedFile = fileChooser.showOpenDialog(currMenu.getScene().getWindow());
      String imagePath = selectedFile.toURI().toString().split("/resources/")[1];
      pcl.propertyChange(new PropertyChangeEvent(this, "setPicture", null, imagePath));
      ((ImageView) currMenu.lookup("#ProfileImage")).setImage(new Image(
          Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath))));
    });
  }

  private EventHandler<KeyEvent> makePCLHandler(TextField component, String label) {
    return event -> {
      if (event.getCode() == KeyCode.ENTER) {
        pcl.propertyChange(new PropertyChangeEvent(this, "set"+label, null, component.getText()));
        try {
          controller.setActiveProfile(component.getText());
        } catch (IOException e) {
          e.printStackTrace();
        }
        component.setPromptText(component.getText());
      }
    };
  }

  private EventHandler<KeyEvent> makePCLKeyBindHandler(TextField component, String s) {
    return event -> {
      if (event.getCode().isLetterKey()) {
        pcl.propertyChange(new PropertyChangeEvent(this, "setKeyBind", null, new Pair<>(event.getCode(), s)));
        component.setPromptText(event.getCode().toString());
      }
    };
  }
}
