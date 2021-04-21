package ooga.view.factories;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
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

public class HandlerFactory {

  private final Controller controller;
  private final ParentComponentFactory pcf = new ParentComponentFactory(this);

  public HandlerFactory(Controller controller) {
    this.controller = controller;
  }

  public EventHandler<ActionEvent> makeActionEventHandler(Node component, Element e) {
    String eventType = e.getElementsByTagName("Type").item(0).getTextContent();
    return event -> {
      try {
        Method m = HandlerFactory.class.getDeclaredMethod(eventType, Node.class, Element.class);
        m.invoke(this, component, e);
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    };
  }

  public EventHandler<KeyEvent> makeKeyEventHandler(Node component, Element e) {
    String eventType = e.getElementsByTagName("Type").item(0).getTextContent();
    return event -> {
      try {
        Method m = HandlerFactory.class.getDeclaredMethod(eventType, Node.class, Element.class, KeyEvent.class);
        m.invoke(this, component, e, event);
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    };
  }

  private void launchGame(Node component, Element e) {
    Stage newStage = new Stage();
    String filePath = e.getElementsByTagName("Path").item(0).getTextContent();
    String gameName = e.getElementsByTagName("Game").item(0).getTextContent();
    KeyListener kl = controller.getKeyListener();
    GameView gv = new GameView(gameName, newStage, kl, controller,
        component.getScene().getStylesheets().get(0));
    controller.startGame(gv);
    gv.start(filePath);
  }

  private void startLevel(Node component, Element e) {
    int levelName = Integer.parseInt(e.getElementsByTagName("Level").item(0).getTextContent());
    controller.startLevel(levelName-1);
  }

  private void goToMenu(Node component, Element e) {
    controller.displayMenu();
  }

  private void restartLevel(Node component, Element e) {
    controller.restartLevel();
  }

  private void nextLevel(Node component, Element e) {
    controller.nextLevel();
  }

  private void changeStack(Node component, Element e) {
    try {
      changeStackPane(component, e,
          (Node) pcf.make((Element) e.getElementsByTagName("FilePath").item(0)));
    } catch (Exception exception) {
      exception.printStackTrace();
    }
  }

  private void profileView(Node component, Element e) {
    String pfName = controller.getActiveProfile();
    try {
      if (pfName.equals("")) {
        changeStackPane(component, e,
            (Node) pcf.make((Element) e.getElementsByTagName("FilePath").item(0)));
      } else {
        makeAndDisplayProfile(component, e, pfName);
      }
    } catch (ViewFactoryException vfe) {
      vfe.printStackTrace();
    }
  }

  private void makeAndDisplayProfile(Node component, Element e, String pfName) {
    Profile profile = controller.getProfile(pfName);
    ProfileView pv = new ProfileView(controller, pcf, profile);
    profile.display(pv);
    changeStackPane(component, e, pv.getParent());
  }

  private void profileLogin(Node component, Element e, KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) {
      try {
        String pfName = ((TextField) component).getText();
        controller.setActiveProfile(pfName);
        makeAndDisplayProfile(component, e, pfName);
      } catch (IOException ioException) {
        ioException.printStackTrace();
      }
    }
  }

  private void logout(Node component, Element e) {
    try {
      controller.setActiveProfile("");
      changeStack(component, e);
    } catch (IOException ioException) {
      ioException.printStackTrace();
    }
  }

  private void changeStackPane(Node component, Element e, Node newNode) {
    StackPane sp = (StackPane) component.getScene()
        .lookup("#" + e.getElementsByTagName("PaneID").item(0).getTextContent());
    sp.getChildren().clear();
    newNode.toFront();
    sp.getChildren().add(newNode);
  }

  private void changeTheme(Node component, Element e) {
    String value = (String) ((ChoiceBox<?>) component).getValue();
    ResourceBundle rb = ResourceBundle.getBundle("view_resources/color_themes/ColorThemes");
    component.getScene().getStylesheets().clear();
    component.getScene().getStylesheets().add(rb.getString(value.toUpperCase()));
  }

  private void saveGame(Node component, Element e) {
    controller.saveGame();
  }

  private void loadLibrary(Node component, Element e) {
    String[] saves = controller.getSaves(e.getElementsByTagName("Game").item(0).getTextContent());
    changeStack(component, e);
  }

  private void loadGame(Node component, Element e) {
    String[] decoded = component.getId().split("_");
    controller.loadGame(decoded[0], decoded[1], decoded[2]);
  }
}
