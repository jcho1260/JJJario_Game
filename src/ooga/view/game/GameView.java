package ooga.view.game;

import java.beans.PropertyChangeEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import ooga.controller.Controller;
import ooga.controller.KeyListener;
import ooga.view.factories.SceneFactory;

public class GameView {
  private final Stage stage;
  private final String gameName;
  private final KeyListener kl;

  public GameView(String gameName, Stage stage, KeyListener kl) {
    this.stage = stage;
    this.gameName = gameName;
    this.kl = kl;
  }

  public void start(String filePath, Controller controller) {
    try {
      SceneFactory sf = new SceneFactory(stage, controller);
      Scene scene = sf.make(filePath);
      scene.setOnKeyPressed(makeKeyAction());
      scene.setOnKeyReleased(makeKeyAction());
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void startLevel() {
    Scene scene = new Scene(new Group());
    scene.setOnKeyPressed(makeKeyAction());
    scene.setOnKeyReleased(makeKeyAction());
  }

  public String getGameName() { return this.gameName; }

  private EventHandler<KeyEvent> makeKeyAction() {
    return event -> kl.propertyChange(new PropertyChangeEvent(this, "currKey", null, event));
  }
}
