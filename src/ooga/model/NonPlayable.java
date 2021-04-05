package ooga.model;

import java.util.List;

/**
 * Represents NonPlayable objects that can be destroyed but not controlled by the user.
 *
 * @author Jessica Yang
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
  public NonPlayable(List<String> entityTypes, Vector initialPosition, Vector initialVelocity,
      double gravityScale, int id, Vector size, Vector finalPosition) {
    super(entityTypes, initialPosition, initialVelocity, gravityScale, id, size);
    startPosition = initialPosition;
    endPosition = finalPosition;
  }

  /**
   * Handles movement in a frame of time.
   *
   * @param elapsedTime
   * @param gameGravity
   */
  @Override
  public void stepMovement(double elapsedTime, double gameGravity) {
    if (!isInPath()) {
      Vector newVelocity = getVelocity().multiply(new Vector(-1, -1));
      setVelocity(newVelocity);
    }

    setPosition(movePosition(elapsedTime, gameGravity));
  }

  private boolean isInPath() {
    return (isStartLessThanEndX() == (getPosition().getX() >= startPosition.getX()))
        && (isStartLessThanEndY() == (getPosition().getY() >= startPosition.getY()))
        && (isStartLessThanEndX() == (getPosition().getX() <= endPosition.getX()))
        && (isStartLessThanEndY() == (getPosition().getY() <= endPosition.getY()));
  }

  private boolean isStartLessThanEndX() {
    return startPosition.getX() < endPosition.getX();
  }

  private boolean isStartLessThanEndY() {
    return startPosition.getY() < endPosition.getY();
  }

  private Vector movePosition(double elapsedTime, double gameGravity) {
    double newX;
    double newY;

    newX = getPosition().getX() + (elapsedTime * getVelocity().getX());
    newY = getPosition().getY() + (elapsedTime * getVelocity().getY())
        - (elapsedTime * gameGravity * getGravityScale());

    return new Vector(newX, newY);
  }
}