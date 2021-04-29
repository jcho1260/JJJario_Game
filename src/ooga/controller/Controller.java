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

/**
 * Controller is responsible for coordinating communication between the model and view. It contains utilities
 * for querying information about the available games, pausing games, saving/loading games, and accessing the GameMaker.
 * It is dependent on ooga.model.GameWorld, ooga.model.gameobjects.GameObject, ooga.model.gameobjects.MovingDestroyable,
 * ooga.model.util.MethodBundle, ooga.model.util.Vector, ooga.view.game.GameView, ooga.view.game.Sprite, ooga.view.launcher.BuilderView,
 * and ooga.view.launcher.ExceptionView;
 *
 * @author Noah Citron
 */
public class Controller {

  private final CollisionsParser collisionsParser;
  private final double frameRate;
  private final ModelListener highscoreListener;
  private final GameSaver gameSaver;
  private LevelParser gameWorldFactory;
  private LevelNameParser levelNameParser;
  private GameWorld gameWorld;
  private GameView gameView;
  private KeyListener keyListener;
  private Timeline animation;
  private String activeProfile;
  private int currentLevel;
  private int totalLevels;
  private GameMaker gameMaker;
  private BuilderView builderView;
  private String currGame;

  /**
   * Constructs the controller
   *
   * @param frameRate the framerate of the game
   */
  public Controller(double frameRate) {
    collisionsParser = new CollisionsParser();
    this.frameRate = frameRate;
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

  /**
   * Gets the number of levels available in the current game. Assumes that the controller already has had the current
   * game set
   *
   * @return the number of levels
   */
  public int getNumLevels() {
    try {
      levelNameParser = new LevelNameParser(new File("data/" + currGame + "/LevelNames.xml"));
    } catch (Exception e) {
      reportError(e);
    }
    return levelNameParser.numLevels();
  }

  /**
   * Get the name of the level given the current level number. Assumes the controller already has the current game set
   *
   * @param n the level number
   * @return  the name of the level with the given number
   */
  public String getLevelName(int n) {
    try {
      levelNameParser = new LevelNameParser(new File("data/" + currGame + "/LevelNames.xml"));
    } catch (Exception e) {
      reportError(e);
    }
    return levelNameParser.getLevelName(n);
  }

  public String[] getUserDefinedLevels() {
    File folder = new File("data/UserDefined/" + currGame);
    return Arrays.stream(folder.listFiles()).map(file -> file.getName().replaceAll(".game", ""))
        .filter(name -> !name.equals("FOLDER_PURPOSE.md")).toArray(String[]::new);
  }

  /**
   * Starts a game with the given gameView. This method is called by the gameView when it is instantiated.
   *
   * @param gameView  the current GameView
   */
  public void startGame(GameView gameView) {
    this.gameView = gameView;
  }

  /**
   * Starts a level
   *
   * @param n the level number to start
   */
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
      reportError(e);
    }

    start();
  }

  /**
   * Sets the current game being played
   *
   * @param game current game name
   */
  public void setCurrGame(String game) {
    this.currGame = game;
  }

  private void start() {
    gameWorld.addListener("highscore", highscoreListener);

    Vector size = gameWorldFactory.getFrameSize();
    gameView.initializeLevel(size.getX(), size.getY(),
        "view_resources/images/backgrounds/" + currGame + ".png");
    highscoreListener.reset();
    keyListener.reset();
    gameView.propertyChange(new PropertyChangeEvent(this, "addScore", null, 0));
    gameView.propertyChange(new PropertyChangeEvent(this, "addHealth", null, 0));
    gameView.propertyChange(new PropertyChangeEvent(this, "addLife", null, 0));

    addSprites(gameWorld);
    gameView.startLevel();

    KeyFrame frame = new KeyFrame(Duration.seconds(1 / frameRate), e -> step(1 / frameRate));
    animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();
  }

  /**
   * Starts the GameMaker utility
   *
   * @param game        the game type being built
   * @param builderView the BuilderView frontend component
   */
  public void startGameMaker(String game, BuilderView builderView) {
    this.builderView = builderView;
    gameMaker = new GameMaker(game);
  }

  /**
   * Undoes an action performed by the GameMaker. Assumes their are actions to undo and that the GameMaker has
   * been started
   */
  public void undoGameMaker() {
    gameMaker.removeGameObjectMaker();
  }

  /**
   * Adds a new GameObjectMaker to the GameMaker. Assumes that the GameMaker has been started
   *
   * @param gom the GameObjectMaker representing the new GameObject to be added to the GameMaker
   */
  public void addObjectToGameMaker(GameObjectMaker gom) {
    gameMaker.addGameObjectMaker(gom);
  }

  /**
   * Sets the player's location in the game currenty being built by the GameMaker. Assumes that the GameMaker has been
   * started
   *
   * @param pos   the position of the player
   * @param size  the size of the player
   */
  public void setGameMakerPlayer(Vector pos, Vector size) {
    try {
      gameMaker.createPlayer(pos, size);
    } catch (Exception e) {
      reportError(e);
    }
  }

  /**
   * Gets the tag list of a particular GameObject. Assumes that the GameMaker has been started
   *
   * @param name  name of the GameObject
   * @return      the tag list of the GameObject
   */
  public List<String> getEntityTypes(String name) {
    try {
      return gameMaker.getEntityType(name);
    } catch (Exception e) {
      reportError(e);
    }
    return null;
  }

  /**
   * Saves the GameMaker. Assumes that the GameMaker has been started and that the Player has been set
   *
   * @param gameName  name of the game
   * @param levelName name of the level
   * @param frameSize size of the screen
   * @param frameRate framerate of the game
   * @param minScreen the starting position of the game
   * @param maxScreen the ending position of the game
   */
  public void saveGameMaker(String gameName, String levelName, Vector frameSize, double frameRate,
      Vector minScreen, Vector maxScreen) {
    try {
      GameWorld gw = gameMaker.makeGameWorld(gameName, frameSize, frameRate, minScreen, maxScreen);
      gameMaker.saveGame(levelName, gw);
    } catch (Exception e) {
      reportError(e);
    }
  }

  /**
   * Loads a use defined game built by the GameMaker. Assumes that the current game has been set and there is a
   * user defined game with the given name
   *
   * @param name
   */
  public void loadUserDefinedName(String name) {
    try {
      gameMaker = new GameMaker(currGame);
      gameWorld = gameMaker.loadGame(currGame, name);
      gameWorld.addPlayerListener();
      gameWorldFactory = new LevelParser(new File("data/" + currGame + "/Level1.xml"));
    } catch (Exception e) {
      reportError(e);
    }
    start();
  }

  /**
   * Gets the number of objects instantiated by the GameMaker
   *
   * @return the number of objects instantiated by the GameMaker
   */
  public int getNumGameMakers() {
    return gameMaker.getNumObjects();
  }

  /**
   * Get all the available GameObjects for a given game. This is used by the frontend to display exactly what is
   * available to be built
   *
   * @return  a list of pairs. The key represents the name of the GameObject, while the value represents its type
   */
  public List<Pair<String, String>> getAllGameObjectsForMaker() {
    try {
      return gameMaker.getGameObjects();
    } catch (Exception e) {
      reportError(e);
    }
    return null;
  }

  /**
   * displays a sprite in the BuilderView
   *
   * @param imageName the name of the image for the sprite
   * @param pos       the position of the sprite
   * @param size      the size of the sprite
   */
  public void displayBuilderSprite(String imageName, Vector pos, Vector size) {
    builderView.displayBuilderSprite(imageName, pos, size);
  }

  /**
   * Saves the game that is currently being played. Assumes a game is currently being played
   */
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

  /**
   * Loads a previously saved game. Assumes the current game has been set
   *
   * @param level       the level name
   * @param dateString  the timestamp of when the game was saved
   */
  public void loadGame(String level, String dateString) {
    try {
      gameWorld = gameSaver.loadGame(currGame, level, dateString);
      gameWorldFactory = new LevelParser(new File("data/" + currGame + "/" + level + ".xml"));
    } catch (Exception e) {
      reportError(e);
    }
    start();
  }

  /**
   * Gets an array of saves
   *
   * @return an array of pairs. The key represents the level and the value represents the timestamp
   */
  public Pair<String, String>[] getSaves() {
    File levelNameFile = new File("data/" + currGame + "/LevelNames.xml");
    try {
      levelNameParser = new LevelNameParser(levelNameFile);
    } catch (Exception e) {
      reportError(e);
    }

    List<Pair<String, String>> levels = new ArrayList<>();
    int numLevels = levelNameParser.numLevels();
    for (int i = 0; i < numLevels; i++) {
      String level = levelNameParser.getLevelName(i);
      File folder = new File("data/saves/" + currGame + "/" + level);
      if (folder.exists()) {
        levels.addAll(Arrays.stream(folder.listFiles()).map(file -> new Pair<>(level,
            file.getName())).filter(name -> !name.getValue().equals("FOLDER_PURPOSE.md"))
            .collect(Collectors.toList()));
      }
    }
    return levels.toArray(Pair[]::new);
  }

  /**
   * Advances to the next level. Assumes that a game is currently being played
   */
  public void nextLevel() {
    currentLevel++;
    if (currentLevel == totalLevels) {
      gameView.displayMenu();
      return;
    }
    startLevel(currentLevel);
  }

  /**
   * Restarts the current level. Assumes that the game is currently being played
   */
  public void restartLevel() {
    startLevel(currentLevel);
  }

  /**
   * Pauses / unpauses the game. Assumes a game is currently being played
   */
  public void togglePaused() {
    Status status = animation.getStatus();
    if (status == Status.PAUSED) {
      animation.play();
    } else {
      animation.pause();
    }
  }

  /**
   * Gets the key listener
   *
   * @return The KeyListener
   */
  public KeyListener getKeyListener() {
    return keyListener;
  }

  /**
   * Gets a profile given the name
   *
   * @param name  profile name
   * @return      profile
   */
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

  /**
   * Gets the name of the currently active profile. Defaults to "default"
   *
   * @return the name of the currently active profile
   */
  public String getActiveProfile() {
    return activeProfile;
  }

  /**
   * Sets the currently active profile
   *
   * @param name  the name of the profile to set to be active
   * @throws IOException
   */
  public void setActiveProfile(String name) throws IOException {
    activeProfile = name;
    keyListener = new KeyListener(getProfile(activeProfile).getKeybinds());
  }

  /**
   * Ends the game. Assumes a game is currently being played
   */
  public void endGame() {
    if (animation != null) {
      animation.stop();
    }
  }

  /**
   * Adds a creatable dynamically to the game. Assumes that Level.xml file defines that this game has creatables
   *
   * @param pos the position to generate the creatable at
   */
  public void addCreatable(Vector pos) {
    int id = new Random().nextInt(Integer.MAX_VALUE);
    MovingDestroyable md = gameWorldFactory.makeCreatable(pos, id);
    List<MovingDestroyable> mdList = new ArrayList<>();
    mdList.add(md);
    gameWorld.queueNewMovingDestroyable(mdList);
    String name = md.getEntityType().get(md.getEntityType().size() - 1);
    Sprite s = new Sprite(gameView.getGameName(), name, md.getSize().getX(), md.getSize().getY(),
        md.getPosition().getX(), md.getPosition().getY());
    md.addListener("sprite", s);
    gameView.propertyChange(new PropertyChangeEvent(this, "addSprite", null, s));
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
        gameView.propertyChange(new PropertyChangeEvent(this, "changeHealth", null,
            (int) highscoreListener.getHealth()));
        gameView.propertyChange(
            new PropertyChangeEvent(this, "changeLife", null, (int) highscoreListener.getLives()));
      } catch (Exception e) {
        reportError(e);
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
    gameObject.addListener("sprite", s);
    gameView.propertyChange(new PropertyChangeEvent(this, "addSprite", null, s));
  }

  /**
   * Displays the game menu
   */
  public void displayMenu() {
    gameView.displayMenu();
  }

  private void reportError(Exception e) {
    if (animation != null) {
      stop();
    }
    new ExceptionView().displayError(e);
  }

  private void stop() {
    animation.stop();
  }
}
