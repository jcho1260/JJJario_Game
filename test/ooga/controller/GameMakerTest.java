package ooga.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import ooga.model.gameobjects.GameObject;
import ooga.model.gameobjects.Player;
import ooga.model.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    Object[] attributes = { tags, new Vector(10, 50), 0, new Vector(10, 10), true };
    GameObjectMaker gom = new GameObjectMaker("ooga.model.gameobjects.GameObject", attributes);
    GameObject go = gom.createGameObject();

    assertEquals(10, go.getSize().getY());
  }

  @Test
  public void testGameMaker()
      throws GameObjectMakerException, ParserConfigurationException, SAXException, IOException {
    List<String> tags = new ArrayList<>();
    tags.add("test");
    tags.add("GameObject");

    Object[] attributes1 = { tags, new Vector(10, 50), 0, new Vector(10, 10), true };
    GameObjectMaker gom1 = new GameObjectMaker("ooga.model.gameobjects.GameObject", attributes1);

    tags = new ArrayList<>();
    tags.add("Mario");
    tags.add("Player");

    Object[] attributes2 = { tags, new Vector(10, 30), 1, new Vector(10, 10), 1, 1, 2, new Vector(10, 10), 5, new Vector(0, 0), 1, 1, true, 1 };
    GameObjectMaker gom2 = new GameObjectMaker("ooga.model.gameobjects.Player", attributes2);

    gm.addGameObjectMaker(gom1);
    gm.addGameObjectMaker(gom2);

    gm.makeGameWorld("JJJario", new Vector(100,100), 60, new Vector(0, 0), new Vector(100, 40));
  }
}
