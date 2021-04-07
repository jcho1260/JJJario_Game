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
      int startLife, int startHealth, double jumpTime, Vector velocityMagnitude, double gravity)
      throws ClassNotFoundException {
    super(entityTypes, initialPosition, id, objSize, startLife, startHealth);
    userMovement = new UserInputMovement(jumpTime, velocityMagnitude, gravity);
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
   * Returns List of active power ups.
   *
   * @return activePowerUps
   */
  public List<GameObject> getActivePowerUps() {
    return new ArrayList<>(activePowerUps);
  }

  private void scaleSize(double scaleFactor) { size.scaleSize(scaleFactor); }

  private void incrementScore(double increment) { score += increment; }

  private void incrementLife(double increment) { lives += increment; }

  private void onKeyPress() { }
}