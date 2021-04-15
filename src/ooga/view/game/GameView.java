package ooga.view.game;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.Statement;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ooga.controller.Controller;
import ooga.controller.KeyListener;
import ooga.controller.ScoreListener;
import ooga.view.factories.SceneFactory;

public class GameView implements PropertyChangeListener {

  private final Stage stage;
  private final String gameName;
  private final KeyListener kl;
  private final SceneFactory sf;
  private final String colorTheme;
  private Scene currScene;

  public GameView(String gameName, Stage stage, KeyListener kl, Controller controller, String colorTheme) {
    this.colorTheme = colorTheme;
    this.stage = stage;
    this.gameName = gameName;
    this.kl = kl;
    sf = new SceneFactory(controller);

    stage.setOnCloseRequest(event -> controller.endGame());
  }

  public void start(String filePath) {
    try {
      currScene = sf.make(filePath);
      currScene.setOnKeyPressed(makeKeyAction());
      currScene.setOnKeyReleased(makeKeyAction());
      currScene.getStylesheets().add(colorTheme);
      stage.setTitle(gameName);
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
    newScene.getStylesheets().addAll(currScene.getStylesheets());
    currScene = newScene;
  }

  public void addSprite(Sprite s) {
    ((Group) currScene.getRoot()).getChildren().add(s.getImageView());
  }

  public void addScore(int score) {
    Text t = new Text("Score: "+score);
    t.setId("ScoreText");
    t.setX(10);
    t.setY(20);
    ((Group) currScene.getRoot()).getChildren().add(t);
  }

  public void changeScore(int score) {
    ((Text) currScene.lookup("#ScoreText")).setText("Score: "+score);
  }

  public void startLevel() {
    stage.setScene(currScene);
    stage.show();
  }

  public void gameOver() {
    try {
      Scene newScene = sf.make("resources/view_resources/game/GameOver.XML");
      newScene.getStylesheets().addAll(currScene.getStylesheets());
      currScene = newScene;
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

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String mName = evt.getPropertyName();
    Object[] mArgs = new Object[]{evt.getNewValue()};
    try {
      new Statement(this, mName, mArgs).execute();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
