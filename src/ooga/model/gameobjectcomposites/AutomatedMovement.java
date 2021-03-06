package ooga.model.gameobjectcomposites;

import java.io.Serializable;
import ooga.model.util.Vector;

/**
 * Handles auto movement for moving GameObjects. Added to GameObjects that require automated
 * movement.
 *
 * @author Jessica Yang
 */
public class AutomatedMovement implements Serializable {

  private final Vector startPosition;
  private final Vector endPosition;
  private Vector velocity;
  private final double gravityScale;
  private final boolean isStartLessThanEndX;
  private final boolean isStartGreaterThanEndY;

  /**
   * Default constructor for AutomatedMovement.
   *
   * @param initialPosition starting location
   * @param finalPosition   ending location
   * @param initialVelocity velocity
   * @param gravity gravity
   */
  public AutomatedMovement(Vector initialPosition, Vector finalPosition, Vector initialVelocity,
      double gravity) {
    startPosition = initialPosition;
    endPosition = finalPosition;
    velocity = initialVelocity;
    gravityScale = gravity;
    isStartLessThanEndX = startPosition.getX() < endPosition.getX();
    isStartGreaterThanEndY = startPosition.getY() > endPosition.getY();
  }

  /**
   * Calculated new position given game information.
   *
   * @param elapsedTime frame duration
   * @param gameGravity gravity
   * @param position    location
   * @return newPosition new location
   */
  public Vector stepMovement(double elapsedTime, double gameGravity, Vector position) {
    if (!isInPath(position)) {
      velocity = velocity.multiply(new Vector(-1, -1));
    }

    double newX = position.getX() + (elapsedTime * velocity.getX());
    double newY = position.getY() + (elapsedTime * velocity.getY())
        + (elapsedTime * gameGravity * gravityScale);

    return new Vector(newX, newY);
  }

  private boolean isInPath(Vector position) {
    boolean withinStartX = isStartLessThanEndX == (position.getX() > startPosition.getX());
    boolean withinStartY = isStartGreaterThanEndY == (position.getY() < startPosition.getY());
    boolean withinEndX = isStartLessThanEndX == (position.getX() < endPosition.getX());
    boolean withinEndY = isStartGreaterThanEndY == (position.getY() > endPosition.getY());
    boolean isStart = position.subtract(startPosition).calculateMagnitude() < 1;
    boolean isEnd = position.subtract(endPosition).calculateMagnitude() < 1;
    return (withinStartX && withinStartY && withinEndX && withinEndY) || isStart || isEnd;
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
