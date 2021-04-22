package ooga.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

  //public List<String> getGameObjects =

  public void createPlayer(Vector pos)
      throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException {

    LevelParser lp = new LevelParser(new File("data/" + game + "Level1.xml"));
    player = lp.createPlayerFromCoords(pos);
  }

  public void addGameObjectMaker(GameObjectMaker gameObjectMaker) {
    gameObjectMakerList.add(gameObjectMaker);
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
