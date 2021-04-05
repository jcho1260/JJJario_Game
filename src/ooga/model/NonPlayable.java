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
      int id, Vector size, Vector finalPosition) {
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
      Vector newVelocity = getVelocity().multiply(new Vector(-1, -1));
      setVelocity(newVelocity);
    }

    setPosition(movePosition(elapsedTime));
  }

  private boolean isPastStart() {
    return (isStartLessThanEndX() == getPosition().getX() < startPosition.getX())
        && (isStartLessThanEndY() == getPosition().getY() < startPosition.getY());
  }

  private boolean isPastEnd() {
    return (isStartLessThanEndX() == getPosition().getX() > endPosition.getX())
        && (isStartLessThanEndY() == getPosition().getY() > endPosition.getY());
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

    newX = getPosition().getX() + (elapsedTime * getVelocity().getX());
    newY = getPosition().getY() + (elapsedTime * getVelocity().getY());

    return new Vector(newX, newY);
  }
}