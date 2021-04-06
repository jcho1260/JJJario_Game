package ooga.model;

import java.util.*;

/**
 *
 */
public class CollisionCheck {

  private Map<String, Map<String, List<MethodBundle>>> collisionMethods;
  private List<GameObject> activeGameObjects;
  private List<GameObject> activeActors;
  private Set<Actor> collisions;

  /**
   * Default constructor
   */
  public CollisionCheck(Map<String, Map<String, List<MethodBundle>>> collisionMethodsMap,
      List<GameObject> activeGameObjectsList, List<GameObject> activeActorsList) {
    collisionMethods = collisionMethodsMap;
    activeGameObjects = activeGameObjectsList;
    activeActors = activeActorsList;
    collisions = new HashSet<>();
  }


  /**
   *
   */
  public void detectAllCollisions() throws NoSuchMethodException {
    // TODO implement here
    for (GameObject actor : activeActors) {
      for (GameObject collisionObject : activeGameObjects) {
        if (actor.equals(collisionObject)) {
          continue;
        }
        if (actor.isCollision(collisionObject).size() != 0) {
          List<MethodBundle> actorCollisionMethods = collisionMethods.get(actor)
              .get(collisionObject);
          ((Actor) actor).addCollision(actorCollisionMethods);
          collisions.add(((Actor) actor));
        }
      }
    }
  }

  /**
   *
   */
  public List<Integer> executeAllCollisions() {
    List<Integer> toDelete = new ArrayList<>();
    for (Actor actor : collisions) {
      actor.executeCollisions();
      if (actor.isDead()) {
        toDelete.add(actor.getId());
      }
    }
    return toDelete;
  }

  /*
  TODO: DELETE THIS LATER THIS IS FOR TESTING
   */
  public void collidedActors() {
    StringBuilder
    for (Actor a : collisions) {
      for (String tag : a.getEntityType()) {

      }
    }
  }
}