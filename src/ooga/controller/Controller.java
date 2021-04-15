package ooga.controller;

import java.beans.PropertyChangeEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import ooga.model.GameWorld;
import ooga.model.gameobjects.GameObject;
import ooga.model.util.MethodBundle;
import ooga.model.util.Vector;
import ooga.view.game.GameView;
import ooga.view.game.Sprite;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Controller {

  private LevelParser gameWorldFactory;
  private final CollisionsParser collisionsParser;
  private GameWorld gameWorld;
  private final Vector frameSize;
  private final double frameRate;
  private GameView gameView;
  private KeyListener keyListener;
  private Timeline animation;
  private String activeProfile;
  private ScoreListener highscoreListener;

  public Controller(Vector frameSize, double frameRate) {
    collisionsParser = new CollisionsParser();
    this.frameSize =  frameSize;
    this.frameRate = frameRate;
    keyListener = new KeyListener(new Profile("default").getKeybinds());
    activeProfile = "";
    highscoreListener = new ScoreListener();
  }

  public void startGame(GameView gameView) {
    this.gameView = gameView;
  }

  public void startLevel(String levelName) {
    String gameName = gameView.getGameName();
    File collisionsFile = new File("data/" + gameName + "/collisions.xml");
    File levelFile = new File("data/" + gameName + "/level.xml");

    try {
      Map<String, Map<String, List<MethodBundle>>> collisions = collisionsParser.parseCollisions(collisionsFile);

      gameWorldFactory = new LevelParser(levelFile);
      gameWorld = gameWorldFactory.createGameWorld(collisions, frameSize, frameRate);
      gameWorld.addListener(highscoreListener);

      String background = gameWorldFactory.getBackground(levelFile);
      System.out.println(background);
      gameView.initializeLevel(frameSize.getX(), frameSize.getY(), background);
      gameView.propertyChange(new PropertyChangeEvent(this, "addScore", null, (int) highscoreListener.getScore()));
    } catch (Exception e) {
      e.printStackTrace();
    }

    addSprites(gameWorld);
    gameView.startLevel();

    KeyFrame frame = new KeyFrame(Duration.seconds(1/frameRate), e -> step(1/frameRate));
    animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();
  }

  public KeyListener getKeyListener() {
    return keyListener;
  }

  public Profile getProfile(String name) {
    try {
      FileInputStream in = new FileInputStream("data/profiles/" + name + ".player");
      ObjectInputStream s = new ObjectInputStream(in);
      return (Profile) s.readObject();
    } catch(IOException | ClassNotFoundException e) {
      return new Profile(name);
    }
  }

  public String getActiveProfile() {
    return activeProfile;
  }

  public void setActiveProfile(String name) throws IOException {
    activeProfile = name;
    keyListener = new KeyListener(getProfile(activeProfile).getKeybinds());
  }

  public void endGame() {
    if (animation != null) {
      animation.stop();
    }
  }

  private void step(double d) {
    if (gameWorld.isGameOver()) {
      double finalScore = highscoreListener.getScore();
      handleHighscore(finalScore);
      endGame();
      gameView.gameOver();
      return;
    }
    try {
      gameWorld.stepFrame(keyListener.getCurrentKey());
      gameView.propertyChange(new PropertyChangeEvent(this, "changeScore", null, (int) highscoreListener.getScore()));
    } catch (Exception e){
      e.printStackTrace();
    }
  }

  private void handleHighscore(double score) {
    Profile profile = getProfile(activeProfile);

    profile.getHighScores().computeIfAbsent(gameView.getGameName(), k -> new HashMap<>());
    Map<String, Integer> scores = profile.getHighScores().get(gameView.getGameName());

    if (scores.get("level1") == null || scores.get("level1") < score) {
      scores.put("level1", (int) score);
      profile.propertyChange(new PropertyChangeEvent(profile, "mapChanged", null, null));
    }
  }

  private void addSprites(GameWorld gameWorld) {
    List<GameObject> gameObjects = gameWorld.getAllGameObjects();
    for (GameObject gameObject : gameObjects) {
      String name = gameObject.getEntityType().get(gameObject.getEntityType().size()-1);
      Sprite s = new Sprite(gameView.getGameName(), name, gameObject.getSize().getX(), gameObject.getSize().getY(), gameObject.getPosition().getX(), gameObject.getPosition().getY());
      gameObject.addListener(s);
      gameView.propertyChange(new PropertyChangeEvent(this, "addSprite", null, s));
    }
  }
}
