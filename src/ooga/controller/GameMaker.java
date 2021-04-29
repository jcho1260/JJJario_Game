package ooga.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;
import javax.xml.parsers.ParserConfigurationException;
import ooga.model.GameWorld;
import ooga.model.gameobjects.Destroyable;
import ooga.model.gameobjects.GameObject;
import ooga.model.gameobjects.Player;
import ooga.model.util.MethodBundle;
import ooga.model.util.Vector;
import org.xml.sax.SAXException;

/**
 * The GameMaker handle creating, loading, and saving user defined games. It is dependent on import ooga.model.GameWorld,
 * ooga.model.gameobjects.Destroyable, ooga.model.gameobjects.GameObject, ooga.model.gameobjects.Player, ooga.model.util.MethodBundle
 * and ooga.model.util.Vector;
 *
 * @author Noah Citron
 */
public class GameMaker {

  private final List<GameObjectMaker> gameObjectMakerList;
  private final String game;
  private Player player;
  private int numObjects = 1;

  /**
   * Starts the GameMaker
   *
   * @param game the type of game being made
   */
  public GameMaker(String game) {
    this.game = game;
    gameObjectMakerList = new ArrayList<>();
  }

  /**
   * Gets the current number of GameObjects added to the GameMaker
   *
   * @return  current number of GameObjects
   */
  public int getNumObjects() {
    return numObjects;
  }

  /**
   * Gets the name of the game currently being made
   *
   * @return name of current game
   */
  public String getGame() {
    return game;
  }

  /**
   * Gets the tag list of a particular GameObject. Assumes that the GameMaker has been started
   *
   * @param name  name of the GameObject
   * @return      the tag list of the GameObject
   * @throws IOException
   * @throws SAXException
   * @throws ParserConfigurationException
   */
  public List<String> getEntityType(String name)
      throws IOException, SAXException, ParserConfigurationException {
    LevelParser lp = new LevelParser(new File("data/" + game + "/Level1.xml"));
    return lp.getTags(name);
  }

  /**
   * Get all the available GameObjects for a given game
   *
   * @return  a list of pairs. The key represents the name of the GameObject, while the value represents its type
   * @return
   * @throws IOException
   * @throws SAXException
   * @throws ParserConfigurationException
   */
  public List<Pair<String, String>> getGameObjects()
      throws IOException, SAXException, ParserConfigurationException {
    LevelParser lp = new LevelParser(new File("data/" + game + "/Level1.xml"));
    return lp.getAllGameObjects();
  }

  /**
   * Creates a new player
   *
   * @param pos   position of the player
   * @param size  size of the player
   * @throws IOException
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws ClassNotFoundException
   */
  public void createPlayer(Vector pos, Vector size)
      throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException {

    LevelParser lp = new LevelParser(new File("data/" + game + "/Level1.xml"));
    player = lp.createPlayerFromCoords(pos, size);
  }

  /**
   * Adds a GameObjectMaker to the GameMaker
   *
   * @param gameObjectMaker GameObjectMaker to add
   */
  public void addGameObjectMaker(GameObjectMaker gameObjectMaker) {
    numObjects++;
    gameObjectMakerList.add(gameObjectMaker);
  }

  /**
   * Removes the most recently added GameObjectMaker. Assumes that the list is not empty
   */
  public void removeGameObjectMaker() {
    gameObjectMakerList.remove(gameObjectMakerList.size() - 1);
  }

  /**
   * Saves the GameMaker state to a game
   *
   * @param name        name of the game
   * @param gameWorld   gameWorld to save
   * @throws IOException
   */
  public void saveGame(String name, GameWorld gameWorld) throws IOException {
    FileOutputStream f = new FileOutputStream("data/UserDefined/" + game + "/" + name + ".game");
    ObjectOutput s = new ObjectOutputStream(f);
    s.writeObject(gameWorld);
  }

  /**
   * Loads a previously defined game
   *
   * @param game  the name of the game
   * @param name  the name of the level
   * @return      the GameWorld to play
   * @throws IOException
   * @throws ClassNotFoundException
   */
  public GameWorld loadGame(String game, String name) throws IOException, ClassNotFoundException {

    String path = "data/UserDefined/" + game + "/" + name + ".game";

    FileInputStream in = new FileInputStream(path);
    ObjectInputStream s = new ObjectInputStream(in);
    return (GameWorld) s.readObject();
  }

  /**
   * Creates a GameWorld from the GameMaker state
   *
   * @param gameName  the name of the game
   * @param frameSize the size of the frame
   * @param frameRate the framerate of the game
   * @param minScreen the starting position of the game
   * @param maxScreen the ending position of the game
   * @return          the GameWorld defined by the GameMaker
   * @throws IOException
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws GameObjectMakerException
   */
  public GameWorld makeGameWorld(String gameName, Vector frameSize, double frameRate,
      Vector minScreen, Vector maxScreen)
      throws IOException, SAXException, ParserConfigurationException, GameObjectMakerException {

    CollisionsParser collisionsParser = new CollisionsParser();
    Map<String, Map<String, List<MethodBundle>>> collisions = collisionsParser
        .parseCollisions(new File("data/" + gameName + "/collisions.xml"));

    List<GameObject> gameObjects = new ArrayList<>();
    List<GameObject> actors = new ArrayList<>();

    for (GameObjectMaker gameObjectMaker : gameObjectMakerList) {
      GameObject gameObject = gameObjectMaker.createGameObject();

      if (gameObject instanceof Destroyable) {
        actors.add(gameObject);
      }
      gameObjects.add(gameObject);
    }

    if (player == null) {
      throw new GameObjectMakerException("Must have a valid Player");
    }

    LevelParser levelParser = new LevelParser(new File("data/" + gameName + "/Level1.xml"));
    return new GameWorld(player, collisions, gameObjects, actors, frameSize, 3,
        levelParser.getGlobalGravity(), frameRate, minScreen, maxScreen);
  }
}
