package ooga.view.launcher;

import java.io.File;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import ooga.controller.Controller;
import ooga.controller.Profile;
import ooga.view.factories.ParentComponentFactory;
import ooga.view.factories.ViewFactoryException;
import org.w3c.dom.Document;

public class ProfileView {
  private final Controller controller;
  private final ParentComponentFactory pcf;

  public ProfileView(Controller controller, ParentComponentFactory pcf) {
    this.controller = controller;
    this.pcf = pcf;
  }

  public Parent makeMenu(Profile profile) throws ViewFactoryException {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    Document doc;
    try {
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      doc = dBuilder.parse(new File("resources/view_resources/launcher/profile/ProfileMenu.XML"));
    } catch (Exception exception) {
      throw new ViewFactoryException(exception.getMessage());
    }
    doc.getDocumentElement().normalize();

    Parent parent = (Parent) pcf.make(doc.getDocumentElement());
    ((TextField) parent.lookup("#UsernameMenuInput")).setPromptText(profile.name());

    return parent;
  }
}
