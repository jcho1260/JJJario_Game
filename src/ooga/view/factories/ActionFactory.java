package ooga.view.factories;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import ooga.view.game.GameView;
import org.w3c.dom.Element;

public class ActionFactory {
  private final Stage stage;

  public ActionFactory(Stage stage) {
    this.stage = stage;
  }

  public EventHandler<ActionEvent> makeAction(Element e) {
    String actionType = e.getElementsByTagName("Type").item(0).getTextContent();
    if (actionType.equals("LaunchGame")) {
      return makeLaunchGameAction(e);
    }
    return null;
  }

  private EventHandler<ActionEvent> makeLaunchGameAction(Element e) {
    return event -> {
      Stage newStage = new Stage();
      String filePath = e.getElementsByTagName("Path").item(0).getTextContent();
      String gameName = e.getElementsByTagName("Game").item(0).getTextContent();
      GameView gv = new GameView(gameName, newStage);
      gv.start(filePath);
    };
  }
}
