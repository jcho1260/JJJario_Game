package ooga.model.gameobjectcomposites;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import ooga.model.util.Vector;

/**
 * Handles Player actions, as described through the Action enum. Used by Player, and instatiates
 * UserInputMovement.
 *
 * @author Jessica Yang, Jin Cho, Juhyoung Lee
 */
public class UserInputActions implements Serializable {

  private Map<String, Integer> parameterCounts;
  private final UserInputMovement movement;
  private final double shootingDelay;
  private double lastShot;

  /**
   * Constructor for UserInputActions.
   *
   * @param jumpTime limit of how long a player can move upwards
   * @param defaultVelocity
   * @param gravity
   * @param autoscrollVector direction in which the player is automatically moved
   * @param contJumpLimit limit of how many times a player can jump without landing on another
   *                      GameObject
   * @param shootingCooldown limit of how long must pass between projectiles being created
   */
  public UserInputActions(double jumpTime, Vector defaultVelocity, double gravity,
      Vector autoscrollVector, int contJumpLimit, double shootingCooldown) {
    movement = new UserInputMovement(jumpTime, defaultVelocity, gravity, autoscrollVector,
        contJumpLimit);
    shootingDelay = shootingCooldown;
    lastShot = shootingDelay * -1;
    createParameterCounts();
  }

  /**
   * Checks if a projectile can be created based of the last time a projectile was created.
   *
   * @param x position of player
   * @param y position of player
   * @param currentFrame
   * @return if a projectile can be created
   */
  public boolean shoot(Double x, Double y, int currentFrame) {
    if (currentFrame >= lastShot + shootingDelay) {
      lastShot = currentFrame;
      return true;
    }
    return false;
  }

  /**
   * Returns vector of change in position as a result of UP. Once the jump has reached its peak, it
   * should hold briefly, then begin to fall.
   *
   * @param elapsedTime
   * @param gameGravity
   * @return deltaPosition
   */
  public Vector up(Double elapsedTime, Double gameGravity) {
    return movement.up(elapsedTime, gameGravity);
  }

  /**
   * Returns vector of change in position as a result of DOWN.
   *
   * @param elapsedTime
   * @param gameGravity
   * @return deltaPosition
   */
  public Vector down(Double elapsedTime, Double gameGravity) {
    return movement.down(elapsedTime, gameGravity);
  }

  /**
   * Returns vector of change in position as a result of LEFT.
   *
   * @param elapsedTime
   * @param gameGravity
   * @return deltaPosition
   */
  public Vector left(Double elapsedTime, Double gameGravity) {
    return movement.left(elapsedTime, gameGravity);
  }

  /**
   * Returns vector of change in position as a result of RIGHT.
   *
   * @param elapsedTime
   * @param gameGravity
   * @return deltaPosition
   */
  public Vector right(Double elapsedTime, Double gameGravity) {
    return movement.right(elapsedTime, gameGravity);
  }

  /**
   * Returns vector of change in position as a result of NONE. Should fall.
   *
   * @param elapsedTime
   * @param gameGravity
   * @return deltaPosiiton
   */
  public Vector none(Double elapsedTime, Double gameGravity) {
    return movement.none(elapsedTime, gameGravity);
  }

  /**
   * Player has landed on a jumpable object.
   */
  public void hitGround() {
    movement.hitGround();
  }

  /**
   * Retrieves velocity.
   *
   * @return stepVelocityMagnitude
   */
  public Vector getVelocity() {
    return movement.getVelocity();
  }

  /**
   * Sets stepVelocityMagnitude to new value.
   *
   * @param velocity
   */
  public void setVelocity(Vector velocity) {
    movement.setVelocity(velocity);
  }

  private void createParameterCounts() {
    parameterCounts = new HashMap<>();
    for (Method m : this.getClass().getMethods()) {
      parameterCounts.put(m.getName(), m.getParameterCount());
    }
  }
}
