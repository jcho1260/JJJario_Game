package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import ooga.model.gameobjects.Player;
import ooga.model.util.Action;
import ooga.model.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests userinputmovement for player.
 *
 * @author Jessica Yang
 */
public class PlayerActionTest {

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
          1, 1, 2, velocityMagnitude, 1, new Vector(0, 0),
          0, 2, true, 0);
      user.addListener("test", new PropertyChangeListener() {
        @Override
        public void propertyChange(PropertyChangeEvent evt) {
          if (evt.getPropertyName().equals("newMovingDestroyable")) {
            assertTrue(evt.getPropertyName().equals("newMovingDestroyable"));
          }
        }
      });
    } catch (Exception ignored) {
    }
    assertNotNull(user);
  }

  /**
   * Simulates shooting.
   */
  @Test
  void shootTest() {
    try {
      user.userStep(Action.SHOOT, 1, 1, 0);
    } catch (Exception ignored) {
    }
  }

  /**
   * Simulates no input from the user.
   */
  @Test
  void moveNone() {
    try {
      user.userStep(Action.NONE, 1, 1, 0);
    } catch (Exception ignored) {
    }
    assertEquals(new Vector(0, 1), user.getPredictedPosition());
  }

  /**
   * Simulates rightward movement.
   */
  @Test
  void moveRight() {
    try {
      user.userStep(Action.RIGHT, 1, 1, 0);
    } catch (Exception ignored) {
    }
    assertEquals(new Vector(1, 1), user.getPredictedPosition());
  }

  /**
   * Simulates leftward movement.
   */
  @Test
  void moveLeft() {
    try {
      user.userStep(Action.LEFT, 1, 1, 0);
    } catch (Exception ignored) {
    }
    assertEquals(new Vector(-1, 1), user.getPredictedPosition());
  }

  /**
   * Simulates a single upward jump.
   */
  @Test
  void moveUp() {
    try {
      user.userStep(Action.UP, 1, 1, 0);
      assertEquals(new Vector(0, -1), user.getPredictedPosition());
      user.userStep(Action.NONE, 1, 1, 0);
      assertEquals(new Vector(0, -2), user.getPredictedPosition());
      user.userStep(Action.NONE, 1, 1, 0);
      assertEquals(new Vector(0, -1), user.getPredictedPosition());
      user.userStep(Action.NONE, 1, 1, 0);
      assertEquals(new Vector(0, 1), user.getPredictedPosition());
      user.userStep(Action.NONE, 1, 1, 0);
      assertEquals(new Vector(0, 4), user.getPredictedPosition());
    } catch (Exception ignored) {
    }
  }

  /**
   * Simulates downward movement.
   */
  @Test
  void moveDown() {
    try {
      user.userStep(Action.DOWN, 1, 1, 0);
    } catch (Exception ignored) {
    }
    assertEquals(new Vector(0, 1), user.getPredictedPosition());
  }

  /**
   * Simulates a user holding the up key - should prevent double jumping.
   */
  @Test
  void moveUpContinuous() {
    try {
      user.userStep(Action.UP, 1, 1, 0);
      assertEquals(new Vector(0, -1), user.getPredictedPosition());
      user.userStep(Action.UP, 1, 1, 0);
      assertEquals(new Vector(0, -2), user.getPredictedPosition());
      user.userStep(Action.UP, 1, 1, 0);
      assertEquals(new Vector(0, -3), user.getPredictedPosition());
      user.userStep(Action.UP, 1, 1, 0);
      assertEquals(new Vector(0, -1), user.getPredictedPosition());
      user.userStep(Action.UP, 1, 1, 0);
      assertEquals(new Vector(0, 2), user.getPredictedPosition());
    } catch (Exception ignored) {
    }
  }

  /**
   * Simulates a user holding the up key, then releasing, and re-pressing while in air. Should have
   * no change in behavior.
   */
  @Test
  void moveUpRelease() {
    try {
      user.userStep(Action.UP, 1, 1, 0);
      assertEquals(new Vector(0, -1), user.getPredictedPosition());
      user.userStep(Action.UP, 1, 1, 0);
      assertEquals(new Vector(0, -2), user.getPredictedPosition());
      user.userStep(Action.NONE, 1, 1, 0);
      assertEquals(new Vector(0, -3), user.getPredictedPosition());
      user.userStep(Action.NONE, 1, 1, 0);
      assertEquals(new Vector(0, -1), user.getPredictedPosition());
      user.userStep(Action.UP, 1, 1, 0);
      assertEquals(new Vector(0, 2), user.getPredictedPosition());
    } catch (Exception ignored) {
    }
  }

  /**
   * Simulates a user holding the up key, then falling and hitting a surface, and jumping again.
   */
  @Test
  void moveUpFallUpAgain() {
    try {
      user.userStep(Action.UP, 1, 1, 0);
      assertEquals(new Vector(0, -1), user.getPredictedPosition());
      user.userStep(Action.NONE, 1, 1, 0);
      assertEquals(new Vector(0, -2), user.getPredictedPosition());
      user.userStep(Action.NONE, 1, 1, 0);
      assertEquals(new Vector(0, -1), user.getPredictedPosition());
      user.userStep(Action.NONE, 1, 1, 0);
      assertEquals(new Vector(0, 1), user.getPredictedPosition());
      user.generalBottomCollision();
      user.userStep(Action.UP, 1, 1, 0);
      assertEquals(new Vector(0, 0), user.getPredictedPosition());
    } catch (Exception ignored) {
    }
  }

  /**
   * Simulates a user playing Flappy Bird. Should allow 2 jumps.
   */
  @Test
  void continuousJumping() {
    try {
      user = new Player(new ArrayList<>(), initPosition, 0, new Vector(1, 1),
          1, 1, 1, velocityMagnitude, 1, new Vector(0, 0),
          2, 2, true, 0);
    } catch (Exception ignored) {
    }
    assertNotNull(user);
    try {
      user.userStep(Action.UP, 1, 1, 0);
      assertEquals(new Vector(0, -1), user.getPredictedPosition());
      user.userStep(Action.UP, 1, 1, 0);
      assertEquals(new Vector(0, -2), user.getPredictedPosition());
      user.userStep(Action.UP, 1, 1, 0);
      assertEquals(new Vector(0, -3), user.getPredictedPosition());
      user.userStep(Action.UP, 1, 1, 0);
      assertEquals(new Vector(0, -4), user.getPredictedPosition());
      user.userStep(Action.UP, 1, 1, 0);
      assertEquals(new Vector(0, -2), user.getPredictedPosition());
      user.userStep(Action.UP, 1, 1, 0);
      assertEquals(new Vector(0, 1), user.getPredictedPosition());
    } catch (Exception ignored) {
    }
  }
}
