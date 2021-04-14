package ooga.view.game;

import java.beans.PropertyChangeEvent;
import java.util.Objects;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ooga.controller.Controller;
import ooga.controller.KeyListener;
import ooga.view.factories.SceneFactory;

public class GameView {

  private final Stage stage;
  private final String gameName;
  private final KeyListener kl;
  private final SceneFactory sf;
  private Scene currScene;

  public GameView(String gameName, Stage stage, KeyListener kl, Controller controller) {
    this.stage = stage;
    this.gameName = gameName;
    this.kl = kl;
    sf = new SceneFactory(controller);
  }

  public void start(String filePath) {
    try {
      currScene = sf.make(filePath);
      currScene.setOnKeyPressed(makeKeyAction());
      currScene.setOnKeyReleased(makeKeyAction());
      stage.setScene(currScene);
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void initializeLevel(double w, double h, String imagePath) {
    Group g = new Group();
    g.setId(gameName + "LevelView");
    Scene newScene = new Scene(g, w, h);
    newScene.setOnKeyPressed(makeKeyAction());
    newScene.setOnKeyReleased(makeKeyAction());
    ImageView bi = new ImageView(
        new Image(Objects.requireNonNull(
            getClass().getClassLoader().getResourceAsStream(imagePath))));
    bi.setX(0);
    bi.setY(0);
    bi.setPreserveRatio(true);
    bi.setFitHeight(h);
    g.getChildren().add(bi);
    currScene = newScene;
  }

  public void addSprite(Sprite s) {
    ((Group) currScene.getRoot()).getChildren().add(s.getImageView());
  }

  public void addScore() {

  }

  public void startLevel() {
    stage.setScene(currScene);
    stage.show();
  }

  public void gameOver() {
    try {
      currScene = sf.make("resources/view_resources/game/GameOver.XML");
      stage.setScene(currScene);
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String getGameName() {
    return this.gameName;
  }

  private EventHandler<KeyEvent> makeKeyAction() {
    return event -> kl.propertyChange(new PropertyChangeEvent(this, "currKey", null, event));
  }
}
