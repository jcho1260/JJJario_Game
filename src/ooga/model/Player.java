package ooga.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents Player object that receives movement input from the user.
 *
 * @author Jessica Yang
 */
public class Player extends Destroyable {

  private List<GameObject> activePowerUps;
  private final UserInputMovement userMovement;
  private final Class<?> userMovementClass;

  /**
   * Default constructor for Player.
   */
  public Player(List<String> entityTypes, Vector initialPosition, int id, Vector size,
      int startLife, int startHealth, double jumpTime, Vector velocityMagnitude, double gravity)
      throws ClassNotFoundException {
    super(entityTypes, initialPosition, id, size, startLife, startHealth);
    userMovement = new UserInputMovement(jumpTime, velocityMagnitude, gravity);
    userMovementClass = Class.forName("ooga.model.UserInputMovement");
  }

  /**
   * Handles player movement given user input.
   *
   * @param direction
   * @param elapsedTime
   * @param gameGravity
   */
  public void userStepMovement(Action direction, double elapsedTime, double gameGravity)
      throws NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    String methodName = "move" + direction.toString();
    Class<?>[] paramClasses = new Class[2];
    for (int i = 0; i < 2; i++) {
      paramClasses[i] = Double.class;
    }

    Method moveMethod = userMovementClass.getMethod(methodName, paramClasses);
    Vector deltaPosition = (Vector) moveMethod.invoke(userMovement, elapsedTime, gameGravity);
    setPosition(getPosition().add(deltaPosition));
  }

  /**
   * Collision method for whenever the bottom of player lands on something.
   */
  public void generalBottomCollision() {
    userMovement.hasHitGround();
  }

  /**
   * Collision method for adding a new power up to the Player.
   */
  public void addPowerUp() {
    // TODO add actual logic for adding a powerup
    notifyListeners("powerUpChange", null, null); // TODO what does frontend need
  }

  /**
   * Collision method for removing an expired power up from the Player.
   */
  public void removePowerUp() {
    // TODO add actual logic for removing a power up
    notifyListeners("powerUpChange", null, null); // TODO what does frontend need
  }

  /**
   * Increments health of player by given amount. Notifies listeners of change in health, and if
   * applicable, change in lives.
   *
   * @param increment
   */
  @Override
  public void incrementHealth(int increment) {
    int prevHealth = getHealth();
    int prevLives = getLives();

    super.incrementHealth(increment);
    notifyListeners("playerHealth", prevHealth, getHealth());

    if (getHealth() != prevLives) {
      notifyListeners("playerLives", prevLives, getLives());
    }
  }

  /**
   * Increments lives of a player by a given amount. Notifies listeners of change in lives.
   *
   * @param increment
   */
  @Override
  public void incrementLives(int increment) {
    int prevLives = getLives();

    super.incrementLives(increment);
    notifyListeners("playerLives", prevLives, getLives());
  }

  /**
   * Returns List of active power ups.
   *
   * @return activePowerUps
   */
  public List<GameObject> getActivePowerUps() {
    return new ArrayList<>(activePowerUps);
  }
}