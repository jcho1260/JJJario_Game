package ooga.view.factories;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import ooga.controller.Controller;
import ooga.controller.KeyListener;
import ooga.view.game.GameView;
import org.w3c.dom.Element;

public class ActionFactory {
  private final Stage stage;
  private final Controller controller;

  public ActionFactory(Stage stage, Controller controller) {
    this.stage = stage;
    this.controller = controller;
  }

  public EventHandler<ActionEvent> makeAction(Element e) {
    String actionType = e.getElementsByTagName("Type").item(0).getTextContent();
    if (actionType.equals("LaunchGame")) {
      return makeLaunchGameAction(e);
    } else if (actionType.equals("StartLevel")) {
      return makeStartLevelAction(e);
    }
    return null;
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
}
