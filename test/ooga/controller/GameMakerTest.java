package ooga.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

  private GameMaker gm;

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
    gm.saveGame("testGame", gw);
    gw = gm.loadGame("JJJario", "testGame");

    assertEquals(10, gw.getAllGameObjects().get(0).getPosition().getX());
  }

  @Test
  public void testGetTags() throws ParserConfigurationException, SAXException, IOException {
    List<String> tags = gm.getEntityType("Mario");
    assertEquals("GameObject", tags.get(0));
  }

  @Test
  public void testGetNumObjects() {
    assertEquals(1, gm.getNumObjects());
  }

  @Test
  public void testGetGame() {
    assertEquals("JJJario", gm.getGame());
  }

  @Test
  public void testGameMakerNoPlayerThrows() {

    List<String> tags = new ArrayList<>();
    tags.add("test");
    tags.add("GameObject");

    Object[] attributes = {tags, new Vector(10, 50), 1, new Vector(10, 10), true};
    GameObjectMaker gom = new GameObjectMaker("ooga.model.gameobjects.GameObject", attributes);

    gm.addGameObjectMaker(gom);
    assertThrows(GameObjectMakerException.class, () -> gm.makeGameWorld("JJJario", new Vector(100, 100), 60, new Vector(0, 0), new Vector(100, 40)));
  }
}
