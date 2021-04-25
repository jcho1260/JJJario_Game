package ooga.model.gameobjectcomposites;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import ooga.JjjanException;
import ooga.model.gameobjects.Player;
import ooga.model.util.MethodBundle;
import ooga.model.gameobjects.Destroyable;
import ooga.model.gameobjects.GameObject;
import ooga.model.util.Vector;

/**
 *
 */
public class WorldCollisionHandling implements Serializable {

  private Map<String, Map<String, List<MethodBundle>>> collisionMethods;
  private List<GameObject> activeGameObjects;
  private List<GameObject> activeDestroyable;
  private Player player;
  private Set<Destroyable> collisions;  // for execution of methods
  private List<Entry<GameObject, GameObject>> collisionPairs; // for post collision position fixes

  /**
   * Default constructor
   */
  public WorldCollisionHandling(Map<String, Map<String, List<MethodBundle>>> collisionMethodsMap,
      List<GameObject> activeGameObjectsList, List<GameObject> activeActorsList, Player gamePlayer) {
    collisionMethods = collisionMethodsMap;
    activeGameObjects = activeGameObjectsList;
    activeDestroyable = activeActorsList;
    player = gamePlayer;
    collisions = new HashSet<>();
    collisionPairs = new ArrayList<>();
  }

  public void clear() {
    collisionPairs = new ArrayList<>();
    collisions = new HashSet<>();
  }
  /**
   *
   * @param gameObjects
   * @param destroyables
   */
  public void updateActiveGameObjects(List<GameObject> gameObjects, List<GameObject> destroyables) {
    activeGameObjects = gameObjects;
    activeDestroyable = destroyables;
  }

  /**
   *
   */
  public boolean detectAllCollisions() throws NoSuchMethodException, JjjanException {
    // TODO refactorrrr here
    activeDestroyable.add(player);
    activeGameObjects.add(player);
    for (GameObject actor : activeDestroyable) {
      for (GameObject collisionObject : activeGameObjects) {
        if (actor.equals(collisionObject)) {
          continue;
        }
        List<String> directionalTags = ((Destroyable) actor).determineCollision(collisionObject);
        if (!directionalTags.isEmpty()) {
          List<MethodBundle> actorCollisionMethods = handleTagHierarchy(actor.getEntityType(), directionalTags);
          ((Destroyable) actor).addCollision(actorCollisionMethods);
          if (!((Destroyable) actor).cornerCollision(collisionObject)) {
            collisions.add(((Destroyable) actor));
//            System.out.println(actor.getEntityType().get(actor.getEntityType().size() - 1)
//                + " " + collisionObject.getEntityType().get(collisionObject.getEntityType().size() - 1)+" "+directionalTags.get(directionalTags.size()-1));
          }
          Entry<GameObject, GameObject> pair = new SimpleEntry<>(actor, collisionObject);
          Entry<GameObject, GameObject> unPair = new SimpleEntry<>(collisionObject, actor);
          if (!collisionPairs.contains(unPair)) {
            collisionPairs.add(pair);

          }
        }
      }
    }

    // TODO refacotr :')
    activeDestroyable.remove(player);
    activeGameObjects.remove(player);

    return !collisionPairs.isEmpty();
  }

  public void fixIntersection(List<GameObject> allBricks) {
    for (Entry<GameObject, GameObject> pair : collisionPairs) {
      Destroyable destroyable = (Destroyable) pair.getKey();
      GameObject gameObject = pair.getValue();

      Vector[] collisionRect = destroyable.determineCollisionRect(destroyable, gameObject);
      if (collisionRect == null) return;
      Vector direction = destroyable.calculateCollisionDirection(destroyable, collisionRect)
          .toUnit().multiply(new Vector(-0.5, -0.5));
      Vector collisionRectSize = new Vector(collisionRect[1].getX() - collisionRect[0].getX(),
          collisionRect[1].getY() - collisionRect[0].getY());
      Vector fixAmount = collisionRectSize.multiply(direction).add(direction.multiply(new Vector(0, 0)));
      destroyable.setPredictedPosition(destroyable.getPredictedPosition().add(fixAmount));

      // TODO use list of all blocks to check what to move
      if (!allBricks.contains(gameObject)) {
        gameObject.setPredictedPosition(gameObject.getPredictedPosition().add(fixAmount
            .multiply(new Vector(-1, -1))));
      } else {
        destroyable.setPredictedPosition(destroyable.getPredictedPosition().add(fixAmount));
      }
    }

  }

  /**
   * executes all collision methods for every destroyable that is colliding with a game object. also checks if destroyable should die.
   */
  public List<Integer> executeAllCollisions()
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    List<Integer> toDelete = new ArrayList<>();
    for (Destroyable destroyable : collisions) {
      destroyable.executeCollisions();
      if (!destroyable.isAlive()) {
        destroyable.kill();
        toDelete.add(destroyable.getId());
      }
    }
    return toDelete;
  }

  private List<MethodBundle> handleTagHierarchy(List<String> destroyableTags, List<String> collidedTags)
      throws JjjanException {
    for (int d = destroyableTags.size() - 1; d >= 0; d--) {
      String dTag = destroyableTags.get(d);
//      System.out.println("DTAG: "+dTag);
      if (collisionMethods.containsKey(dTag)) {
        for (int c = collidedTags.size() - 1; c >= 0; c--) {
//          System.out.println("COLLIDED TAGS: "+collidedTags.get(c));
          Map<String, List<MethodBundle>> destroyableCollisionMap = collisionMethods.get(dTag);
          if (destroyableCollisionMap.containsKey(collidedTags.get(c))) {
            return destroyableCollisionMap.get(collidedTags.get(c));
          }
        }
      }
    }
    throw new JjjanException("collision handling not defined for destroyable");
  }
}