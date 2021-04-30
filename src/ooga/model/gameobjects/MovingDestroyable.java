package ooga.model.gameobjects;

import java.util.List;
import ooga.model.gameobjectcomposites.AutomatedMovement;
import ooga.model.util.Vector;

/**
 * A GameObject that can move and be destroyed. Movement is impemented through AutomatedMovement.
 *
 * @author Jin Cho, Juhyoung Lee, Jessica Yang
 */
public class MovingDestroyable extends Destroyable {

  private final AutomatedMovement autoMove;

  /**
   * Constructor for MovingDestroyable.
   *
   * @param entityTypes tags
   * @param initialPosition starting loc
   * @param id id
   * @param size size
   * @param startLife initial lives
   * @param startHealth initial health
   * @param initialVelocity starting velocity
   * @param finalPosition ending position
   * @param gravityScale multiplier
   * @param vis visibility
   */
  public MovingDestroyable(List<String> entityTypes, Vector initialPosition, int id, Vector size,
      int startLife, int startHealth, int points, Vector initialVelocity, Vector finalPosition,
      double gravityScale, boolean vis) {
    super(entityTypes, initialPosition, id, size, startLife, startHealth, points, vis);
    autoMove = new AutomatedMovement(initialPosition, finalPosition, initialVelocity, gravityScale);
  }

  /**
   * Updates position of object with each step of animation while also calling repeated methods if
   * applicable.
   *
   * @param elapsedTime frame duration
   * @param gameGravity overall gravity
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

  /**
   * Scales object velocity.
   *
   * @param xVel x multiplier
   * @param yVel y multiplier
   */
  public void scaleVelocity(Double xVel, Double yVel) {
    Vector newVelo = autoMove.getVelocity().multiply(new Vector(xVel, yVel));
    autoMove.setVelocity(newVelo);
  }
}

