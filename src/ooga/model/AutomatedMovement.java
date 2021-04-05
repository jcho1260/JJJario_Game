package ooga.model;

/**
 * Handles automovement for moving GameObjects.
 *
 * @author Jessica Yang
 */
public class AutomatedMovement {

  private final Vector startPosition;
  private final Vector endPosition;
  private Vector velocity;

  /**
   * Default constructor for AutomatedMovement.
   */
  public AutomatedMovement(Vector initialPosition, Vector finalPosition, Vector initialVelocity) {
    startPosition = initialPosition;
    endPosition = finalPosition;
    velocity = initialVelocity;
  }

  /**
   * Calculated new position given starting position.
   *
   * @param elapsedTime double
   * @param gameGravity double
   * @param position Vector
   * @return newPosition Vector
   */
  public Vector stepMovement(double elapsedTime, double gameGravity, Vector position,
      double gravityScale) {
    if (!isInPath(position)) {
      velocity = velocity.multiply(new Vector(-1, -1));
    }

    double newX = position.getX() + (elapsedTime * position.getX());
    double newY = position.getY() + (elapsedTime * position.getY())
        - (elapsedTime * gameGravity * gravityScale);

    return new Vector(newX, newY);
  }

  private boolean isInPath(Vector position) {
    return (isStartLessThanEndX() == (position.getX() >= startPosition.getX()))
        && (isStartLessThanEndY() == (position.getY() >= startPosition.getY()))
        && (isStartLessThanEndX() == (position.getX() <= endPosition.getX()))
        && (isStartLessThanEndY() == (position.getY() <= endPosition.getY()));
  }

  private boolean isStartLessThanEndX() {
    return startPosition.getX() < endPosition.getX();
  }

  private boolean isStartLessThanEndY() {
    return startPosition.getY() < endPosition.getY();
  }
}
