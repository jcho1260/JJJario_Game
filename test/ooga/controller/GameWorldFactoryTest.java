package ooga.controller;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import ooga.model.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

public class GameWorldFactoryTest {

  GameWorldFactory levelParser;

  @BeforeEach
  public void init() {
    levelParser = new GameWorldFactory();
  }

  @Test
  public void test()
      throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException {
    levelParser.createGameWorld(new File("data/testgame/level.xml"), null, new Vector(1000, 1000), 10);
  }
}
