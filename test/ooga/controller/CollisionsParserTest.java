package ooga.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import ooga.model.util.MethodBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;


public class CollisionsParserTest {

  CollisionsParser collisionsParser;

  @BeforeEach
  public void init() {
    collisionsParser = new CollisionsParser();
  }

  @Test
  public void testCollisions() throws IOException, SAXException, ParserConfigurationException {
    Map<String, Map<String, List<MethodBundle>>> c = collisionsParser
        .parseCollisions(new File("data/testgame/collisions.xml"));
    double paramOne = c.get("Player").get("Enemy").get(1).getParameters()[0];
    double paramTwo = c.get("Player").get("Enemy").get(1).getParameters()[1];

    assertEquals(8, paramOne);
    assertEquals(7, paramTwo);
  }

  @Test
  public void testMalformedCollisionsThrows() {
    assertThrows(Exception.class,
        () -> collisionsParser.parseCollisions(new File("data/testgame/badCollisions.xml")));
  }

  @Test
  public void testNoCollisionsFileThrows() {
    assertThrows(FileNotFoundException.class,
        () -> collisionsParser.parseCollisions(new File("this/is/not/a/valid/file")));
  }
}
