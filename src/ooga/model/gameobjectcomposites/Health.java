package ooga.model.gameobjectcomposites;

import java.io.Serializable;

/**
 * Health composite element that adds health information to a GameObject.
 *
 * @author Juhyoung Lee, Jessica Yang, Jin Cho
 */
public class Health implements Serializable {

  private final double startHealth;
  private double health;
  private double lives;

  /**
   * Default constructor.
   *
   * @param startingHealth initial health
   * @param startingLives initial lives
   */
  public Health(int startingHealth, int startingLives) {
    startHealth = startingHealth;
    health = startingHealth;
    lives = startingLives;
  }

  /**
   * Increments health by a given amount.
   *
   * @param increment amount
   */
  public void incrementHealth(Double increment) {
    health += increment;
  }

  /**
   * Increments lives by a given amount.
   *
   * @param increment amount
   */
  public void incrementLives(Double increment) {
    lives += increment;
  }

  /**
   * Checks if the Destroyable is alive.
   *
   * @return alive status
   */
  public boolean isAlive() {
    return health > 0 && lives >= 0;
  }

  /**
   * Retrieves health.
   *
   * @return health
   */
  public double getHealth() {
    return health;
  }

  /**
   * Retrieves lives.
   *
   * @return lives
   */
  public double getLives() {
    return lives;
  }

  /**
   * Subtracts one life
   */
  public void loseLife() {
    health = startHealth;
    lives--;
  }

  /**
   * Sets health to zero
   */
  public void kill() {
    health = 0;
  }
}
