package ooga.model.gameobjects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import ooga.model.gameobjectcomposites.UserInputMovement;
import ooga.model.util.Action;
import ooga.model.util.Vector;

/**
 * Represents Player object that receives movement input from the user.
 *
 * @author Jessica Yang
 */
public class Player extends Destroyable {

  private List<GameObject> activePowerUps;
  private final UserInputMovement userMovement;
  private final Class<?> userMovementClass;
  private int lives;

  /**
   * Default constructor for Player.
   */
  public Player(List<String> entityTypes, Vector initialPosition, int id, Vector objSize,
      int startLife, int startHealth, double jumpTime, Vector velocityMagnitude, double gravity,
      Vector drivingVelocity)
      throws ClassNotFoundException {
    super(entityTypes, initialPosition, id, objSize, startLife, startHealth, 5);
    userMovement = new UserInputMovement(jumpTime, velocityMagnitude, gravity, drivingVelocity);
    userMovementClass = Class.forName("ooga.model.gameobjectcomposites.UserInputMovement");
    lives = startLife;
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
    System.out.println(direction);
    Method moveMethod = userMovementClass.getMethod(methodName, paramClasses);
    Vector deltaPosition = (Vector) moveMethod.invoke(userMovement, elapsedTime, gameGravity);
    setPredictedPosition(getPredictedPosition().add(deltaPosition));
  }

  /**
   * Collision method for whenever the bottom of player lands on something.
   */
  public void generalBottomCollision() {
    userMovement.hitGround();
  }

  /**
   * gets velocity of player
   * @return
   */
  public Vector getVelocity() {return userMovement.getVelocity();}

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
  public void incrementHealth(Double increment) {
    int prevHealth = getHealth();
    int prevLives = getLives();

    super.incrementHealth(increment);
//    notifyListeners("playerHealth", prevHealth, getHealth());

    if (getHealth() != prevLives) {
//      notifyListeners("playerLives", prevLives, getLives());
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
//    notifyListeners("playerLives", prevLives, getLives());
  }

  /**
   * Scales velocity by given amount.
   *
   * @param x
   * @param y
   */
  public void scaleVelocity(Double x, Double y) {
    Vector newVelocity = userMovement.getVelocity().multiply(new Vector(x, y));
    userMovement.setVelocity(newVelocity);
  }

  /**
   * Returns List of active power ups.
   *
   * @return activePowerUps
   */
  public List<GameObject> getActivePowerUps() {
    return new ArrayList<>(activePowerUps);
  }

  private void scaleSize(Double scaleFactor) {
    getRect().scaleSize(scaleFactor);
  }

  private void incrementScore(Double increment) { score += increment; }

  private void incrementLife(Double increment) { lives += increment; }

  private void onKeyPress() { }
}