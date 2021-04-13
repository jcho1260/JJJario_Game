package ooga.view.launcher;

import java.io.File;
import java.util.Map;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import ooga.controller.Controller;
import ooga.controller.Profile;
import ooga.model.util.Action;
import ooga.view.factories.ParentComponentFactory;
import ooga.view.factories.ViewFactoryException;
import org.w3c.dom.Document;

public class ProfileView {
  private final Controller controller;
  private final ParentComponentFactory pcf;
  private Parent currMenu;

  public ProfileView(Controller controller, ParentComponentFactory pcf) {
    this.controller = controller;
    this.pcf = pcf;
  }

  public void makeMenu(String name, String imagePath, Map<KeyCode, Action> keyCodeActionMap, Map<String, Map<Integer, Integer>> highScoresMap) throws ViewFactoryException {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    Document doc;
    try {
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      doc = dBuilder.parse(new File("resources/view_resources/launcher/profile/ProfileMenu.XML"));
    } catch (Exception exception) {
      throw new ViewFactoryException(exception.getMessage());
    }
    doc.getDocumentElement().normalize();

    currMenu = (Parent) pcf.make(doc.getDocumentElement());
    ((TextField) currMenu.lookup("#UsernameMenuInput")).setPromptText(name);
  }

  public Parent getParent() {
    return this.currMenu;
  }
}
