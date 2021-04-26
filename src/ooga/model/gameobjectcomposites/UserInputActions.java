package ooga.model.gameobjectcomposites;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import ooga.model.util.Vector;

public class UserInputActions implements Serializable {

  private Map<String, Integer> parameterCounts;
  private final UserInputMovement movement;
  private final double shootingDelay;
  private double lastShot;

  public UserInputActions(double jumpTime, Vector defaultVelocity, double gravity,
      Vector autoscrollVector, int contJumpLimit, double shootingCooldown) {
    movement = new UserInputMovement(jumpTime, defaultVelocity, gravity, autoscrollVector,
        contJumpLimit);
    shootingDelay = shootingCooldown;
    lastShot = shootingDelay * -1;
    createParameterCounts();
  }

  public boolean shoot(Double x, Double y, int currentFrame) {
    if (currentFrame >= lastShot + shootingDelay) {
      lastShot = currentFrame;
      return true;
    }
    return false;
  }

  public Vector up(Double elapsedTime, Double gameGravity) {
    return movement.up(elapsedTime, gameGravity);
  }

  public Vector down(Double elapsedTime, Double gameGravity) {
    return movement.down(elapsedTime, gameGravity);
  }

  public Vector left(Double elapsedTime, Double gameGravity) {
    return movement.left(elapsedTime, gameGravity);
  }

  public Vector right(Double elapsedTime, Double gameGravity) {
    return movement.right(elapsedTime, gameGravity);
  }

  public Vector none(Double elapsedTime, Double gameGravity) {
    return movement.none(elapsedTime, gameGravity);
  }

  public void hitGround() {
    movement.hitGround();
  }

  public Vector getVelocity() {
    return movement.getVelocity();
  }

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
