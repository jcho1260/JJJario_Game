package ooga.model;

import java.beans.PropertyChangeListener;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import ooga.JjjanException;
import ooga.Observable;
import ooga.model.gameobjectcomposites.WorldCollisionHandling;
import ooga.model.gameobjects.Destroyable;
import ooga.model.gameobjects.GameObject;
import ooga.model.gameobjects.MovingDestroyable;
import ooga.model.gameobjects.Player;
import ooga.model.util.Action;
import ooga.model.util.MethodBundle;
import ooga.model.util.Vector;


/**
 *
 */
public class GameWorld extends Observable implements Serializable {

  private static final double playerXLoc = 0.5;
  private static final double playerYLoc = 2.0/3.0;
  private static final int correctionCycles = 10;
  private static final String TAG_SPECIFICATIONS =  "ooga.model.model_resources.games.EntityTagSpecifications";
  private static final String ALL_ACTIVE_KEY = "ALWAYSACTIVE";

  private List<GameObject> allGameObjects;
  private List<GameObject> allActiveGameObjects;
  private List<GameObject> allDestroyables;
  private List<GameObject> allActiveDestroyables;
  private List<GameObject> allAlwaysActive;
  private List<MovingDestroyable> runtimeCreations;
  private WorldCollisionHandling worldCollisionHandling;
  private double score;
  private int currentFrameCount;
  private Player player;
  private Vector windowSize;
  private Vector[] frameCoords;
  private Vector screenLimitsMin;
  private Vector screenLimitsMax;

  private final List<String> alwaysActiveTags;
  private final double gravity;
  private final double stepTime;

  /**
   * Default constructor
   */
  public GameWorld(Player gamePlayer, Map<String, Map<String, List<MethodBundle>>> collisionMethods,
      List<GameObject> gameObjects, List<GameObject> actors, Vector frameSize, int startingLives,
      double levelGravity, double frameRate, Vector minScreenLimit, Vector maxScreenLimit) {
    PropertyChangeListener playerListener = evt -> notifyListenerKey("highscore", evt.getPropertyName(), null,
        evt.getNewValue());
    player = gamePlayer;
    player.addListener("gameworld", playerListener);
    windowSize = frameSize;
    allGameObjects = gameObjects;
    gravity = levelGravity;
    stepTime = 1.0/frameRate;
    score = 0;
    currentFrameCount = 0;
    screenLimitsMin = minScreenLimit;
    screenLimitsMax = maxScreenLimit;
    alwaysActiveTags = getAlwaysActiveTags();
    frameCoords = new Vector[4];
    updateFrameCoordinates(player.getPosition(), player.getSize());
    allAlwaysActive = new ArrayList<>();
    findAlwaysActive();
    allDestroyables = actors;
    worldCollisionHandling = new WorldCollisionHandling(collisionMethods, gameObjects, actors, player);
    windowSize = frameSize;
    runtimeCreations = new ArrayList<>();
  }

  private List<String> getAlwaysActiveTags() {
    ResourceBundle tagSpecifications = ResourceBundle.getBundle(TAG_SPECIFICATIONS);
    String allTags = tagSpecifications.getString(ALL_ACTIVE_KEY);
    return Arrays.asList(allTags.split(" ").clone());
  }

  public void stepFrame(Action pressEffect)
      throws NoSuchMethodException, JjjanException, InvocationTargetException, IllegalAccessException {
    allActiveGameObjects = findActiveObjects(allGameObjects);
    allActiveDestroyables = findActiveObjects(allDestroyables);

    player.userStep(pressEffect, stepTime, gravity, currentFrameCount);  // use setPredicted
    allGameObjectStep(stepTime);  // use setPredicted
    collisionsDetectAndExecute();
    updatePositions();
    playerOffScreen();
    // using actual position (after setPosition() was called) --> do later, call internally
    updateFrameCoordinates(player.getPosition(), player.getSize());
    appendRuntimeCreations();
    updateAllActiveInfo();
    sendViewCoords();
    currentFrameCount++;
  }

  private void collisionsDetectAndExecute()
      throws NoSuchMethodException, JjjanException, InvocationTargetException, IllegalAccessException {
    worldCollisionHandling.detectAllCollisions(); // use setPredicted
    List<Integer> forDeletion = worldCollisionHandling.executeAllCollisions();  // use setPredicted
    removeDeadActors(forDeletion);
    correctCollisionIntersections();
  }

  private void correctCollisionIntersections() throws NoSuchMethodException, JjjanException {
    for (int i = 0; i < correctionCycles; i++) {
      if (worldCollisionHandling.detectAllCollisions()) {
        worldCollisionHandling.fixIntersection(allAlwaysActive);
        worldCollisionHandling.clear();
      } else {
        worldCollisionHandling.clear();
        break;
      }
    }
  }

  private void updatePositions() {
    for (GameObject go : allActiveDestroyables) {
      go.updatePosition();
    }
    player.updatePosition();
  }

  private void findAlwaysActive() {
    for (GameObject go : allGameObjects) {
      for(String tag : alwaysActiveTags) {
        if (go.getEntityType().contains(tag)) {
          allAlwaysActive.add(go);
        }
      }
    }
  }

  private void allGameObjectStep(double elapsedTime) {
    for (GameObject o : allGameObjects) {
      o.step(elapsedTime, gravity);
    }
  }

  private void playerOffScreen() {
    if (player.getPosition().getY()+player.getSize().getY() >= screenLimitsMax.getY() ||
        player.getPosition().getY() <= screenLimitsMin.getY()) {
      player.kill();
    }
  }

  public void queueNewMovingDestroyable(List<MovingDestroyable> newMovingDestroyables) {
    if (newMovingDestroyables.size() != 0 && newMovingDestroyables.get(0) != null){
      runtimeCreations.addAll(newMovingDestroyables);
    }
  }

  private void appendRuntimeCreations() {
    allGameObjects.addAll(runtimeCreations);
    allDestroyables.addAll(runtimeCreations);
    runtimeCreations.clear();
  }

  private void updateAllActiveInfo() {
    allActiveGameObjects = findActiveObjects(allGameObjects);
    allActiveDestroyables = findActiveObjects(allDestroyables);
    worldCollisionHandling.updateActiveGameObjects(allActiveGameObjects, allActiveDestroyables);
  }

  private List<GameObject> findActiveObjects(List<GameObject> allObjects) {
    Vector frameTopL = frameCoords[0];
    Vector frameBotR = frameCoords[3];
    List<GameObject> ret = new ArrayList<>();
    for (GameObject o : allObjects) {
      boolean isActive = checkActiveState(o, frameTopL, frameBotR);
      o.setActive(isActive);
      if(isActive) {ret.add(o);}
    }
    player.setActive(player.isAlive());
    return ret;
  }

  private boolean checkActiveState(GameObject o, Vector frameTopL, Vector frameBotR) {
    Vector oTopL = o.getPosition();
    Vector oTopR = o.getPosition().add(new Vector(o.getSize().getX(), 0));
    Vector oBotL = o.getPosition().add(new Vector(0,o.getSize().getY()));
    Vector oBotR = o.getPosition().add(new Vector(o.getSize().getX(),o.getSize().getY()));
    return oTopL.insideBox(frameTopL,frameBotR) || oTopR.insideBox(frameTopL,frameBotR) ||
        oBotL.insideBox(frameTopL, frameBotR) || oBotR.insideBox(frameTopL,frameBotR) || allAlwaysActive
        .contains(o);
  }

  private void removeDeadActors(List<Integer> deadActors) {
    getScoreDead(deadActors);

    allGameObjects = removeIndicesFromList(allGameObjects, deadActors);
    allDestroyables = removeIndicesFromList(allDestroyables, deadActors);
    allAlwaysActive = removeIndicesFromList(allAlwaysActive, deadActors);

    allActiveGameObjects = findActiveObjects(allGameObjects);
    allActiveDestroyables = findActiveObjects(allDestroyables);
    worldCollisionHandling.updateActiveGameObjects(allActiveGameObjects, allActiveDestroyables);
  }

  private void getScoreDead(List<Integer> deadActors) {
    for(GameObject d : allDestroyables) {
      if (deadActors.contains(d.getId())) {
        incrementScore(((Destroyable)d).getScore());
      }
    }
  }

  private List<GameObject> removeIndicesFromList(List<GameObject> objects, List<Integer> deadActors) {
    for(int j = objects.size() - 1; j >= 0; j--) {
      if (deadActors.contains(objects.get(j).getId())) {
        objects.remove(j);
      }
    }
    return objects;
  }

  private void updateFrameCoordinates(Vector playerCoord, Vector playerSize) {
    double defaultXLeft = screenLimitsMin.getX();
    double defaultXRight = screenLimitsMax.getX();
    double defaultYBot = screenLimitsMax.getY();
    double defaultYTop = screenLimitsMin.getY();
    Vector playerCenter = new Vector(playerCoord.getX()+ 0.5*playerSize.getX(), playerCoord.getY() + 0.5*playerSize.getY());
    double topY = playerCenter.getY() - (playerYLoc * windowSize.getY());
    double botY = playerCoord.getY() + ((1-playerYLoc) * windowSize.getY());
    if(defaultYBot > windowSize.getY()) {botY = defaultYBot;}
    double leftX = playerCenter.getX() - (playerXLoc * windowSize.getX());
    double rightX = playerCenter.getX() + (playerXLoc * windowSize.getX());
    if (topY <= defaultYTop) {
      topY = defaultYTop;
      botY = defaultYTop + windowSize.getY();
    }
    if (botY >= defaultYBot) {
      botY = defaultYBot;
      topY = defaultYBot - windowSize.getY();
    }
    if (leftX <= defaultXLeft) {
      leftX = defaultXLeft;
      rightX = defaultXLeft + windowSize.getX();
    }
    if (rightX >= defaultXRight) {
      leftX = defaultXRight - windowSize.getX();
      rightX = defaultXRight;
    }
    frameCoords[0] = new Vector(leftX, topY);
    frameCoords[1] = new Vector(rightX, topY);
    frameCoords[2] = new Vector(leftX, botY);
    frameCoords[3] = new Vector(rightX, botY);
  }

  private void sendViewCoords() {
    for (GameObject o : allActiveGameObjects) {
      o.sendToView(frameCoords[0]);
    }
    player.sendToView(frameCoords[0]);
  }

  /**
   * returns all destroyable game objects
   */
  public List<GameObject> getAllDestroyables() {
    List<GameObject> ret = new ArrayList<>(allDestroyables);
    ret.add(player);
    return ret;
  }

  /**
   * returns all gameobjects in the game
   */
  public List<GameObject> getAllGameObjects() {
    List<GameObject> ret = new ArrayList<>(allGameObjects);
    ret.add(player);
    return ret;
  }

  public double getGravity() {
    return gravity;
  }

  public boolean isGameOver() {
    return !player.isAlive();
  }

  public boolean didPlayerWin() {
    return player.getWinStatus();
  }

  /**
   * Adds to score by given amount. Notifies listeners of change.
   *
   * @param increment
   */
  private void incrementScore(double increment) {
    double prevScore = score;
    score += increment;
    notifyListenerKey("highscore", "score", prevScore, score);
  }

  public void addPlayerListener() {
    PropertyChangeListener playerListener = evt -> notifyListenerKey("highscore", evt.getPropertyName(), null,
        evt.getNewValue());
    player.addListener("gameworld", playerListener);
  }
}