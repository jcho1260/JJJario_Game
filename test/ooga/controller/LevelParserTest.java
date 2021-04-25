package ooga.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import ooga.model.GameWorld;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

public class LevelParserTest {

  LevelParser gameWorldFactory;

  @BeforeEach
  public void init() throws IOException, SAXException, ParserConfigurationException {
    gameWorldFactory = new LevelParser(new File("data/testgame/Level1.xml"));
  }

  @Test
  public void testParse() throws ClassNotFoundException {

    GameWorld gw = gameWorldFactory.createGameWorld(null, 10);
    assertEquals(3, gw.getAllDestroyables().size());
  }

  @Test
  public void testNoLevelFileThrows() {
    assertThrows(FileNotFoundException.class, () -> new LevelParser(new File("not/valid/path")));
  }

  @Test
  public void testMalformedLevelFileThrows()
      throws IOException, SAXException, ParserConfigurationException {
    gameWorldFactory = new LevelParser(new File("data/testgame/badLevel.xml"));
    assertThrows(Exception.class, () -> gameWorldFactory.createGameWorld(null, 10));
  }
}
