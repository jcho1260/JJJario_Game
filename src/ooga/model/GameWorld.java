package ooga.model;

import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.Map;

/**
 * 
 */
public class GameWorld {
    private List<GameObject> allGameObjects;
    private List<GameObject> allActors;
    private CollisionCheck collisionCheck;

    /**
     * Default constructor
     */
    public GameWorld(Map<String, Map<String, List<String>>> collisionMethods, List<GameObject> gameObjects) {
        allGameObjects = gameObjects;
        collisionCheck = new CollisionCheck(collisionMethods, gameObjects);
    }

    public void stepFrame() {
        collisionCheck.detectAllCollisions();
        collisionCheck.executeAllCollisions();
    }

    /**
     * 
     */
    public List<GameObject> getAllActors() {
        // TODO implement here
        return null;
    }

    /**
     * 
     */
    public List<GameObject> getActiveActors() {
        // TODO implement here
        return null;
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