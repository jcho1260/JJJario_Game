package ooga.model;

public class PhysicsEngine {
  private final double defaultGravity;

  /**
   *
   * @param gravity
   */
  public PhysicsEngine(double gravity) {
    defaultGravity = gravity;
  }

  /**
   * Returns new Vector describing new position after applying given parameters.
   *
   * @param mass
   * @param gravity
   * @param velocity
   * @param position
   * @param acceleration
   * @return
   */
  public Vector move(double mass, double gravityScale, Vector velocity, Vector position, Vector acceleration) {
    double newXVal, newYVal;

    newXVal = calculateCoordinate(double);
    newYVal = calculateCoordinate(double);
  }

  private double calculateCoordinate(double m)
}
