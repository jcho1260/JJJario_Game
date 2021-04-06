package ooga.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class Destroyable extends GameObject{
  private Map<Method, List<double[]>> collisions; //TODO: refactor so its not a list of arrays?
  private DestroyableCollisionHandling collisionHandler;
  private Health health;

  /**
   * Default constructor with default lives, health values
   */
  public Destroyable(List<String> entityTypes, Vector position, int id, Vector size, int startLife, int startHealth) {
    super(entityTypes, position, id, size);
    collisions = new HashMap<>();
    collisionHandler = new DestroyableCollisionHandling();
  }

  /**
   *
   * @return
   */
  public int getLives() {
    return 0;
  }

  /**
   *
   * @return
   */
  public int getHealth() {
    return 0;
  }

  /**
   *
   */
  public boolean isDead() {
    return true;
  }

  /**
   * create a Queue of all methods to invoke on self for collisions with other GameObjects
   */
  public void addCollision(List<MethodBundle> methods) throws NoSuchMethodException {
    collisions = collisionHandler.addCollision(this.getClass(), methods, collisions);
  }

  /**
   * execute all impacts of collisions with other GameObjects to self
   */
  public void executeCollisions(Object collided) {
    collisionHandler.executeCollisions(collided, collisions);
  }

}
