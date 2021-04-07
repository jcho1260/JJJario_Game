package ooga.view.factories;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.w3c.dom.Element;

public class ActionFactory {
  public ActionFactory() {

  }

  public EventHandler<ActionEvent> makeAction(Element e) {
    String actionType = e.getElementsByTagName("Type").item(0).getTextContent();
    if (actionType.equals("NewScreen")) {
      return makeNewScreenAction(e);
    }
    return null;
  }

  private EventHandler<ActionEvent> makeNewScreenAction(Element e) {
    return event -> {
      SceneFactory sf = new SceneFactory();
      String filePath = e.getElementsByTagName("Path").item(0).getTextContent();
      try {
        Scene scene = sf.make(filePath);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
      } catch (Exception exception) {
        exception.printStackTrace();
      }
    };
  }
}
