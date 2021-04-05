package ooga.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class GameWorld {

  private List<GameObject> allGameObjects;
  private List<GameObject> allActiveGameObjects;
  private List<GameObject> allActors;
  private List<GameObject> allActiveActors;
  private CollisionCheck collisionCheck;
  private Player player;
  private Vector windowSize;

  private final double gravity;
  private final double stepTime;

  /**
   * Default constructor
   */
  public GameWorld(Player gamePlayer, Map<String, Map<String, List<MethodBundle>>> collisionMethods,
      List<GameObject> gameObjects, List<GameObject> actors, Vector frameSize, double levelGravity, double frameRate) {
    player = gamePlayer;
    windowSize = frameSize;
    allGameObjects = gameObjects;
    gravity = levelGravity;
    stepTime = frameRate;
    allActiveGameObjects = findActiveObjects(allGameObjects);
    allActors = actors;
    allActiveActors = findActiveObjects(allActors);
    collisionCheck = new CollisionCheck(collisionMethods, gameObjects, actors);
  }

  public void stepFrame(Movement pressEffect) throws NoSuchMethodException {
    collisionCheck.detectAllCollisions();
    List<Integer> forDeletion = collisionCheck.executeAllCollisions();
    removeDeadActors(forDeletion);
    allActiveGameObjects = findActiveObjects(allGameObjects);
    allActiveActors = findActiveObjects(allActors);
  }

  // TODO: refactor out isActive from GameObject and calculate active status here DO THIS !!!!!
  private List<GameObject> findActiveObjects(List<GameObject> allObjects) {
    Vector topL = getWindowFrame()[0];
    Vector botR = getWindowFrame()[1];
    List<GameObject> ret = new ArrayList<>();
    for (GameObject o : allObjects) {
      Vector oTopL = o.getPosition();
      Vector oTopR = o.getPosition().add(new Vector(o.getSize().getX(), 0));
      Vector oBotL = o.getPosition().add(new Vector(0,o.getSize().getY()));
      Vector oBotR = o.getPosition().add(new Vector(o.getSize().getX(),o.getSize().getY()));

      if (oTopL.insideBox(topL,botR) || oTopR.insideBox(topL,botR) || oBotL.insideBox(topL, botR) || oBotR.insideBox(topL,botR)) {
        ret.add(o);
      }
    }
    return ret;
  }

  // TODO LATER
  private Vector[] getWindowFrame() {
    return new Vector[0];
  }

  private void removeDeadActors(List<Integer> deadActors) {
    allGameObjects = removeIndicesFromList(allGameObjects, deadActors);
    allActors = removeIndicesFromList(allActors, deadActors);
  }

  private List<GameObject> removeIndicesFromList(List<GameObject> objects, List<Integer> deadActors) {
    for(int j = objects.size() - 1; j >= 0; j--) {
      if (deadActors.contains(objects.get(j).getId())) {
        objects.remove(j);
      }
    }
    return objects;
  }

  /**
   *
   */
  public List<GameObject> getAllActors() {
    // TODO implement here
    return allActors;
  }

  /**
   *
   */
  public List<GameObject> getActiveActors() {
    // TODO implement here
    return allActiveActors;
  }

  /**
   *
   */
  public List<GameObject> getAllGameObjects() {
    // TODO implement here
    return allGameObjects;
  }

  /**
   *
   */
  public List<GameObject> getActiveGameObjects() {
    // TODO implement here
    return null;
  }

  public double getGravity() {
    return gravity;
  }

  public double getStepTime() {
    return stepTime;
  }

  /**
   *
   */
  public List<PropertyChangeListener> getAllListeners() {
    // TODO implement here
    return null;
  }

  /**
   *
   */
  public void acceptAllListeners(List<PropertyChangeListener> listeners) {
    // TODO implement here
  }
}