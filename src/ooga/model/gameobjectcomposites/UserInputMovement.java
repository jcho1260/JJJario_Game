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
  private boolean isJumping;
  private int continuousJumps;
  private final int continuousJumpLimit;
  private double gravitySink;
  private Vector drivingVelocity;

  /**
   * Constructor for UserInputMovement.
   * jumpTime should be based of the animation frame stuff
   *
   * @param defaultVelocity per step
   */
  public UserInputMovement(double jumpTime, Vector defaultVelocity, double gravity,
      Vector autoscrollVector, int contJumpLimit) {
    jumpTimeLimit = jumpTime;
    stepVelocityMagnitude = defaultVelocity;
    gravityScale = gravity;
    jumpTimeCounter = 0;
    drivingVelocity = autoscrollVector;
    continuousJumpLimit = contJumpLimit;
  }

  private Vector decideJumping(Double elapsedTime, Double gameGravity, Vector change) {
    if (isJumping) {
      jumpTimeCounter += elapsedTime;
    }

    if (isJumping && jumpTimeCounter <= jumpTimeLimit) {
      return deltaPosition(elapsedTime, gameGravity, change.add(new Vector(0, -1)));
    } else {
      return deltaPosition(elapsedTime, gameGravity, change);
    }
  }

  /**
   * Returns vector of change in position as a result of NONE. Should fall.
   *
   * @param elapsedTime
   * @param gameGravity
   * @return deltaPosiiton
   */
  public Vector none(Double elapsedTime, Double gameGravity) {
    return decideJumping(elapsedTime, gameGravity, new Vector(0, 0));
  }

  /**
   * Returns vector of change in position as a result of UP. Once the jump has reached its peak,
   * it should hold briefly, then begin to fall.
   *
   * @param elapsedTime
   * @param gameGravity
   * @return deltaPosition
   */
  public Vector up(Double elapsedTime, Double gameGravity) {
    if (isJumping && continuousJumps <= continuousJumpLimit) {
      jumpTimeCounter = 0;
      continuousJumps++;
    } else {
      if (!isJumping) {
        isJumping = jumpTimeCounter == 0;
      }
    }

    return none(elapsedTime, gameGravity);
  }

  /**
   * Returns vector of change in position as a result of DOWN.
   *
   * @param elapsedTime
   * @param gameGravity
   * @return deltaPosition
   */
  public Vector down(Double elapsedTime, Double gameGravity) {
    isJumping = false;
    return deltaPosition(elapsedTime, gameGravity, new Vector(0, 1));
  }

  /**
   * Returns vector of change in position as a result of RIGHT.
   *
   * @param elapsedTime
   * @param gameGravity
   * @return deltaPosition
   */
  public Vector right(Double elapsedTime, Double gameGravity) {
    return decideJumping(elapsedTime, gameGravity, new Vector(1, 0));
  }

  /**
   * Returns vector of change in position as a result of LEFT.
   *
   * @param elapsedTime
   * @param gameGravity
   * @return deltaPosition
   */
  public Vector left(Double elapsedTime, Double gameGravity) {
    return decideJumping(elapsedTime, gameGravity, new Vector(-1, 0));
  }

  /**
   * Retrieves velocity.
   *
   * @return stepVelocityMagnitude
   */
  public Vector getVelocity() {
    return stepVelocityMagnitude;
  }

  /**
   * Sets stepVelocityMagnitude to new value.
   *
   * @param newVelocity
   */
  public void setVelocity(Vector newVelocity) {
    stepVelocityMagnitude = newVelocity;
  }

  // TODO refactor duplicate code w/ automatedmovement
  private Vector deltaPosition(double elapsedTime, double gameGravity, Vector change) {
    gravitySink = (1 + change.getY()) * elapsedTime * gameGravity * gravityScale;

    double newX = (elapsedTime * Math.abs(stepVelocityMagnitude.getX()) * change.getX())
        + (elapsedTime * drivingVelocity.getX());
    double newY = (elapsedTime * Math.abs(stepVelocityMagnitude.getY()) * change.getY())
        + (elapsedTime * drivingVelocity.getY()) + gravitySink;

    return new Vector(newX, newY);
  }

  /**
   * Player has landed on a jumpable object.
   */
  public void hitGround() {
    jumpTimeCounter = 0;
    continuousJumps = 0;
    isJumping = false;
  }
}
