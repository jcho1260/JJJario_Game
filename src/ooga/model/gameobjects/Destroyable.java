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
  public Destroyable(List<String> entityTypes, Vector position, int id, Vector size, int startLife, int startHealth, int points) {
    super(entityTypes, position, id, size);
    collisionQueue = new LinkedList<>();
    collisionHandler = new DestroyableCollisionHandling();
    health = new Health(startHealth, startLife);
    score = points;
  }

  /**
   * checks to see if destroyable is still alive
   * @return true if alive
   */
  public boolean isAlive() { return health.isAlive(); }

  /**
   * checks to see if the collision is a small corner collision with the object and should be ignored
   * @param o object destroyable is colliding with
   * @return true if it is a small corner collision
   */
  public boolean cornerCollision(GameObject o) {
    return collisionHandler.smallCorner(this, o);
  }

  /**
   *
   * @param o
   * @return
   */
  public List<String> determineCollision(GameObject o) {
    return collisionHandler.determineCollisionMethods(this, o);
  }

  public Vector[] determineCollisionRect(GameObject myself, GameObject o) {
    return collisionHandler.determineCollisionRectangle(myself, o);
  }

  public Vector calculateCollisionDirection(GameObject myself, Vector[] collisionBox) {
    return collisionHandler.calculateCollisionDirection(myself, collisionBox);
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

  public void producePowerUp() {

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

  /**
   * gets the number of points a destroyable is worth once destroyed
   * @return number of points destroyable is worth
   */
  public double getScore() { return score; }


  public void kill() {
    notifyListeners("changeVisibility", true, false);
    health.kill();
  }

  private void onDeath() {
    //TODO: implement
  }


}
