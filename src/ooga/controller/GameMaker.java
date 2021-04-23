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

public class GameMaker {

  private List<GameObjectMaker> gameObjectMakerList;
  private String game;
  private Player player;

  public GameMaker(String game) {
    this.game = game;
    gameObjectMakerList = new ArrayList<>();
  }

  public String getGame() {
    return game;
  }

  public List<String> getEntityType(String name)
      throws IOException, SAXException, ParserConfigurationException {
    LevelParser lp = new LevelParser(new File("data/" + game + "Level1.xml"));
    return lp.getTags(name);
  }

  public List<Pair<String, String>> getGameObjects()
      throws IOException, SAXException, ParserConfigurationException {
    LevelParser lp = new LevelParser(new File("data/" + game + "Level1.xml"));
    return lp.getAllGameObjects();
  }

  public void createPlayer(Vector pos, Vector size)
      throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException {

    LevelParser lp = new LevelParser(new File("data/" + game + "/Level1.xml"));
    player = lp.createPlayerFromCoords(pos, size);
  }

  public void addGameObjectMaker(GameObjectMaker gameObjectMaker) {
    gameObjectMakerList.add(gameObjectMaker);
  }

  public void removeGameObjectMaker() {
    gameObjectMakerList.remove(gameObjectMakerList.size()-1);
  }

  public void saveGame(String name, GameWorld gameWorld) {
    try {
      FileOutputStream f = new FileOutputStream("UserDefined/" + game + "/" + name + ".game");
      ObjectOutput s = new ObjectOutputStream(f);
      s.writeObject(gameWorld);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public GameWorld loadGame(String game, String name) {

    String path = "UserDefined/" + game + "/" + name + ".game";

    try {
      FileInputStream in = new FileInputStream(path);
      ObjectInputStream s = new ObjectInputStream(in);
      return (GameWorld) s.readObject();
    } catch(IOException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

  public GameWorld makeGameWorld(String gameName, Vector frameSize, double frameRate, Vector minScreen, Vector maxScreen)
      throws IOException, SAXException, ParserConfigurationException, GameObjectMakerException {

    CollisionsParser collisionsParser = new CollisionsParser();
    Map<String, Map<String, List<MethodBundle>>> collisions = collisionsParser.parseCollisions(new File("data/" + gameName + "/collisions.xml"));

    List<GameObject> gameObjects = new ArrayList<>();
    List<GameObject> actors = new ArrayList<>();;

    for (GameObjectMaker gameObjectMaker : gameObjectMakerList) {
      GameObject gameObject = gameObjectMaker.createGameObject();

      if (gameObject instanceof Destroyable) {
        actors.add(gameObject);
      }
      gameObjects.add(gameObject);
    }

    LevelParser levelParser = new LevelParser(new File("data/" + gameName + "/Level1.xml"));
    return new GameWorld(player, collisions, gameObjects, actors, frameSize, 3, levelParser.getGlobalGravity(), frameRate, minScreen, maxScreen);
  }
}
