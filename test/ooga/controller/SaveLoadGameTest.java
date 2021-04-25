package ooga.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import ooga.model.GameWorld;
import ooga.model.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

public class SaveLoadGameTest {

  GameSaver gameSaver;
  GameWorld gameWorld;

  @BeforeEach
  public void init()
      throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException {
    gameSaver = new GameSaver();
    LevelParser levelParser = new LevelParser(new File("data/testgame/Level1.xml"));
    gameWorld = levelParser.createGameWorld(null, new Vector(1000, 1000), 10);
  }

  @Test
  public void testSaveLoad() throws IOException, ClassNotFoundException {
    gameSaver.saveGame("testGame", "level", "testSave", gameWorld);
    GameWorld loadedWorld = gameSaver.loadGame("testGame", "level", "testSave.game");

    int originalNumObjects = gameWorld.getAllGameObjects().size();
    int loadedNumObjects = loadedWorld.getAllGameObjects().size();

    assertEquals(originalNumObjects, loadedNumObjects);
  }
}
