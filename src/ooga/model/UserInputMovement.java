package ooga.model;

/**
 * Handles movement based on user input for Player. Invoked using reflection.
 */
public class UserInputMovement {

  private Vector velocityMagnitude;
  private double gravityScale;

  /**
   * Constructor for UserInputMovement.
   *
   * @param defaultVelocity per step
   */
  public UserInputMovement(Vector defaultVelocity, double gravity) {
    velocityMagnitude = defaultVelocity;
    gravityScale = gravity;
  }

  public Vector moveUP(Double elapsedTime, Double gameGravity) {
    return deltaPosition(elapsedTime, gameGravity, new Vector(0, -1));
    // TODO limit how long player can effectively jump
  }

  public Vector moveDOWN(Double elapsedTime, Double gameGravity) {
    return deltaPosition(elapsedTime, gameGravity, new Vector(0, 1));
  }

  public Vector moveRIGHT(Double elapsedTime, Double gameGravity) {
    return deltaPosition(elapsedTime, gameGravity, new Vector(1, 0));
  }

  public Vector moveLEFT(Double elapsedTime, Double gameGravity) {
    return deltaPosition(elapsedTime, gameGravity, new Vector(-1, 0));
  }

  private Vector deltaPosition(double elapsedTime, double gameGravity, Vector change) {
    double newX = elapsedTime * velocityMagnitude.getX() * change.getX();
    double newY = (elapsedTime * velocityMagnitude.getY() * change.getY())
        + ((Math.abs(change.getY()) - 1) * elapsedTime * gameGravity * gravityScale);
      // TODO make sure that gameobjects/player can't sink into the ground - actually when are we using DOWN lol

    return new Vector(newX, newY);
  }
}
