package ooga.model.gameobjects;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import ooga.model.gameobjectcomposites.Health;
import ooga.model.util.MethodBundle;
import ooga.model.util.Vector;

/**
 * Represents a destroyable object. This is a GameObject that has lives and can be destroyed.
 *
 * @author Jin Cho, Juhyoung Lee, Jessica Yang
 */
public class Destroyable extends GameObject {

  private final Queue<MethodBundle> collisionQueue;
  private final DestroyableCollisionHandling collisionHandler;
  protected Health health;
  protected int score;

  /**
   * Default constructor. Parameters should be read in from datafiles.
   *
   * @param entityTypes tags
   * @param position location
   * @param id id
   * @param size size
   * @param startLife initial lives
   * @param startHealth initial health
   * @param points scores to be dropped on destroy
   * @param vis visibility
   */
  public Destroyable(List<String> entityTypes, Vector position, int id, Vector size, int startLife,
      int startHealth, int points, boolean vis) {
    super(entityTypes, position, id, size, vis);
    collisionQueue = new LinkedList<>();
    collisionHandler = new DestroyableCollisionHandling();
    health = new Health(startHealth, startLife);
    score = points;
  }

  /**
   * Checks to see if destroyable is still alive
   *
   * @return true if alive
   */
  public boolean isAlive() {
    return health.isAlive();
  }

  /**
   * Checks to see if the collision is a small corner collision with the object and should be
   * ignored
   *
   * @param o object destroyable is colliding with
   * @return true if it is a small corner collision
   */
  public boolean cornerCollision(GameObject o) {
    return collisionHandler.smallCorner(this, o);
  }

  /**
   * Determines collision between this and new object.
   *
   * @param o other object
   * @return list of collision methods to execute. Will be empty if no collision.
   */
  public List<String> determineCollision(GameObject o) {
    return collisionHandler.determineCollisionMethods(this, o);
  }

  /**
   * Returns collision rectangle.
   *
   * @param myself object 1
   * @param o object 2
   * @return collision rectangle (top left, bottom right) or empty array if no collision
   */
  public Vector[] determineCollisionRect(GameObject myself, GameObject o) {
    return collisionHandler.determineCollisionRectangle(myself, o);
  }

  /**
   * Returns direction of collision from perspective of object 1. Assumes collision does occur.
   *
   * @param myself object 1
   * @param collisionBox collision location
   * @return direction of collision
   */
  public Vector calculateCollisionDirection(GameObject myself, Vector[] collisionBox) {
    return collisionHandler.calculateCollisionDirection(myself, collisionBox);
  }

  /**
   * Create a Queue of all methods to invoke on self for collisions with other GameObjects.
   *
   * @param methodList methods to be executed reflectively to handle collision
   */
  public void addCollision(List<MethodBundle> methodList) {
    for (MethodBundle mb : methodList) {
      collisionQueue.add(mb);
    }
  }

  /**
   * Execute all impacts of collisions with other GameObjects to self
   */
  public void executeCollisions()
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    collisionHandler.executeCollisions(this, collisionQueue);
  }

  /**
   * Increments health by a given amount. Uses Health methods.
   *
   * @param increment amount
   */
  public void incrementHealth(Double increment) {
    health.incrementHealth(increment);
    if (health.getHealth() <= 0) {
      health.loseLife();
    }
  }

  /**
   * Increments lives by a given amount. Uses Health methods.
   *
   * @param increment amount
   */
  public void incrementLives(Double increment) {
    health.incrementLives(increment);
  }

  /**
   * Retrieves health for child classes.
   *
   * @return health
   */
  protected double getHealth() {
    return health.getHealth();
  }

  /**
   * Retrieves lives for child classes.
   *
   * @return lives
   */
  protected double getLives() {
    return health.getLives();
  }

  /**
   * gets the number of points a destroyable is worth once destroyed
   *
   * @return number of points destroyable is worth
   */
  public double getScore() {
    return score;
  }

  /**
   * Destroys object and updates visibility via listeners.
   */
  public void kill() {
    health.kill();
    health.loseLife();
    if (!health.isAlive()) {
      notifyListenerKey("sprite", "changeVisibility", true, false);
    }
  }


}
