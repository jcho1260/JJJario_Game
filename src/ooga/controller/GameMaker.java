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

  public GameWorld makeGameWorld(String gameName, List<GameObjectMaker> gameObjectMakerList, Vector frameSize, double frameRate, Vector minScreen, Vector maxScreen)
      throws IOException, SAXException, ParserConfigurationException, GameObjectMakerException {

    CollisionsParser collisionsParser = new CollisionsParser();
    Map<String, Map<String, List<MethodBundle>>> collisions = collisionsParser.parseCollisions(new File("data/" + gameName + "/collisions.xml"));

    List<GameObject> gameObjects = new ArrayList<>();
    List<GameObject> actors = new ArrayList<>();
    Player player = null;

    for (GameObjectMaker gameObjectMaker : gameObjectMakerList) {
      GameObject gameObject = gameObjectMaker.createGameObject();
      if (gameObject instanceof Player) {
        player = (Player) gameObject;
      } else if (gameObject instanceof Destroyable) {
        actors.add(gameObject);
        gameObjects.add(gameObject);
      } else {
        gameObjects.add(gameObject);
      }
    }

    LevelParser levelParser = new LevelParser(new File("data/" + gameName + "/Level1.xml"));
    return new GameWorld(player, collisions, gameObjects, actors, frameSize, 3, levelParser.getGlobalGravity(), frameRate, minScreen, maxScreen);
  }
}
