package ooga.view.factories;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ooga.controller.Controller;
import ooga.controller.KeyListener;
import ooga.view.game.GameView;
import org.w3c.dom.Element;

public class ActionFactory {
  private final Controller controller;
  private final ParentComponentFactory pcf = new ParentComponentFactory(this);

  public ActionFactory(Controller controller) {
    this.controller = controller;
  }

  public EventHandler<ActionEvent> makeAction(Node component, Element e) {
    String actionType = e.getElementsByTagName("Type").item(0).getTextContent();
    return switch (actionType) {
      case "LaunchGame" -> makeLaunchGameAction(e);
      case "StartLevel" -> makeStartLevelAction(e);
      case "ChangeStack" -> makeChangeStackAction(component, e);
      default -> null;
    };
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
        StackPane sp = (StackPane) component.getScene().lookup("#"+e.getElementsByTagName("PaneID").item(0).getTextContent());
        Node newNode = (Node) pcf.make((Element) e.getElementsByTagName("FilePath").item(0));
        newNode.toFront();
        sp.getChildren().add(newNode);
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    };
  }
}
