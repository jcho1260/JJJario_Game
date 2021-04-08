package ooga.model;

import ooga.model.util.Vector;

public class PhysicsEngine {
  private final double gravity;
  private final double stepTime;

  /**
   * Constructor for the PhysicsEngine.
   *
   */
  public PhysicsEngine(double gravity, double stepTime) {
    this.gravity = gravity;
    this.stepTime = stepTime;
  }

  /**
   * Returns new Vector describing new position after applying given parameters.
   *
   * @param gravityScale measure of how much gravity will affect an object
   * @param velocity
   * @param position
   * @param acceleration
   * @return
   */
  public Vector move(double gravityScale, Vector velocity, Vector position, Vector acceleration) {
    double newPositionX, newPositionY;
    double adjustedYAccel = acceleration.getY() - gravityScale * gravity;

    newPositionX = calculateKinematicPosition(position.getX(), velocity.getX(), acceleration.getX());
    newPositionY = calculateKinematicPosition(position.getY(), velocity.getY(), adjustedYAccel);

    return new Vector(newPositionX, newPositionY);
  }

  private double calculateKinematicPosition(double initialPosition, double velocity, double acceleration) {
    return initialPosition + (velocity * stepTime) + (0.5 * acceleration * stepTime * stepTime);
  }

  public Vector collisionMovement(double selfMass, double collideMass, Vector selfVelocity, Vector collideVelocity) {
    double newVelocityX, newVelocityY;

    newVelocityX = calculateMomentumVelocity(selfMass, collideMass, selfVelocity.getX(), collideVelocity.getX());
    newVelocityY = calculateMomentumVelocity(selfMass, collideMass, selfVelocity.getY(), collideVelocity.getY());

    return new Vector(newVelocityX, newVelocityY);
  }

  /**
   * Returns velocity of mass A after a collision with mass B. Assumes final velocity of B is 0.
   */
  private double calculateMomentumVelocity(double massA, double massB, double velocityA, double velocityB) {
    double initialMomentum = massA * velocityA + massB * velocityB;
    return initialMomentum / massA;
  }
}
