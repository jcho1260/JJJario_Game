package ooga.controller;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.util.Pair;
import ooga.model.GameWorld;
import ooga.model.gameobjects.GameObject;
import ooga.model.gameobjects.MovingDestroyable;
import ooga.model.util.MethodBundle;
import ooga.model.util.Vector;
import ooga.view.game.GameView;
import ooga.view.game.Sprite;
import ooga.view.launcher.BuilderView;
import ooga.view.launcher.ExceptionView;

public class Controller {

  private final CollisionsParser collisionsParser;
  private final double frameRate;
  private final ModelListener highscoreListener;
  private LevelParser gameWorldFactory;
  private LevelNameParser levelNameParser;
  private GameWorld gameWorld;
  private GameView gameView;
  private KeyListener keyListener;
  private Timeline animation;
  private String activeProfile;
  private int currentLevel;
  private int totalLevels;
  private final GameSaver gameSaver;
  private GameMaker gameMaker;
  private BuilderView builderView;
  private String currGame;
  private final ExceptionView exceptionView;

  public Controller(double frameRate, ExceptionView exceptionView) {
    collisionsParser = new CollisionsParser();
    this.frameRate = frameRate;
    this.exceptionView = exceptionView;
    try {
      keyListener = new KeyListener(new Profile("default").getKeybinds());
    } catch (Exception e) {
      reportError(e);
    }
    activeProfile = "";
    highscoreListener = new ModelListener();
    highscoreListener.addController(this);
    gameSaver = new GameSaver();
  }

  public int getNumLevels() {
    try {
      levelNameParser = new LevelNameParser(new File("data/" + currGame + "/LevelNames.xml"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return levelNameParser.numLevels();
  }

  public String getLevelName(int n) {
    try {
      levelNameParser = new LevelNameParser(new File("data/" + currGame + "/LevelNames.xml"));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return levelNameParser.getLevelName(n);
  }

  public String[] getUserDefinedLevels() {
    File folder = new File("data/UserDefined/" + currGame);
    return Arrays.stream(folder.listFiles()).map(file -> file.getName().replaceAll(".game", ""))
        .filter(name -> !name.equals("FOLDER_PURPOSE.md")).toArray(String[]::new);
  }

  public void startGame(GameView gameView) {
    this.gameView = gameView;
  }

  public void startLevel(int n) {

    currentLevel = n;

    String gameName = currGame;
    File levelNameFile = new File("data/" + gameName + "/LevelNames.xml");

    try {
      levelNameParser = new LevelNameParser(levelNameFile);
      totalLevels = levelNameParser.numLevels();
      File collisionsFile = new File("data/" + gameName + "/collisions.xml");
      File levelFile = new File(
          "data/" + gameName + "/" + levelNameParser.getLevelName(n) + ".xml");

      Map<String, Map<String, List<MethodBundle>>> collisions = collisionsParser
          .parseCollisions(collisionsFile);

      gameWorldFactory = new LevelParser(levelFile);
      gameWorld = gameWorldFactory.createGameWorld(collisions, frameRate);
    } catch (Exception e) {
      e.printStackTrace();
    }

    start();
  }

  public void setCurrGame(String game) {
    this.currGame = game;
  }

  private void start() {
    gameWorld.addListener(highscoreListener);

    Vector size = gameWorldFactory.getFrameSize();
    gameView.initializeLevel(size.getX(), size.getY(),
        "view_resources/images/backgrounds/" + currGame + ".png");
    highscoreListener.reset();
    keyListener.reset();
    gameView.propertyChange(new PropertyChangeEvent(this, "addScore", null, 0));

    addSprites(gameWorld);
    gameView.startLevel();

    KeyFrame frame = new KeyFrame(Duration.seconds(1 / frameRate), e -> step(1 / frameRate));
    animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();
  }

  public void startGameMaker(String game, BuilderView builderView) {
    this.builderView = builderView;
    gameMaker = new GameMaker(game);
  }

  public void addObjectToGameMaker(GameObjectMaker gom) {
    gameMaker.addGameObjectMaker(gom);
  }

  public void setGameMakerPlayer(Vector pos, Vector size) {
    try {
      gameMaker.createPlayer(pos, size);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public List<String> getEntityTypes(String name) {
    try {
      return gameMaker.getEntityType(name);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void saveGameMaker(String gameName, String levelName, Vector frameSize, double frameRate,
      Vector minScreen, Vector maxScreen) {
    try {
      GameWorld gw = gameMaker.makeGameWorld(gameName, frameSize, frameRate, minScreen, maxScreen);
      gameMaker.saveGame(levelName, gw);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void loadUserDefinedName(String name) {
    try {
      gameMaker = new GameMaker(currGame);
      gameWorld = gameMaker.loadGame(currGame, name);
    } catch (Exception e) {
      reportError(e);
    }
    start();
  }

  public int getNumGameMakers() {
    return gameMaker.getNumObjects();
  }

  public List<Pair<String, String>> getAllGameObjectsForMaker() {
    try {
      return gameMaker.getGameObjects();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void displayBuilderSprite(String imageName, Vector pos, Vector size) {
    builderView.displayBuilderSprite(imageName, pos, size);
  }

  public void saveGame() {
    String pattern = "MM-dd-yyyy_HH_mm_ss";
    DateFormat df = new SimpleDateFormat(pattern);
    String dateString = df.format(new Date());

    try {
      gameSaver
          .saveGame(currGame, levelNameParser.getLevelName(currentLevel), dateString, gameWorld);
    } catch (Exception e) {
      reportError(e);
    }
  }

  public void loadGame(String level, String dateString) {
    try {
      gameWorld = gameSaver.loadGame(currGame, level, dateString);
    } catch (Exception e) {
      reportError(e);
    }
    start();
  }

  public Pair<String, String>[] getSaves() {
    File levelNameFile = new File("data/" + currGame + "/LevelNames.xml");
    try {
      levelNameParser = new LevelNameParser(levelNameFile);
    } catch (Exception e) {
      e.printStackTrace();
    }

    List<Pair<String, String>> levels = new ArrayList<>();
    int numLevels = levelNameParser.numLevels();
    for (int i = 0; i < numLevels; i++) {
      String level = levelNameParser.getLevelName(i);
      File folder = new File("data/saves/" + currGame + "/" + level);
      if (folder.exists()) {
        levels.addAll(Arrays.stream(folder.listFiles()).map(file -> new Pair<>(level,
            file.getName())).collect(Collectors.toList()));
      }
    }
    return levels.toArray(Pair[]::new);
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
    } catch (IOException | ClassNotFoundException e) {
      try {
        return new Profile(name);
      } catch (Exception ex) {
        reportError(e);
      }
    }
    return null;
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

  public void addCreatable(Vector pos) {
    int id = new Random().nextInt(Integer.MAX_VALUE);
    MovingDestroyable md = gameWorldFactory.makeCreatable(pos, id);
    List<MovingDestroyable> mdList = new ArrayList<>();
    mdList.add(md);
    gameWorld.queueNewMovingDestroyable(mdList);
    addSprite(md);
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
        gameView.propertyChange(
            new PropertyChangeEvent(this, "changeScore", null, (int) highscoreListener.getScore()));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private void handleHighscore(double score) {
    Profile profile = getProfile(activeProfile);

    profile.getHighScores().computeIfAbsent(currGame, k -> new HashMap<>());
    Map<String, Integer> scores = profile.getHighScores().get(currGame);

    String levelName = levelNameParser.getLevelName(currentLevel);
    if (scores.get(levelName) == null || scores.get(levelName) < score) {
      scores.put(levelName, (int) score);
      profile.propertyChange(new PropertyChangeEvent(profile, "mapUpdated", null, null));
    }
  }

  private void addSprites(GameWorld gameWorld) {
    List<GameObject> gameObjects = gameWorld.getAllGameObjects();
    for (GameObject gameObject : gameObjects) {
      addSprite(gameObject);
    }
  }

  private void addSprite(GameObject gameObject) {
    String name = gameObject.getEntityType().get(gameObject.getEntityType().size() - 1);
    Sprite s = new Sprite(currGame, name, gameObject.getSize().getX(), gameObject.getSize().getY(),
        gameObject.getPosition().getX(), gameObject.getPosition().getY());
    gameObject.addListener(s);
    gameView.propertyChange(new PropertyChangeEvent(this, "addSprite", null, s));
  }

  public void displayMenu() {
    gameView.displayMenu();
  }

  private void reportError(Exception e) {
    exceptionView.displayError(e);
  }
}
