package ooga.model.gameobjects;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import ooga.model.gameobjectcomposites.Health;
import ooga.model.util.MethodBundle;
import ooga.model.util.Vector;

public class Destroyable extends GameObject{
  private Queue<MethodBundle> collisionQueue;
  private DestroyableCollisionHandling collisionHandler;
  private Health health;
  protected int score; //TODO: need in player to increment score, but nonplayer destroyables also have a score

  /**
   * Default constructor with default lives, health values
   */
  public Destroyable(List<String> entityTypes, Vector position, int id, Vector size, int startLife, int startHealth) {
    super(entityTypes, position, id, size);
    collisionQueue = new LinkedList<>();
    collisionHandler = new DestroyableCollisionHandling();
    health = new Health(startHealth, startLife);
  }

  /**
   *
   */
  public boolean isAlive() { return health.isAlive(); }


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

  /**
   * Increments health by a given amount. Uses Health methods.
   *
   * @param increment
   */
  public void incrementHealth(Double increment) {
    health.incrementHealth(increment);
  }

  /**
   * Increments lives by a given amount. Uses Health methods.
   *
   * @param increment
   */
  public void incrementLives(int increment) {
    health.incrementLives(increment);
  }

  /**
   * Retrieves health for child classes.
   *
   * @return health
   */
  protected int getHealth() {
    return health.getHealth();
  }

  /**
   * Retrives lives for child classes.
   *
   * @return lives
   */
  protected int getLives() {
    return health.getLives();
  }

  private void kill() {
    health.kill();
  }

  private void onDeath() {
    //TODO: implement
  }


}
