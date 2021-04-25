package ooga.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import ooga.model.GameWorld;
import ooga.model.gameobjects.GameObject;
import ooga.model.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

public class GameMakerTest {

  GameMaker gm;

  @BeforeEach
  public void init() {
    gm = new GameMaker("JJJario");
  }

  @Test
  public void testGameObjectMaker() throws GameObjectMakerException {

    List<String> tags = new ArrayList<>();
    tags.add("test");
    tags.add("GameObject");

    Object[] attributes = {tags, new Vector(10, 50), 0, new Vector(10, 10), true};
    GameObjectMaker gom = new GameObjectMaker("ooga.model.gameobjects.GameObject", attributes);
    GameObject go = gom.createGameObject();

    assertEquals(10, go.getSize().getY());
  }

  @Test
  public void testGameMaker()
      throws GameObjectMakerException, ParserConfigurationException, SAXException, IOException, ClassNotFoundException {
    List<String> tags = new ArrayList<>();
    tags.add("test");
    tags.add("GameObject");

    Object[] attributes = {tags, new Vector(10, 50), 1, new Vector(10, 10), true};
    GameObjectMaker gom = new GameObjectMaker("ooga.model.gameobjects.GameObject", attributes);

    gm.createPlayer(new Vector(50, 10), new Vector(80, 80));
    gm.addGameObjectMaker(gom);

    GameWorld gw = gm
        .makeGameWorld("JJJario", new Vector(100, 100), 60, new Vector(0, 0), new Vector(100, 40));

    assertEquals(10, gw.getAllGameObjects().get(0).getPosition().getX());
  }
}
