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

public class GameWorldFactoryTest {

  GameWorldFactory gameWorldFactory;

  @BeforeEach
  public void init() {
    gameWorldFactory = new GameWorldFactory();
  }

  @Test
  public void test()
      throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException {

    GameWorld gw = gameWorldFactory.createGameWorld(new File("data/testgame/level.xml"), null, new Vector(1000, 1000), 10);
    assertEquals(2, gw.getAllDestroyables().size());
  }
}
