package ooga.view.launcher;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import ooga.controller.Profile;
import ooga.model.util.Action;
import ooga.view.factories.ParentComponentFactory;
import ooga.view.factories.ViewFactoryException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ProfileView {
  private final ParentComponentFactory pcf;
  private Parent currMenu;
  private PropertyChangeListener pcl;

  public ProfileView(ParentComponentFactory pcf, PropertyChangeListener pcl) {
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
      TextField tf = ((TextField) currMenu.lookup("#UsernameMenuInput"));
      tf.setPromptText(name);
      tf.setOnKeyPressed(makePCLHandler(tf, "Name"));
    } catch (Exception exception) {
      new ViewFactoryException(exception.getMessage()).printStackTrace();
    }
  }

  public Parent getParent() {
    return this.currMenu;
  }

  private EventHandler<KeyEvent> makePCLHandler(TextField component, String label) {
    return event -> {
      if (event.getCode() == KeyCode.ENTER) {
        pcl.propertyChange(new PropertyChangeEvent(this, "set"+label, null, component.getText()));
        component.setPromptText(component.getText());
      }
    };
  }
}
