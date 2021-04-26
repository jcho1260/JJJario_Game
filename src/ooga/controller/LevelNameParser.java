package ooga.controller;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import ooga.JjjanException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class LevelNameParser {

  Document doc;

  public LevelNameParser(File f) throws ParserConfigurationException, IOException, SAXException {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    DocumentBuilder db = dbf.newDocumentBuilder();
    doc = db.parse(f);
  }

  public int numLevels() {
    return doc.getElementsByTagName("Level").getLength();
  }

  public String getLevelName(int n) {
    return doc.getElementsByTagName("Level").item(n).getTextContent();
  }

}
