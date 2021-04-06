package ooga.model;

import java.util.*;

/**
 *
 */
public class WorldCollisionHandling {

  private Map<String, Map<String, List<MethodBundle>>> collisionMethods;
  private List<GameObject> activeGameObjects;
  private List<GameObject> activeActors;
  private Set<Destroyable> collisions;

  /**
   * Default constructor
   */
  public WorldCollisionHandling(Map<String, Map<String, List<MethodBundle>>> collisionMethodsMap,
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
          ((Destroyable) actor).addCollision(actorCollisionMethods);
          collisions.add(((Destroyable) actor));
        }
      }
    }
  }

  /**
   *
   */
  public List<Integer> executeAllCollisions() {
    List<Integer> toDelete = new ArrayList<>();
    for (Destroyable destroyable : collisions) {
      destroyable.executeCollisions(destroyable);
      if (destroyable.isDead()) {
        toDelete.add(destroyable.getId());
      }
    }
    return toDelete;
  }

  /*
  TODO: DELETE THIS LATER THIS IS FOR TESTING
   */
  public void collidedActors() {
    StringBuilder
    for (Destroyable a : collisions) {
      for (String tag : a.getEntityType()) {

      }
    }
  }
}