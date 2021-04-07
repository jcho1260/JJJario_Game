package ooga.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import ooga.model.GameWorld;
import ooga.model.MethodBundle;
import ooga.model.Vector;
import org.xml.sax.SAXException;

public class Controller {

  private GameWorldFactory gameWorldFactory;
  private CollisionsParser collisionsParser;
  private GameWorld gameWorld;
  private Vector frameSize;
  private double frameRate;

  public Controller(Vector frameSize, double frameRate) {
    gameWorldFactory = new GameWorldFactory();
    collisionsParser = new CollisionsParser();
    this.frameSize =  frameSize;
    this.frameRate = frameRate;
  }

  public void startGame()
      throws IOException, SAXException, ParserConfigurationException, ClassNotFoundException {

    Map<String, Map<String, List<MethodBundle>>> collisions = collisionsParser.parseCollisions(new File(""));
    gameWorldFactory.createGameWorld(new File(""), collisions, frameSize, frameRate);
  }



  //TODO: add listeners to view


}
