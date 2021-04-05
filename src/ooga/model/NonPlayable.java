package ooga.model;

import java.util.List;

/**
 *
 */
public class NonPlayable extends Actor {

  private final Vector startPosition;
  private final Vector endPosition;

  /**
   * Default constructor for NonPlayable. If NonPlayable moves indefinitely in a direction,
   * finalPosition should be the boundary of the game.
   *
   * @param initialVelocity
   * @param initialPosition
   * @param finalPosition
   */
  public NonPlayable(List<String> entityTypes, Vector initialPosition, Vector initialVelocity, double gravity,
      int id, double size, Vector finalPosition) {
    super(entityTypes, initialPosition, initialVelocity, gravity, id, size);
    startPosition = initialPosition;
    endPosition = finalPosition;
  }

  /**
   * Handles movement in a frame of time.
   *
   * @param elapsedTime
   */
  public void stepMovement(double elapsedTime) {
    if (isPastStart() && isPastEnd()) {
      velocity = velocity.multiply(new Vector(-1, -1));
    }

    position = movePosition(elapsedTime);
  }

  private boolean isPastStart() {
    return (isStartLessThanEndX() == position.getX() < startPosition.getX())
        && (isStartLessThanEndY() == position.getY() < startPosition.getY());
  }

  private boolean isPastEnd() {
    return (isStartLessThanEndX() == position.getX() > endPosition.getX())
        && (isStartLessThanEndY() == position.getY() > endPosition.getY());
  }

  private boolean isStartLessThanEndX() {
    return startPosition.getX() < endPosition.getX();
  }

  private boolean isStartLessThanEndY() {
    return startPosition.getY() < endPosition.getY();
  }

  private Vector movePosition(double elapsedTime) {
    double newX;
    double newY;

    newX = position.getX() + (elapsedTime * velocity.getX());
    newY = position.getY() + (elapsedTime * velocity.getY());

    return new Vector(newX, newY);
  }
}