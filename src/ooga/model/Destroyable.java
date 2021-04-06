package ooga.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public abstract class Destroyable extends GameObject{
  private Queue<MethodBundle> collisionQueue;
  private DestroyableCollisionHandling collisionHandler;
  private Health health;

  /**
   * Default constructor with default lives, health values
   */
  public Destroyable(List<String> entityTypes, Vector position, int id, Vector size, int startLife, int startHealth) {
    super(entityTypes, position, id, size);
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
  public void addCollision(List<MethodBundle> methodList) throws NoSuchMethodException {
    for (MethodBundle mb : methodList) {
      collisionQueue.add(mb);
    }
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
