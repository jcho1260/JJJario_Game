package ooga.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import ooga.JjjanException;
import ooga.model.GameWorld;
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
    gameWorld = levelParser.createGameWorld(null, 10);
  }

  @Test
  public void testSaveLoad() throws IOException, ClassNotFoundException, JjjanException {
    gameSaver.saveGame("testGame", "level", "testSave", gameWorld);
    GameWorld loadedWorld = gameSaver.loadGame("testGame", "level", "testSave.game");

    int originalNumObjects = gameWorld.getAllGameObjects().size();
    int loadedNumObjects = loadedWorld.getAllGameObjects().size();

    assertEquals(originalNumObjects, loadedNumObjects);
  }

  @Test
  public void testNullGameWorldThrows() {
    assertThrows(JjjanException.class, () -> gameSaver.saveGame("testGame", "level", "testSave", null));
  }

  @Test
  public void testLoadNonExistingSaveThrows() {
    assertThrows(FileNotFoundException.class, () -> gameSaver.loadGame("testGame", "level", "notValid.game"));
  }
}
