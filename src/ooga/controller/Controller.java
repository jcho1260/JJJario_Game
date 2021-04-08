package ooga.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import ooga.model.GameWorld;
import ooga.model.gameobjects.GameObject;
import ooga.model.util.MethodBundle;
import ooga.model.util.Vector;
import ooga.view.game.GameView;
import ooga.view.game.Sprite;

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
    gameView.initializeLevel(frameSize.getX(), frameSize.getY());
    String gameName = gameView.getGameName();
    File collisionsFile = new File("data/" + gameName + "/collisions.xml");
    File levelFile = new File("data/" + gameName + "/level.xml");

    try {
      Map<String, Map<String, List<MethodBundle>>> collisions = collisionsParser.parseCollisions(collisionsFile);
      gameWorld = gameWorldFactory.createGameWorld(levelFile, collisions, frameSize, frameRate);
    } catch (Exception e) {
      e.printStackTrace();
    }

    addSprites(gameWorld);
    gameView.startLevel();

    KeyFrame frame = new KeyFrame(Duration.seconds(1/frameRate), e -> step(1/frameRate));
    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames().add(frame);
    animation.play();
  }

  public KeyListener getKeyListener() {
    return keyListener;
  }

  private void step(double d) {
    try {
      gameWorld.stepFrame(keyListener.getCurrentKey());
    } catch (Exception ignored){
      System.out.println(ignored);
    }
  }

  private void addSprites(GameWorld gameWorld) {
    List<GameObject> gameObjects = gameWorld.getAllGameObjects();
    for (GameObject gameObject : gameObjects) {
      String name = gameObject.getEntityType().get(gameObject.getEntityType().size()-1);
      Sprite s = new Sprite(name, gameObject.getSize().getX(), gameObject.getSize().getY(), gameObject.getPosition().getX(), gameObject.getPosition().getY());
      gameObject.addListener(s);
      gameView.addSprite(s);
    }
  }
}
