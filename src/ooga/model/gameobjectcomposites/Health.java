package ooga.model.gameobjectcomposites;

public class Health {

  private int health;
  private int lives;

  public Health(int startingHealth, int startingLives) {
    health = startingHealth;
    lives = startingLives;
  }

  /**
   * Increments health by a given amount.
   *
   * @param increment
   */
  public void incrementHealth(int increment) {
    System.out.println("incrementing health");
    health += increment;
    if (health <= 0) {
      lives--;
    }
  }

  /**
   * Increments lives by a given amount.
   *
   * @param increment
   */
  public void incrementLives(int increment) {
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

  public void kill() { health = 0; }
}
