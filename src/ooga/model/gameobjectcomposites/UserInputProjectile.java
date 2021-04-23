package ooga.model.gameobjectcomposites;

import java.io.Serializable;
import ooga.model.util.Vector;

public class UserInputProjectile implements Serializable {

  private double shootingCooldown;
  private double startTime;

  public UserInputProjectile (double shootingCooldown) {
    this.shootingCooldown = shootingCooldown;
    this.startTime = startTime;
  }

  private void createSingleDestroyable(Vector position, Vector velocity) {
    // notifies~!
  }
}
