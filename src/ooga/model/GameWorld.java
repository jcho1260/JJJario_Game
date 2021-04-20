package ooga.model;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

  private List<GameObject> allGameObjects;
  private List<GameObject> allActiveGameObjects;
  private List<GameObject> allDestroyables;
  private List<GameObject> allActiveDestroyables;
  private List<GameObject> allBricks;
  private List<MovingDestroyable> runtimeCreations;
  private WorldCollisionHandling worldCollisionHandling;
  private double score;
  private Player player;
  private Vector windowSize;
  private Vector[] frameCoords;
  private Vector screenLimitsMin;
  private Vector screenLimitsMax;
  private Vector playerViewCoord;
  private boolean playerWin;


  private final double gravity;
  private final double stepTime;

  /**
   * Default constructor
   */
  public GameWorld(Player gamePlayer, Map<String, Map<String, List<MethodBundle>>> collisionMethods,
      List<GameObject> gameObjects, List<GameObject> actors, Vector frameSize, int startingLives,
      double levelGravity, double frameRate, Vector minScreenLimit, Vector maxScreenLimit) {
    player = gamePlayer;
    windowSize = frameSize;
    allGameObjects = gameObjects;
    gravity = levelGravity;
    stepTime = 1.0/frameRate;
    score = 0;
    playerWin = false;
    screenLimitsMin = minScreenLimit;
    screenLimitsMax = maxScreenLimit;
    frameCoords = new Vector[4];
    frameCoordinates(player.getPosition(), player.getSize());
    allBricks = new ArrayList<>();
    findBricks();
    allActiveGameObjects = findActiveObjects(allGameObjects);
    allDestroyables = actors;
    allActiveDestroyables = findActiveObjects(allDestroyables);
    worldCollisionHandling = new WorldCollisionHandling(collisionMethods, gameObjects, actors, player);
    windowSize = frameSize;
    double playerViewX = frameSize.getX() * playerXLoc;
    double playerViewY = frameSize.getY() * playerYLoc;
    playerViewCoord = new Vector(playerViewX, playerViewY);
    runtimeCreations = new ArrayList<>();
  }

  public void stepFrame(Action pressEffect)
      throws NoSuchMethodException, JjjanException, InvocationTargetException, IllegalAccessException {
    player.userStep(pressEffect, stepTime, gravity);  // use setPredicted
    allGameObjectStep(stepTime);  // use setPredicted
    worldCollisionHandling.detectAllCollisions(); // use setPredicted
    List<Integer> forDeletion = worldCollisionHandling.executeAllCollisions();  // use setPredicted
    removeDeadActors(forDeletion);

    for (int i = 0; i < 10; i++) {
      if (worldCollisionHandling.detectAllCollisions()) {
        worldCollisionHandling.fixIntersection(allBricks);
        worldCollisionHandling.clear();
      } else {
        worldCollisionHandling.clear();
        break;
      }
    }
    updatePositions();
    playerOffScreen();

    // using actual position (after setPosition() was called) --> do later, call internally
    frameCoordinates(player.getPosition(), player.getSize());
    appendRuntimeCreations();
    allActiveGameObjects = findActiveObjects(allGameObjects);
    allActiveDestroyables = findActiveObjects(allDestroyables);
    worldCollisionHandling.updateActiveGameObjects(allActiveGameObjects, allActiveDestroyables);
    sendViewCoords();
  }

  private void updatePositions() {
    for (GameObject go : allActiveDestroyables) {
      go.updatePosition();
    }
    player.updatePosition();
  }

  private void findBricks() {
    for (GameObject go : allGameObjects) {
      // TODO ah
      if (go.getEntityType().contains("Block")) {
        allBricks.add(go);
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
    if (player.getPosition().getX() + player.getSize().getX() >= screenLimitsMax.getX()) {
      playerWin = true;
      System.out.println("YOU WON!!!!!");
    }
  }

  public void queueNewMovingDestroyable(List<MovingDestroyable> newMovingDestroyables) {
    runtimeCreations.addAll(newMovingDestroyables);
  }

  private void appendRuntimeCreations() {
    allGameObjects.addAll(runtimeCreations);
    allDestroyables.addAll(runtimeCreations);
    runtimeCreations.clear();
  }


  // TODO: refactor out isActive from GameObject and calculate active status here DO THIS !!!!!
  private List<GameObject> findActiveObjects(List<GameObject> allObjects) {
    Vector topL = frameCoords[0];
    Vector botR = frameCoords[3];
    List<GameObject> ret = new ArrayList<>();
    if(!player.isAlive()) {
      player.setActive(false);
    } else {
      player.setActive(true);
    }
    for (GameObject o : allObjects) {
      Vector oTopL = o.getPosition();
      Vector oTopR = o.getPosition().add(new Vector(o.getSize().getX(), 0));
      Vector oBotL = o.getPosition().add(new Vector(0,o.getSize().getY()));
      Vector oBotR = o.getPosition().add(new Vector(o.getSize().getX(),o.getSize().getY()));

      if (oTopL.insideBox(topL,botR) || oTopR.insideBox(topL,botR) || oBotL.insideBox(topL, botR) || oBotR.insideBox(topL,botR) || allBricks.contains(o)) {
        ret.add(o);
        o.setActive(true);
      } else { o.setActive(false); }
    }
    return ret;
  }

  private void removeDeadActors(List<Integer> deadActors) {
    getScoreDead(deadActors);

    allGameObjects = removeIndicesFromList(allGameObjects, deadActors);
    allDestroyables = removeIndicesFromList(allDestroyables, deadActors);
    allBricks = removeIndicesFromList(allBricks, deadActors);

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

  private void frameCoordinates(Vector playerCoord, Vector playerSize) {
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
    return playerWin;
  }

  /**
   * Adds to score by given amount. Notifies listeners of change.
   *
   * @param increment
   */
  private void incrementScore(double increment) {
    double prevScore = score;
    score += increment;
    System.out.println("score: "+score);
    notifyListeners("score", prevScore, score);
  }


}