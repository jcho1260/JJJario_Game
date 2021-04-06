package ooga.model;

/**
 * Handles movement based on user input for Player. Invoked using reflection.
 */
public class UserInputMovement {

  // TODO make elapsed time, game gravity a final?
  private Vector velocity;

  /**
   * Constructor for UserInputMovement.
   *
   * @param defaultVelocity per step
   */
  public UserInputMovement(Vector defaultVelocity) {
    velocity = defaultVelocity;
  }

  public Vector moveUP(Double elapsedTime, Double gameGravity, Double gravityScale) {
    return deltaPosition(elapsedTime, gameGravity, gravityScale, new Vector(0, -1));
    // TODO limit how long player can effectively jump
  }

  public Vector moveDOWN(Double elapsedTime, Double gameGravity, Double gravityScale) {
    return deltaPosition(elapsedTime, gameGravity, gravityScale, new Vector(0, 1));
  }

  public Vector moveRIGHT(Double elapsedTime, Double gameGravity, Double gravityScale) {
    return deltaPosition(elapsedTime, gameGravity, gravityScale, new Vector(1, 0));
  }

  public Vector moveLEFT(Double elapsedTime, Double gameGravity, Double gravityScale) {
    return deltaPosition(elapsedTime, gameGravity, gravityScale, new Vector(-1, 0));
  }

  private Vector deltaPosition(double elapsedTime, double gameGravity, double gravityScale, Vector change) {
    double newX = elapsedTime * velocity.getX() * change.getX();
    double newY = (elapsedTime * velocity.getY() * change.getY())
        + ((Math.abs(change.getY()) - 1) * elapsedTime * gameGravity * gravityScale);
      // TODO make sure that gameobjects/player can't sink into the ground - actually when are we using DOWN lol

    return new Vector(newX, newY);
  }
}
