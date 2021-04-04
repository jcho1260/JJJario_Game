package ooga.model;

import java.util.*;

/**
 *
 */
public class CollisionCheck {
    private Map<String, Map<String, List<String>>> collisionMethods;
    private List<GameObject> allGameObjects;
    private List<Actor> allActors;
    private Set<Actor> collisions;

    /**
     * Default constructor
     */
    public CollisionCheck(Map<String, Map<String, List<String>>> collisionMethods, List<GameObject> allGameObjects) {
        this.collisionMethods = collisionMethods;
        this.allGameObjects = allGameObjects;
        collisions = new HashSet<>();
    }


    /**
     *
     */
    public void detectAllCollisions() {
        // TODO implement here
        for (Actor actor : allActors) {
            for (GameObject collisionObject : allGameObjects) {
                if (actor.isCollision(collisionObject)) {
                    List<String> actorCollisionMethods = collisionMethods.get(actor).get(collisionObject);
                    actor.addCollision(actorCollisionMethods);
                    collisions.add(actor);
                }
            }
        }
    }

    /**
     *
     */
    public List<GameObject> executeAllCollisions() {
        for (Actor actor : collisions) {
            actor.executeCollisions();
            if (actor.isDead()) {
                //remove from the game objects list
            }
        }
        return allGameObjects;
    }

}