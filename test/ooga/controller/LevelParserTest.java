package ooga.controller;

import ooga.model.GameWorld;
import ooga.model.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LevelParserTest {

  LevelParser gameWorldFactory;

  @BeforeEach
  public void init() throws IOException, SAXException, ParserConfigurationException {
    gameWorldFactory = new LevelParser(new File("data/testgame/level.xml"));
  }

  @Test
  public void test()
      throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException {

    GameWorld gw = gameWorldFactory.createGameWorld(null, new Vector(1000, 1000), 10);
    assertEquals(2, gw.getAllDestroyables().size());
  }
}
