package ooga.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import ooga.model.GameWorld;
import ooga.model.util.MethodBundle;
import ooga.model.util.Vector;
import ooga.view.game.GameView;

import java.io.File;
import java.util.List;
import java.util.Map;

public class Controller {

  private GameWorldFactory gameWorldFactory;
  private CollisionsParser collisionsParser;
  private GameWorld gameWorld;
  private Vector frameSize;
  private double frameRate;
  private GameView gameView;
  KeyListener keyListener;

  public Controller(Vector frameSize, double frameRate) {
    gameWorldFactory = new GameWorldFactory();
    collisionsParser = new CollisionsParser();
    this.frameSize =  frameSize;
    this.frameRate = frameRate;
    keyListener = new KeyListener();
  }

  public void startGame(GameView gameView) {
    this.gameView = gameView;
  }

  public void startLevel(String levelName) {
    String gameName = gameView.getGameName();
    File collisionsFile = new File("data/" + gameName + "/collisions.xml");
    File levelFile = new File("data/" + gameName + "/level.xml");

    try {
      Map<String, Map<String, List<MethodBundle>>> collisions = collisionsParser.parseCollisions(collisionsFile);
      gameWorld = gameWorldFactory.createGameWorld(levelFile, collisions, frameSize, frameRate);
    } catch (Exception ignored){}

    KeyFrame frame = new KeyFrame(Duration.seconds(1/frameRate), e -> step());
    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();
  }

  public KeyListener getKeyListener() {
    return keyListener;
  }

  private void step() {
    try {
      gameWorld.stepFrame(keyListener.getCurrentKey());
    } catch (Exception ignored){}
  }

  private void addListeners() {
    //TODO: add listeners to view
  }
}
