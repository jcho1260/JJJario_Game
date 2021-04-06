package ooga.model;

import java.util.List;

public class MovingDestroyable extends GameObject {

  private final AutomatedMovement autoMove;

  public MovingDestroyable(List<String> entityTypes, Vector initialPosition, int id, Vector size,
      Vector initialVelocity, Vector finalPosition, double gravityScale) {
    super(entityTypes, initialPosition, id, size);
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