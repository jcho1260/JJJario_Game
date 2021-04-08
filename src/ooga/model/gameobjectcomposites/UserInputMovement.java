package ooga.model.gameobjectcomposites;

import ooga.model.util.Vector;

/**
 * Handles movement based on user input for Player. Invoked using reflection.
 *
 * @author Jessica Yang
 */
public class UserInputMovement {

  private final double jumpTimeLimit;
  private Vector stepVelocityMagnitude;
  private double gravityScale;
  private double jumpTimeCounter;
  private double clockTime;
  private double gravityLevel;

  /**
   * Constructor for UserInputMovement.
   * jumpTime should be based of the animation frame stuff TODO ask noah about it
   *
   * @param defaultVelocity per step
   */
  public UserInputMovement(double jumpTime, Vector defaultVelocity, double gravity) {
    jumpTimeLimit = jumpTime;
    stepVelocityMagnitude = defaultVelocity;
    gravityScale = gravity;
    jumpTimeCounter = 0;
  }

  /**
   * Returns vector of change in position as a result of NONE. Should fall.
   *
   * @param elapsedTime
   * @param gameGravity
   * @return deltaPosiiton
   */
  public Vector moveNONE(Double elapsedTime, Double gameGravity) {
    return deltaPosition(elapsedTime, gameGravity, new Vector(0, 0));
  }

  /**
   * Returns vector of change in position as a result of UP. Once the jump has reached its peak,
   * it should hold briefly, then begin to fall.
   *
   * @param elapsedTime
   * @param gameGravity
   * @return deltaPosition
   */
  public Vector moveUP(Double elapsedTime, Double gameGravity) {
    jumpTimeCounter += elapsedTime;
    if (jumpTimeCounter <= jumpTimeLimit) {
      return deltaPosition(elapsedTime, gameGravity, new Vector(0, -1));
    } else {
      return moveNONE(elapsedTime, gameGravity);
    }
  }

  /**
   * Returns vector of change in position as a result of DOWN.
   *
   * @param elapsedTime
   * @param gameGravity
   * @return deltaPosition
   */
  public Vector moveDOWN(Double elapsedTime, Double gameGravity) {
    return deltaPosition(elapsedTime, gameGravity, new Vector(0, 1));
  }

  /**
   * Returns vector of change in position as a result of RIGHT.
   *
   * @param elapsedTime
   * @param gameGravity
   * @return deltaPosition
   */
  public Vector moveRIGHT(Double elapsedTime, Double gameGravity) {
    return deltaPosition(elapsedTime, gameGravity, new Vector(1, 0));
  }

  /**
   * Returns vector of change in position as a result of LEFT.
   *
   * @param elapsedTime
   * @param gameGravity
   * @return deltaPosition
   */
  public Vector moveLEFT(Double elapsedTime, Double gameGravity) {
    return deltaPosition(elapsedTime, gameGravity, new Vector(-1, 0));
  }

  // TODO refactor duplicate code w/ automatedmovement
  private Vector deltaPosition(double elapsedTime, double gameGravity, Vector change) {
    clockTime = elapsedTime;
    gravityLevel = gameGravity;

    double newX = elapsedTime * stepVelocityMagnitude.getX() * change.getX();
    double newY = (elapsedTime * stepVelocityMagnitude.getY() * change.getY())
        + ((1 + change.getY()) * elapsedTime * gameGravity * gravityScale);

    return new Vector(newX, newY);
  }

  /**
   * Player has landed on a jumpable object.
   *
   * @return deltaPosition to un-sink
   */
  public Vector hitGround() {
    jumpTimeCounter = 0;
    return deltaPosition(clockTime, gravityLevel, new Vector(0, -1));
  }
}
