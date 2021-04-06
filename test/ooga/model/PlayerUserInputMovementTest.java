package ooga.model;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerUserInputMovementTest {
  Vector initPosition = new Vector(0, 0);
  Vector velocityMagnitude = new Vector(1, 1);

  Player user;

  /**
   * Checks that Player can be constructed.
   */
  @BeforeEach
  @Test
  public void init() {
    try {
      user = new Player(new ArrayList<>(), initPosition, 0, new Vector(1, 1),
      1, 1, velocityMagnitude, 1);
    } catch (ClassNotFoundException e) {
      System.out.println(e.getMessage());
    }
    assertNotNull(user);
  }

  /**
   * Simulates rightward movement.
   */
  @Test
  void moveRight() {
    try {
      user.userStepMovement(Action.RIGHT, 1, 1, 1);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    assertTrue(user.getPosition().getX() == 1 && user.getPosition().getY() == -1);
  }

}
