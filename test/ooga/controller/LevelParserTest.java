package ooga.controller;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

public class LevelParserTest {

  LevelParser levelParser;

  @BeforeEach
  public void init() {
    levelParser = new LevelParser();
  }

  @Test
  public void test() throws IOException, SAXException, ParserConfigurationException {
    levelParser.createLevel(new File("data/testgame/level.xml"));
  }
}
