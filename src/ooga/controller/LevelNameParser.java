package ooga.controller;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * Parses LevelNames.xml files which define the number and names of the levels in a game
 *
 * @author Noah Citron
 */
public class LevelNameParser {

  Document doc;

  /**
   * Creates a new LevelName parser. Assumes that the given file exists
   *
   * @param f the LevelNames.xml file
   * @throws ParserConfigurationException
   * @throws IOException
   * @throws SAXException
   */
  public LevelNameParser(File f) throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    doc = db.parse(f);
  }

  /**
   * Gets the number of levels for a given game
   *
   * @return  number of levels
   */
  public int numLevels() {
    return doc.getElementsByTagName("Level").getLength();
  }

  /**
   * Gets the name of a level
   *
   * @param n the level number
   * @return  the level name
   */
  public String getLevelName(int n) {
    return doc.getElementsByTagName("Level").item(n).getTextContent();
  }

}
