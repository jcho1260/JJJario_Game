package ooga.model;

import java.util.*;

/**
 *
 */
public class CollisionCheck {
    private Map<String, Map<String, List<String>>> collisionMethods;
    private List<GameObject> allGameObjects;
    private List<GameObject> allActors;
    private Set<Actor> collisions;

    /**
     * Default constructor
     */
    public CollisionCheck(Map<String, Map<String, List<String>>> collisionMethods, List<GameObject> allGameObjects, List<GameObject> allActors) {
        this.collisionMethods = collisionMethods;
        this.allGameObjects = allGameObjects;
        collisions = new HashSet<>();
    }


    /**
     *
     */
    public void detectAllCollisions() {
        // TODO implement here
        for (GameObject actor : allActors) {
            for (GameObject collisionObject : allGameObjects) {
                if (actor.isCollision(collisionObject)) {
                    List<String> actorCollisionMethods = collisionMethods.get(actor).get(collisionObject);
                    ((Actor)actor).addCollision(actorCollisionMethods);
                    collisions.add(((Actor)actor));
                }
            }
        }
    }

    /**
     *
     */
    public List<GameObject> executeAllCollisions() {
        List<GameObject> toDelete = new ArrayList<>();
        for (Actor actor : collisions) {
            actor.executeCollisions();
            if (actor.isDead()) {
                toDelete.add(actor);
            }
        }
        return toDelete;
    }

}