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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ooga.controller.Controller;
import ooga.controller.KeyListener;
import ooga.view.factories.SceneFactory;

public class GameView implements PropertyChangeListener {

  private final Controller controller;
  private final Stage stage;
  private final String gameName;
  private final KeyListener kl;
  private final SceneFactory sf;
  private final String colorTheme;
  private Scene menuScene;
  private Scene currScene;
  private Scene cachedScene;
  private boolean inGameMenu = false;

  public GameView(String gameName, Stage stage, KeyListener kl, Controller controller,
      String colorTheme) {
    this.controller = controller;
    this.colorTheme = colorTheme;
    this.stage = stage;
    this.gameName = gameName;
    this.kl = kl;
    sf = new SceneFactory(controller);

    stage.setOnCloseRequest(event -> controller.endGame());
  }

  public void start(String filePath) {
    try {
      menuScene = sf.make(filePath);
      menuScene.getStylesheets().add(colorTheme);
      stage.setTitle(gameName);
      stage.setScene(menuScene);
      currScene = menuScene;
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void displayMenu() {
    currScene = menuScene;
    stage.setScene(menuScene);
    stage.show();
  }

  public void initializeLevel(double w, double h, String imagePath) {
    Group g = new Group();
    g.getStylesheets().add("view_resources/game/css/ScoreText.css");
    g.setId(gameName + "LevelView");
    Scene newScene = new Scene(g, w, h);
    newScene.setOnKeyPressed(makeKeyActionPress());
    newScene.setOnKeyReleased(makeKeyActionRelease());
    ImageView background = new ImageView(
        new Image(Objects.requireNonNull(
            getClass().getClassLoader().getResourceAsStream(imagePath))));
    background.setX(0);
    background.setY(0);
//    background.setPreserveRatio(true);
    background.setFitWidth(w);
    background.setFitHeight(h);
    g.getChildren().add(background);
    newScene.getStylesheets().addAll(currScene.getStylesheets());
    currScene = newScene;
  }

  public void addSprite(Sprite s) {
    ((Group) currScene.getRoot()).getChildren().add(s.getImageView());
  }

  public void addScore(int score) {
    Text t = new Text("Score: " + score);
    t.setId("ScoreText");
    t.setX(10);
    t.setY(30);
    ((Group) currScene.getRoot()).getChildren().add(t);
  }

  public void changeScore(int score) {
    ((Text) currScene.lookup("#ScoreText")).setText("Score: " + score);
  }

  public void startLevel() {
    stage.setScene(currScene);
    stage.show();
  }

  public void gameOver() {
    try {
      Scene newScene = sf.make("resources/view_resources/game/GameLost.XML");
      newScene.getStylesheets().addAll(currScene.getStylesheets());
      currScene = newScene;
      stage.setScene(currScene);
      stage.show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void gameWin() {
    try {
      Scene newScene = sf.make("resources/view_resources/game/GameWon.XML");
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

  private void addStandardLevels() {

  }

  private EventHandler<KeyEvent> makeKeyActionRelease() {
    return event -> {
      kl.propertyChange(new PropertyChangeEvent(this, "currKey", null, event));
    };
  }

  private EventHandler<KeyEvent> makeKeyActionPress() {
    return event -> {
      if (event.getCode() == KeyCode.ESCAPE) {
        toggleGameMenu();
      }
      kl.propertyChange(new PropertyChangeEvent(this, "currKey", null, event));
    };
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

  private void toggleGameMenu() {
    if (inGameMenu) {
      currScene = cachedScene;
      stage.setScene(currScene);
      stage.show();
      controller.togglePaused();
      inGameMenu = false;
    } else {
      controller.togglePaused();
      cachedScene = currScene;
      try {
        Scene newScene = sf.make("resources/view_resources/game/InternalMenu.XML");
        newScene.getStylesheets().addAll(currScene.getStylesheets());
        newScene.setOnKeyPressed(event -> {
          if (event.getCode() == KeyCode.ESCAPE) {
            toggleGameMenu();
          }
        });
        currScene = newScene;
        inGameMenu = true;
        stage.setScene(currScene);
        stage.show();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
