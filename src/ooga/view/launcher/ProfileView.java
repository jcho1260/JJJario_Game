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
  private Parent currMenu;
  private PropertyChangeListener pcl;

  public ProfileView(Controller controller, ParentComponentFactory pcf, PropertyChangeListener pcl) {
    this.controller = controller;
    this.pcf = pcf;
    this.pcl = pcl;
  }

  public void makeMenu(String name, String imagePath, Map<KeyCode, Action> keyCodeActionMap, Map<String, Map<Integer, Integer>> highScoresMap) {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    Document doc;
    try {
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      doc = dBuilder.parse(new File("resources/view_resources/launcher/profile/ProfileMenu.XML"));
      doc.getDocumentElement().normalize();
      currMenu = (Parent) pcf.make(doc.getDocumentElement());
      editUsernameInput(name);
    } catch (Exception exception) {
      new ViewFactoryException(exception.getMessage()).printStackTrace();
    }
  }

  public Parent getParent() {
    return this.currMenu;
  }

  private void editUsernameInput(String name) {
    TextField tf = ((TextField) currMenu.lookup("#UsernameMenuInput"));
    tf.setPromptText(name);
    tf.setOnKeyPressed(makePCLHandler(tf, "Name"));
  }

  private void makeKeyBindingInput(String name) {
    TextField tf = ((TextField) currMenu.lookup("#ForwardMenuInput"));
    tf.setPromptText(name);
    tf.setOnKeyPressed(makePCLHandler(tf, "Name"));
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
}
