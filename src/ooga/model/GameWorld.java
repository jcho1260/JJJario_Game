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

  private final double gravity;
  private final double stepTime;

  /**
   * Default constructor
   */
  public GameWorld(Map<String, Map<String, List<MethodBundle>>> collisionMethods,
      List<GameObject> gameObjects, List<GameObject> actors, double gravity, double stepTime) {
    allGameObjects = gameObjects;
    this.gravity = gravity;
    this.stepTime = stepTime;
    allActiveGameObjects = reduceActiveObjects(allGameObjects);
    allActors = actors;
    allActiveActors = reduceActiveObjects(allActors);
    collisionCheck = new CollisionCheck(collisionMethods, gameObjects, actors);
  }

  public void stepFrame(Movement pressEffect) throws NoSuchMethodException {
    collisionCheck.detectAllCollisions();
    List<Integer> forDeletion = collisionCheck.executeAllCollisions();
    removeDeadActors(forDeletion);
  }

  // TODO: refactor out isActive from GameObject and calculate active status here DO THIS !!!!!
  private List<GameObject> reduceActiveObjects(List<GameObject> allObjects) {
    List<GameObject> ret = new ArrayList<>();
    for (GameObject o : allObjects) {
      if (o.isActive()) {
        ret.add(o);
      }
    }
    return ret;
  }

  private void removeDeadActors(List<Integer> deadActors) {
    for(Integer i : deadActors) {
      // TODO DO THIS !!!!! ENXT!!!!
    }
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