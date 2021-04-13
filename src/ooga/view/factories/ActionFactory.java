package ooga.view.factories;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ooga.controller.Controller;
import ooga.controller.KeyListener;
import ooga.controller.Profile;
import ooga.view.game.GameView;
import ooga.view.launcher.ProfileView;
import org.w3c.dom.Element;

public class ActionFactory {

  private final Controller controller;
  private final ParentComponentFactory pcf = new ParentComponentFactory(this);

  public ActionFactory(Controller controller) {
    this.controller = controller;
  }

  public EventHandler<ActionEvent> makeActionEvent(Node component, Element e) {
    String actionType = e.getElementsByTagName("Type").item(0).getTextContent();
    return switch (actionType) {
      case "LaunchGame" -> makeLaunchGameAction(e);
      case "StartLevel" -> makeStartLevelAction(e);
      case "ChangeStack" -> makeChangeStackAction(component, e);
      case "ProfileView" -> makeProfileViewAction(component, e);
      default -> null;
    };
  }

  public EventHandler<KeyEvent> makeKeyEvent(Node component, Element e) {
    return makeProfileLoginAction(component, e);
  }

  private EventHandler<ActionEvent> makeLaunchGameAction(Element e) {
    return event -> {
      Stage newStage = new Stage();
      String filePath = e.getElementsByTagName("Path").item(0).getTextContent();
      String gameName = e.getElementsByTagName("Game").item(0).getTextContent();
      KeyListener kl = controller.getKeyListener();
      GameView gv = new GameView(gameName, newStage, kl, controller);
      controller.startGame(gv);
      gv.start(filePath);
    };
  }

  private EventHandler<ActionEvent> makeStartLevelAction(Element e) {
    return event -> {
      String levelName = e.getElementsByTagName("Level").item(0).getTextContent();
      controller.startLevel(levelName);
    };
  }

  private EventHandler<ActionEvent> makeChangeStackAction(Node component, Element e) {
    return event -> {
      try {
        changeStackPane(component, e, (Node) pcf.make((Element) e.getElementsByTagName("FilePath").item(0)));
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    };
  }

  private EventHandler<ActionEvent> makeProfileViewAction(Node component, Element e) {
    return event -> {
      String pfName = controller.getActiveProfile();
      try {
        if (pfName.equals("")) {
          changeStackPane(component, e, (Node) pcf.make((Element) e.getElementsByTagName("FilePath").item(0)));
        } else {
          Profile profile = controller.getProfile(pfName);
          ProfileView pv = new ProfileView(controller, pcf, profile);
          profile.display(pv);
          changeStackPane(component, e, pv.getParent());
        }
      } catch (ViewFactoryException vfe) {
        vfe.printStackTrace();
      }
    };
  }

  private EventHandler<KeyEvent> makeProfileLoginAction(Node component, Element e) {
    return event -> {
      if (event.getCode() == KeyCode.ENTER) {
        try {
          String pfName = ((TextField) component).getText();
          controller.setActiveProfile(pfName);
          Profile profile = controller.getProfile(pfName);
          ProfileView pv = new ProfileView(controller, pcf, profile);
          profile.display(pv);
          changeStackPane(component, e, pv.getParent());
        } catch (IOException | ViewFactoryException ioException) {
          ioException.printStackTrace();
        }
      }
    };
  }

  private void changeStackPane(Node component, Element e, Node newNode) throws ViewFactoryException {
    StackPane sp = (StackPane) component.getScene()
        .lookup("#" + e.getElementsByTagName("PaneID").item(0).getTextContent());
    sp.getChildren().clear();
    newNode.toFront();
    sp.getChildren().add(newNode);
  }
}
