package ooga.model;

import java.util.ArrayList;
import ooga.model.gameobjects.Player;
import ooga.model.util.Action;
import ooga.model.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests userinputmovement for player.
 *
 * @author Jessica Yang
 */
public class PlayerMovementTest {
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
          1, 1, 2, velocityMagnitude, 1, new Vector(0, 0));
    } catch (ClassNotFoundException e) {
      System.out.println(e.getMessage());
    }
    assertNotNull(user);
  }

  /**
   * Simulates no input from the user.
   */
  @Test
  void moveNone() {
    try {
      user.userStepMovement(Action.NONE, 1, 1);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    assertTrue(user.getPredictedPosition().equals(new Vector(0, 1)));
  }

  /**
   * Simulates rightward movement.
   */
  @Test
  void moveRight() {
    try {
      user.userStepMovement(Action.RIGHT, 1, 1);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    assertTrue(user.getPredictedPosition().equals(new Vector(1, 1)));
  }

  /**
   * Simulates leftward movement.
   */
  @Test
  void moveLeft() {
    try {
      user.userStepMovement(Action.LEFT, 1, 1);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    assertTrue(user.getPredictedPosition().equals(new Vector(-1, 1)));
  }

  /**
   * Simulates a single upward jump.
   */
  @Test
  void moveUp() {
    try {
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -1)));
      user.userStepMovement(Action.NONE, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -2)));
      user.userStepMovement(Action.NONE, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -1)));
      user.userStepMovement(Action.NONE, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, 0)));
      user.userStepMovement(Action.NONE, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, 1)));
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Simulates downward movement.
   */
  @Test
  void moveDown() {
    try {
      user.userStepMovement(Action.DOWN, 1, 1);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
    assertTrue(user.getPredictedPosition().equals(new Vector(0, 0)));
  }

  /**
   * Simulates a user holding the up key - should prevent double jumping.
   */
  @Test
  void moveUpContinuous() {
    try {
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -1)));
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -2)));
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -1)));
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, 0)));
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, 1)));
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Simulates a user holding the up key, then releasing, and re-pressing while in air. Should have
   * no change in behavior.
   */
  @Test
  void moveUpRelease() {
    try {
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -1)));
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -2)));
      user.userStepMovement(Action.NONE, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -1)));
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, 0)));
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, 1)));
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Simulates a user holding the up key, then falling and hitting a surface, and jumping again.
   */
  @Test
  void moveUpFallUpAgain() {
    try {
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -1)));
      user.userStepMovement(Action.NONE, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -2)));
      user.userStepMovement(Action.NONE, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -1)));
      user.userStepMovement(Action.NONE, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, 0)));
      user.generalBottomCollision();
      user.userStepMovement(Action.UP, 1, 1);
      assertTrue(user.getPredictedPosition().equals(new Vector(0, -1)));
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }
}
