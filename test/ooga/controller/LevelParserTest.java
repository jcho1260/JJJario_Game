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

public class LevelParserTest {

  LevelParser gameWorldFactory;

  @BeforeEach
  public void init() throws IOException, SAXException, ParserConfigurationException {
    gameWorldFactory = new LevelParser(new File("data/testgame/Level1.xml"));
  }

  @Test
  public void test()
      throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException {

    GameWorld gw = gameWorldFactory.createGameWorld(null, 10);
    assertEquals(3, gw.getAllDestroyables().size());
  }
}
