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

    public static final double GAME_GRAVITY;
    public static final double STEP_TIME;

    /**
     * Default constructor
     */
    public GameWorld(Map<String, Map<String, List<String>>> collisionMethods, List<GameObject> gameObjects, List<GameObject> actors) {
        allGameObjects = gameObjects;
        allActiveGameObjects = reduceActiveObjects(allGameObjects);
        allActors = actors;
        allActiveActors = reduceActiveObjects(allActors);
        collisionCheck = new CollisionCheck(collisionMethods, gameObjects, actors);
    }

    public void stepFrame(KeyEffects pressEffect) {
        collisionCheck.detectAllCollisions();
        allGameObjects = collisionCheck.executeAllCollisions();
    }

    private List<GameObject> reduceActiveObjects(List<GameObject> allObjects) {
        List<GameObject> ret = new ArrayList<>();
        for(GameObject o : allObjects) {
            if(o.isActive()) { ret.add(o); }
        }
        return ret;
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