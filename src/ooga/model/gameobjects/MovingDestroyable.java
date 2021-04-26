package ooga.model.gameobjects;

import java.util.List;
import ooga.model.gameobjectcomposites.AutomatedMovement;
import ooga.model.util.Vector;

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
   * @param vis
   */
  public MovingDestroyable(List<String> entityTypes, Vector initialPosition, int id, Vector size,
      int startLife, int startHealth, int points, Vector initialVelocity, Vector finalPosition,
      double gravityScale, boolean vis) {
    super(entityTypes, initialPosition, id, size, startLife, startHealth, points, vis);
    autoMove = new AutomatedMovement(initialPosition, finalPosition, initialVelocity, gravityScale);
  }

  /**
   * Updates position of object with each step of animation while also calling repeated methods if
   * applicable
   *
   * @param elapsedTime
   * @param gameGravity
   */
  @Override
  public void step(double elapsedTime, double gameGravity) {
    setPredictedPosition(autoMove.stepMovement(elapsedTime, gameGravity, getPredictedPosition()));
  }

  /**
   * Returns velocity.
   *
   * @return velocity from autoMove
   */
  @Override
  public Vector getVelocity() {
    return autoMove.getVelocity();
  }

  public void scaleVelocity(Double xVel, Double yVel) {
    Vector newVelo = autoMove.getVelocity().multiply(new Vector(xVel, yVel));
    autoMove.setVelocity(newVelo);
  }
}

