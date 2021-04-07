package ooga.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import ooga.model.GameWorld;
import ooga.model.MethodBundle;
import ooga.model.Vector;
import ooga.view.game.GameView;
import org.xml.sax.SAXException;

public class Controller {

  private GameWorldFactory gameWorldFactory;
  private CollisionsParser collisionsParser;
  private GameWorld gameWorld;
  private Vector frameSize;
  private double frameRate;
  private GameView gameView;

  public Controller(Vector frameSize, double frameRate) {
    gameWorldFactory = new GameWorldFactory();
    collisionsParser = new CollisionsParser();
    this.frameSize =  frameSize;
    this.frameRate = frameRate;
  }

  public void startGame(GameView gameView) {

    String gameName = gameView.getGameName();
    File collisionsFile = new File("data/" + gameName + "/collisions.xml");
    File levelFile = new File("data/" + gameName + "/level.xml");

    try {
      Map<String, Map<String, List<MethodBundle>>> collisions = collisionsParser.parseCollisions(collisionsFile);
      gameWorld = gameWorldFactory.createGameWorld(levelFile, collisions, frameSize, frameRate);
    } catch (Exception ignored){}
  }

  private void addListeners() {
    //TODO: add listeners to view
  }
}
