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
   */
  public MovingDestroyable(List<String> entityTypes, Vector initialPosition, int id, Vector size,
      int startLife, int startHealth, Vector initialVelocity, Vector finalPosition,
      double gravityScale) {
    super(entityTypes, initialPosition, id, size, startLife, startHealth);
    Vector fp = finalPosition.multiply(new Vector(50,50));
    autoMove = new AutomatedMovement(initialPosition, fp, initialVelocity, gravityScale);
  }

  /**
   * Updates position of object with each step of animation while also calling repeated methods if applicable
   * 
   * @param elapsedTime
   * @param gameGravity
   */
  @Override
  public void step(double elapsedTime, double gameGravity) {
    setPosition(autoMove.stepMovement(elapsedTime, gameGravity, getPosition()));
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

  public void scaleVelocity(double xVel, double yVel) {
//    System.out.println("scale velo: "+scaleFactor.getX()+ " "+scaleFactor.getY());
    Vector newVelo = autoMove.getVelocity().multiply(new Vector(xVel, yVel));
    autoMove.setVelocity(newVelo);
  }
}

