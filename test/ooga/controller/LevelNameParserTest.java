package ooga.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;


public class LevelNameParserTest {

  private LevelNameParser levelNameParser;

  @BeforeEach
  public void init() throws IOException, SAXException, ParserConfigurationException {
    levelNameParser = new LevelNameParser(new File("data/JJJario/LevelNames.xml"));
  }

  @Test
  public void testGetNumLevels() {
    int numLevels = levelNameParser.numLevels();
    assertEquals(2, numLevels);
  }

  @Test
  public void testGetLevelName() {
    String name = levelNameParser.getLevelName(0);
    assertEquals("Level1", name);
  }

  @Test
  public void testNoFileThrows() {
    assertThrows(FileNotFoundException.class, () -> new LevelNameParser(new File("invalid/path")));
  }
}
