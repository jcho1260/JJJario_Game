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
import ooga.view.launcher.ExceptionView;

/**
 * This class provides the user interface for playing games and levels. This class itself is a
 * PropertyChangeListener to receive updates on the score and health of the player as well as all
 * currently active Sprites to display. It is dependent on ooga.controller.Controller,
 * ooga.controller.KeyListener, ooga.view.factories.SceneFactory, and
 * ooga.view.launcher.ExceptionView.
 *
 * @author Adam Hufstetler
 */
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

  /**
   * @param gameName String
   * @param stage Stage
   * @param kl KeyListener for user keyboard input
   * @param controller Controller
   * @param colorTheme Stylesheet string that defines the default colors for the UI
   */
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

  /**
   * Starts the game at the home menu screen.
   *
   * @param filePath String
   */
  public void start(String filePath) {
    try {
      menuScene = sf.make(filePath);
      menuScene.getStylesheets().add(colorTheme);
      ((ImageView) menuScene.lookup("#Logo")).setImage(
          new Image(
              Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(
                  "view_resources/images/logo/" + gameName + "Logo.png"))));
      stage.setTitle(gameName);
      stage.setScene(menuScene);
      currScene = menuScene;
      stage.setResizable(false);
      stage.show();
    } catch (Exception e) {
      new ExceptionView().displayError(e);
    }
  }

  /**
   * Returns the user interface to the game main menu.
   */
  public void displayMenu() {
    currScene = menuScene;
    stage.setScene(menuScene);
    stage.show();
  }

  /**
   * Initializes a level to be displayed later.
   *
   * @param w game viewport width
   * @param h game viewport height
   * @param imagePath file path for the background
   */
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

  /**
   * Adds a sprite, only called via propertyChange().
   *
   * @param s Sprite
   */
  public void addSprite(Sprite s) {
    ((Group) currScene.getRoot()).getChildren().add(s.getImageView());
  }

  /**
   * Adds a score, only called via propertyChange().
   *
   * @param score int
   */
  public void addScore(int score) {
    Text t = new Text("Score: " + score);
    t.setId("ScoreText");
    t.setX(10);
    t.setY(30);
    ((Group) currScene.getRoot()).getChildren().add(t);
  }

  /**
   * Adds player life, only called via propertyChange().
   *
   * @param life int
   */
  public void addLife(int life) {
    Text t = new Text("Lives: " + life);
    t.setId("LifeText");
    t.setX(10);
    t.setY(60);
    ((Group) currScene.getRoot()).getChildren().add(t);
  }

  /**
   * Adds player health, only called via propertyChange().
   *
   * @param health int
   */
  public void addHealth(int health) {
    Text t = new Text("Health: " + health);
    t.setId("HealthText");
    t.setX(10);
    t.setY(90);
    ((Group) currScene.getRoot()).getChildren().add(t);
  }

  /**
   * Changes the score, only called via propertyChange(). Assumed to be called only after addScore
   * is called.
   *
   * @param score int
   */
  public void changeScore(int score) {
    ((Text) currScene.lookup("#ScoreText")).setText("Score: " + score);
  }

  /**
   * Changes the life, only called via propertyChange(). Assumed to be called only after addScore
   * is called.
   *
   * @param newLife int
   */
  public void changeLife(int newLife) {
    ((Text) currScene.lookup("#LifeText")).setText("Lives: " + newLife);
  }

  /**
   * Changes the health, only called via propertyChange(). Assumed to be called only after addScore
   * is called.
   *
   * @param newHealth int
   */
  public void changeHealth(int newHealth) {
    ((Text) currScene.lookup("#HealthText")).setText("Health: " + newHealth);
  }

  /**
   * Displays the level previously initialized.
   */
  public void startLevel() {
    stage.setScene(currScene);
    stage.show();
  }

  /**
   * Displays the game over screen.
   */
  public void gameOver() {
    try {
      Scene newScene = sf.make("resources/view_resources/game/GameLost.XML");
      newScene.getStylesheets().addAll(currScene.getStylesheets());
      currScene = newScene;
      stage.setScene(currScene);
      stage.show();
    } catch (Exception e) {
      new ExceptionView().displayError(e);
    }
  }

  /**
   * Displays the game win screen.
   */
  public void gameWin() {
    try {
      Scene newScene = sf.make("resources/view_resources/game/GameWon.XML");
      newScene.getStylesheets().addAll(currScene.getStylesheets());
      currScene = newScene;
      stage.setScene(currScene);
      stage.show();
    } catch (Exception e) {
      new ExceptionView().displayError(e);
    }
  }

  /**
   * Returns the game name.
   *
   * @return the game name.
   */
  public String getGameName() {
    return this.gameName;
  }

  private EventHandler<KeyEvent> makeKeyActionRelease() {
    return event -> kl.propertyChange(new PropertyChangeEvent(this, "currKey", null, event));
  }

  private EventHandler<KeyEvent> makeKeyActionPress() {
    return event -> {
      if (event.getCode() == KeyCode.ESCAPE) {
        toggleGameMenu();
      }
      kl.propertyChange(new PropertyChangeEvent(this, "currKey", null, event));
    };
  }

  /**
   * Used to add images and text to the game viewport.
   *
   * @param evt the property change event that encapsulates the neccesary information.
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String mName = evt.getPropertyName();
    Object[] mArgs = new Object[]{evt.getNewValue()};
    try {
      new Statement(this, mName, mArgs).execute();
    } catch (Exception e) {
      new ExceptionView().displayError(e);
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
        new ExceptionView().displayError(e);
      }
    }
  }
}
