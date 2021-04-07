package ooga.model.gameobjectcomposites;

public class Health {

  private int health;

  public Health(int startingHealth) {
    health = startingHealth;
  }

  public void incrementHealth(int increment) {
    health += increment;
  }

  public boolean isAlive() {
    return health > 0;
  }

  public void kill() { health = 0; }
}
