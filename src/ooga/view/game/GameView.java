package ooga.view.game;

import javafx.scene.Scene;
import javafx.stage.Stage;
import ooga.view.factories.SceneFactory;

public class GameView {
  private final Stage stage;
  private final String gameName;

  public GameView(String gameName, Stage stage) {
    this.stage = stage;
    this.gameName = gameName;
  }

  public void start(String filePath) {
    try {
      SceneFactory sf = new SceneFactory(stage);
      Scene scene = sf.make(filePath);
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getGameName() { return this.gameName; }
}
