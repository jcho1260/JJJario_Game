package ooga.model.gameobjectcomposites;

import ooga.model.util.Vector;

/**
 * Handles auto movement for moving GameObjects.
 *
 * @author Jessica Yang
 */
public class AutomatedMovement {

  private final Vector startPosition;
  private final Vector endPosition;
  private Vector velocity;
  private double gravityScale;

  /**
   * Default constructor for AutomatedMovement.
   *
   * @param initialPosition Vector
   * @param finalPosition Vector
   * @param initialVelocity Vector
   */
  public AutomatedMovement(Vector initialPosition, Vector finalPosition, Vector initialVelocity,
      double gravity) {
    startPosition = initialPosition;
    endPosition = finalPosition;
    velocity = initialVelocity;
    gravityScale = gravity;
  }

  /**
   * Calculated new position given starting position.
   *
   * @param elapsedTime double
   * @param gameGravity double
   * @param position Vector
   * @return newPosition Vector
   */
  public Vector stepMovement(double elapsedTime, double gameGravity, Vector position) {
    if (!isInPath(position)) {
      velocity = velocity.multiply(new Vector(-1, -1));
    }

    double newX = position.getX() + (elapsedTime * velocity.getX());
    double newY = position.getY() + (elapsedTime * velocity.getY())
        - (elapsedTime * gameGravity * gravityScale); // up = -, down = +

    return new Vector(newX, newY);
  }

  private boolean isInPath(Vector position) {
    boolean withinStartX = isStartLessThanEndX() == (position.getX() >= startPosition.getX());
    boolean withinStartY = isStartGreaterThanEndY() == (position.getY() <= startPosition.getY());
    boolean withinEndX = isStartLessThanEndX() == (position.getX() <= endPosition.getX());
    boolean withinEndY = isStartGreaterThanEndY() == (position.getY() >= endPosition.getY());
    return withinStartX && withinStartY && withinEndX && withinEndY;
  }

  private boolean isStartLessThanEndX() {
    return startPosition.getX() < endPosition.getX();
  }

  private boolean isStartGreaterThanEndY() {
    return startPosition.getY() > endPosition.getY();
  }

  /**
   * Returns velocity.
   *
   * @return velocity
   */
  public Vector getVelocity() {
    return velocity.copy();
  }

  /**
   * Sets velocity.
   *
   * @param newVelocity
   */
  public void setVelocity(Vector newVelocity) {
    velocity = newVelocity;
  }
}
