package ooga.view.launcher;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Pair;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import ooga.controller.Controller;
import ooga.model.util.Action;
import ooga.view.factories.ParentComponentFactory;
import ooga.view.factories.ViewFactoryException;
import org.w3c.dom.Document;

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
      for (KeyCode kc : keyCodeActionMap.keySet()) {
        makeTextFieldInput(keyCodeActionMap.get(kc).toString(), kc.toString());
      }
    } catch (Exception exception) {
      new ViewFactoryException(exception.getMessage()).printStackTrace();
    }
  }

  public Parent getParent() {
    return this.currMenu;
  }

  private void makeTextFieldInput(String type, String prompt) {
    TextField tf = new TextField();
    tf.setId("#"+type+"MenuInput");
    tf.setPromptText(prompt);
    if (type.equals("Username")) {
      tf.setOnKeyPressed(makePCLHandler(tf, type));
    } else {
      tf.setOnKeyPressed(makePCLKeyBindHandler(type));
    }
    ((Pane) currMenu.lookup("#ProfileMenuTextFieldVBox")).getChildren().add(tf);
    Text t = new Text();
    t.setText(type+":");
    ((Pane) currMenu.lookup("#ProfileMenuLabelVBox")).getChildren().add(t);
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

  private EventHandler<KeyEvent> makePCLKeyBindHandler(String s) {
    return event -> pcl.propertyChange(new PropertyChangeEvent(this, "setKeyBind", null, new Pair<>(event.getCode(), s)));
  }
}
