package ooga.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class Destroyable extends GameObject{
  private Map<String, List<MethodBundle>> collisionMap;
  private Queue<MethodBundle> collisionQueue;
  private DestroyableCollisionHandling collisionHandler;
  private Health health;

  /**
   * Default constructor with default lives, health values
   */
  public Destroyable(List<String> entityTypes, Vector position, int id, Vector size, int startLife, int startHealth, Map<String, List<MethodBundle>> collisionSet) {
    super(entityTypes, position, id, size);
    collisionMap = collisionSet;
    collisionQueue = new LinkedList<>();
    collisionHandler = new DestroyableCollisionHandling();
    health = new Health(startHealth);
  }

  /**
   *
   */
  public boolean isDead() { return !health.isAlive(); }


  public List<String> determineCollision(GameObject o) {
    return collisionHandler.determineCollisionMethods(this, o);
  }

  /**
   * create a Queue of all methods to invoke on self for collisions with other GameObjects
   */
  public boolean addCollision(String entityTag) throws NoSuchMethodException {
    if (!collisionMap.containsKey(entityTag)) {
      return false;
    }
    for (MethodBundle mb : collisionMap.get(entityTag)) {
      collisionQueue.add(mb);
    }
    return true;
  }

  /**
   * execute all impacts of collisions with other GameObjects to self
   */
  public void executeCollisions()
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    collisionHandler.executeCollisions(this, collisionQueue);
  }

  private void incrementHealth(int increment) {health.incrementHealth(increment);}
}
