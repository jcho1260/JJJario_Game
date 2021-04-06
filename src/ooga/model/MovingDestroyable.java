package ooga.model;

import java.util.List;

public class MovingDestroyable extends Destroyable {

  private final AutomatedMovement autoMove;

  /**
   * Constructor for MovingDestroyable.
   *
   * @param entityTypes
   * @param initialPosition
   * @param id
   * @param size
   * @param startLife
   * @param startHealth
   * @param initialVelocity
   * @param finalPosition
   * @param gravityScale
   */
  public MovingDestroyable(List<String> entityTypes, Vector initialPosition, int id, Vector size,
      int startLife, int startHealth, Vector initialVelocity, Vector finalPosition,
      double gravityScale) {
    super(entityTypes, initialPosition, id, size, startLife, startHealth);
    autoMove = new AutomatedMovement(initialPosition, finalPosition, initialVelocity, gravityScale);
  }

  public void step(double elapsedTime, double gameGravity) {
    setPosition(autoMove.stepMovement(elapsedTime, gameGravity, getPosition()));
  }

  /**
   * Returns velocity.
   *
   * @return velocity from autoMove
   */
 public Vector getVelocity() {
    return autoMove.getVelocity();
  }
}

