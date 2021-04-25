package ooga.model.gameobjectcomposites;

import java.io.Serializable;

public class Health implements Serializable {

  private int startHealth;
  private int health;
  private int lives;

  public Health(int startingHealth, int startingLives) {
    startHealth = startingHealth;
    health = startingHealth;
    lives = startingLives;
  }

  /**
   * Increments health by a given amount.
   *
   * @param increment
   */
  public void incrementHealth(Double increment) {
    health += increment;
  }

  /**
   * Increments lives by a given amount.
   *
   * @param increment
   */
  public void incrementLives(Double increment) {
    lives += increment;
  }

  /**
   * Checks if the Destroyable is alive.
   *
   * @return boolean
   */
  public boolean isAlive() {
    return health > 0 && lives >= 0;
  }

  /**
   * Retrieves health.
   *
   * @return health
   */
  public int getHealth() {
    return health;
  }

  /**
   * Retrieves lives.
   *
   * @return lives
   */
  public int getLives() {
    return lives;
  }

  public void loseLife() {
    health = startHealth;
    lives--;
  }

  public void kill() {
    health = startHealth;
    lives--;
  }
}
