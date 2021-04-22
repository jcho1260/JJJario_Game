package ooga.controller;

import java.beans.PropertyChangeEvent;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javax.xml.parsers.ParserConfigurationException;
import ooga.model.GameWorld;
import ooga.model.gameobjects.GameObject;
import ooga.model.util.MethodBundle;
import ooga.model.util.Vector;
import ooga.view.game.GameView;
import ooga.view.game.Sprite;

import java.io.File;
import java.util.List;
import java.util.Map;
import org.xml.sax.SAXException;

public class Controller {

  private LevelParser gameWorldFactory;
  private LevelNameParser levelNameParser;
  private final CollisionsParser collisionsParser;
  private GameWorld gameWorld;
  private final Vector frameSize;
  private final double frameRate;
  private GameView gameView;
  private KeyListener keyListener;
  private Timeline animation;
  private String activeProfile;
  private final ScoreListener highscoreListener;
  private int currentLevel;
  private int totalLevels;
  private GameSaver gameSaver;

  public Controller(Vector frameSize, double frameRate) {
    collisionsParser = new CollisionsParser();
    this.frameSize =  frameSize;
    this.frameRate = frameRate;
    keyListener = new KeyListener(new Profile("default").getKeybinds());
    activeProfile = "";
    highscoreListener = new ScoreListener();
    gameSaver = new GameSaver();
  }

  public void startGame(GameView gameView) {
    this.gameView = gameView;
  }

  public void startLevel(int n) {

    currentLevel = n;

    String gameName = gameView.getGameName();
    File levelNameFile = new File("data/" + gameName + "/LevelNames.xml");

    try {
      levelNameParser = new LevelNameParser(levelNameFile);
      totalLevels = levelNameParser.numLevels();
      File collisionsFile = new File("data/" + gameName + "/collisions.xml");
      File levelFile = new File("data/" + gameName + "/" + levelNameParser.getLevelName(n) + ".xml");

      Map<String, Map<String, List<MethodBundle>>> collisions = collisionsParser.parseCollisions(collisionsFile);

      gameWorldFactory = new LevelParser(levelFile);
      gameWorld = gameWorldFactory.createGameWorld(collisions, frameSize, frameRate);
    } catch (Exception e) {
      e.printStackTrace();
    }

    start();
  }

  private void start() {
    gameWorld.addListener(highscoreListener);

    gameView.initializeLevel(frameSize.getX(), frameSize.getY(), "view_resources/images/backgrounds/JJJario.png");
    highscoreListener.reset();
    keyListener.reset();
    gameView.propertyChange(new PropertyChangeEvent(this, "addScore", null, 0));

    addSprites(gameWorld);
    gameView.startLevel();

    KeyFrame frame = new KeyFrame(Duration.seconds(1/frameRate), e -> step(1/frameRate));
    animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();
  }

  public void saveGame() {
    String pattern = "MM-dd-yyyy_HH_mm_ss";
    DateFormat df = new SimpleDateFormat(pattern);
    String dateString = df.format(new Date());

    gameSaver.saveGame(gameView.getGameName(), levelNameParser.getLevelName(currentLevel), dateString, gameWorld);
  }

  public void loadGame(String level, String dateString) {
    gameWorld = gameSaver.loadGame(gameView.getGameName(), level, dateString);
    start();
  }

  public String[] getSaves(String game) {
    File levelNameFile = new File("data/" + game + "/LevelNames.xml");
    try {
      levelNameParser = new LevelNameParser(levelNameFile);
    } catch (Exception e) {
      e.printStackTrace();
    }

    List<String> levels = new ArrayList<>();
    int numLevels = levelNameParser.numLevels();
    for (int i = 0; i < numLevels; i++) {
      String level =  levelNameParser.getLevelName(i);
      File folder = new File("data/saves/" + game + "/" + level);
      if (folder.exists()) {
        levels.addAll(Arrays.stream(folder.listFiles()).map(file -> level + "/" + file.getName()).collect(Collectors.toList()));
      }
    }
    return levels.toArray(String[]::new);
  }

  public void nextLevel() {
    currentLevel++;
    if (currentLevel == totalLevels) {
      gameView.displayMenu();
      return;
    }
    startLevel(currentLevel);
  }

  public void restartLevel() {
    startLevel(currentLevel);
  }

  public void togglePaused() {
    Status status = animation.getStatus();
    if (status == Status.PAUSED) {
      animation.play();
    } else {
      animation.pause();
    }
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

    double finalScore = highscoreListener.getScore();

    if (gameWorld.isGameOver()) {
      handleHighscore(finalScore);
      endGame();
      gameView.gameOver();
    } else if (gameWorld.didPlayerWin()) {
      handleHighscore(finalScore);
      endGame();
      gameView.gameWin();
    } else {
      try {
        gameWorld.stepFrame(keyListener.getCurrentKey());
        gameView.propertyChange(new PropertyChangeEvent(this, "changeScore", null, (int) highscoreListener.getScore()));
      } catch (Exception e){
        e.printStackTrace();
      }
    }
  }

  private void handleHighscore(double score) {
    Profile profile = getProfile(activeProfile);

    profile.getHighScores().computeIfAbsent(gameView.getGameName(), k -> new HashMap<>());
    Map<String, Integer> scores = profile.getHighScores().get(gameView.getGameName());

    String levelName = levelNameParser.getLevelName(currentLevel);
    if (scores.get(levelName) == null || scores.get(levelName) < score) {
      scores.put(levelName, (int) score);
      profile.propertyChange(new PropertyChangeEvent(profile, "mapUpdated", null, null));
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

  public void displayMenu() {
    gameView.displayMenu();
  }
}
