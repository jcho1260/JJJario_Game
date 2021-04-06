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
   * Default constructor
   */
  public Player(List<String> entityTypes, Vector initialPosition, int id, Vector size,
      int startLife, int startHealth, Vector velocityMagnitude, double gravity) throws ClassNotFoundException {
    super(entityTypes, initialPosition, id, size, startLife, startHealth);
    userMovement = new UserInputMovement(velocityMagnitude, gravity);
    userMovementClass = Class.forName("ooga.model.UserInputMovement");
  }

  /**
   * Handles player movement given user input.
   *
   * @param direction
   * @param elapsedTime
   * @param gameGravity
   * @param gravityScale
   */
  public void userStepMovement(Action direction, double elapsedTime, double gameGravity,
      double gravityScale) throws NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    String methodName = "move" + direction.toString();
    Class<?>[] paramClasses = new Class[3];
    for (int i = 0; i < 3; i++) {
      paramClasses[i] = Double.class;
    }

    Method moveMethod = userMovementClass.getMethod(methodName, paramClasses);
    Vector deltaPosition = (Vector) moveMethod.invoke(userMovement, elapsedTime, gameGravity, gravityScale);
    setPosition(new Vector((deltaPosition.getX() + getPosition().getX()), (deltaPosition.getY() + getPosition().getY())));
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