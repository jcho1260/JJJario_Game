package ooga.model;

import java.util.List;

/**
 * Represents NonPlayable objects that can be destroyed but not controlled by the user.
 *
 * @author Jessica Yang
 */
public class NonPlayable extends Destroyable {

  private final AutomatedMovement autoMove;

  /**
   * Default constructor for NonPlayable. If NonPlayable moves indefinitely in a direction,
   * finalPosition should be the boundary of the game. todo add destroy method that codes that
   *
   * @param initialVelocity
   * @param initialPosition
   * @param finalPosition
   */
  public NonPlayable(List<String> entityTypes, Vector initialPosition, Vector initialVelocity,
      double gravityScale, int id, Vector size, Vector finalPosition) {
    super(entityTypes, initialPosition, initialVelocity, gravityScale, id, size);
    autoMove = new AutomatedMovement(initialPosition, finalPosition, initialVelocity);
  }

  /**
   * Handles movement in a frame of time.
   *
   * @param elapsedTime
   * @param gameGravity
   */
  @Override
  public void stepMovement(double elapsedTime, double gameGravity) {
    setPosition(autoMove.stepMovement(elapsedTime, gameGravity, getPosition(), getGravityScale()));
  }
}