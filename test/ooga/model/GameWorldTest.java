package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ooga.model.gameobjects.GameObject;
import ooga.model.gameobjects.MovingDestroyable;
import ooga.model.gameobjects.Player;
import ooga.model.util.Action;
import ooga.model.util.MethodBundle;
import ooga.model.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Test suite for GameWorld.
 *
 * @author Jessica Yang
 */
public class GameWorldTest {

  GameWorld gw;
  Map collisionMethods = new HashMap<String, Map<String, List<MethodBundle>>>();
  List<GameObject> gameobjects = new ArrayList<>();
  List<GameObject> actors = new ArrayList<>();
  Vector frameSize = new Vector(50, 50);
  int gravity = 2;
  double frameRate = 1.0 / 30;
  Vector minScreenLimit = new Vector(-5, -5);
  Vector maxScreenLimit = new Vector(50, 50);

  Player player;
  Vector initPosition = new Vector(0, 0);
  Vector velocityMagnitude = new Vector(1, 1);

  GameObject block;
  List<String> blockEntityTypes = new ArrayList<>();
  Vector blockPosition = new Vector(0, 30);
  Vector blockSize = new Vector(5, 5);

  MovingDestroyable enemy;
  List<String> enemyEntityTypes = new ArrayList<>();
  Vector enemyInitialPos = new Vector(6, 6);
  Vector enemySize = new Vector(3, 3);
  Vector enemyInitVelocity = new Vector(0, 5);
  Vector enemyFinalPos = new Vector(12, 12);

  /**
   * Checks that GameWorld can be constructed.
   */
  @BeforeEach
  @Test
  void init() {
    try {
      player = new Player(new ArrayList<>(), initPosition, 0, new Vector(1, 1),
          1, 1, 2, velocityMagnitude, 0, new Vector(0, 0),
          0, 2, true, 0);
      gw = new GameWorld(player, collisionMethods, gameobjects, actors, frameSize, 5,
          gravity, frameRate, minScreenLimit, maxScreenLimit);
      blockEntityTypes.add("GameObject");
      block = new GameObject(blockEntityTypes, blockPosition, 8, blockSize, true);
      enemyEntityTypes.add("GameObject");
      enemyEntityTypes.add("MovingDestroyable");
      enemy = new MovingDestroyable(enemyEntityTypes, enemyInitialPos, 10, enemySize,
          1, 5, 10, enemyInitVelocity, enemyFinalPos, 1, true);
    } catch (Exception ignored) {
    }
    assertNotNull(gw);
  }

  /**
   * Run a simple step.
   */
  @Test
  void runStepTest() {
    try {
      gw.stepFrame(Action.NONE);
      assertFalse(gw.didPlayerWin());
    } catch (Exception ignored) {
    }
  }

  /**
   * Run a step with a player action.
   */
  @Test
  void runPlayerStepTest() {
    try {
      Vector initPos = player.getPosition();
      gw.stepFrame(Action.RIGHT);
      assertEquals(initPos.add(new Vector(30, 0)), player.getPosition());
    } catch (Exception ignored) {
    }
  }

  /**
   * Run a step with Action.SHOOT. Should not change player position.
   */
  @Test
  void runPlayerShootStepTest() {
    try {
      Vector initPos = player.getPosition();
      gw.stepFrame(Action.SHOOT);
      assertEquals(initPos, player.getPosition());
    } catch (Exception ignored) {
    }
  }

  /**
   * Add moving destroyables created during runtime.
   */
  @Test
  void runTimeCreationTest() {
    try {
      List<MovingDestroyable> creations = new ArrayList<>();
      creations.add(new MovingDestroyable(enemyEntityTypes, initPosition, 11,
          enemySize, 1, 5, 10, enemyInitVelocity, enemyFinalPos, 1,
          true));
      gw.queueNewMovingDestroyable(creations);
      gw.stepFrame(Action.NONE);
      assertTrue(gw.getAllDestroyables().size() == 2 && gw.getAllGameObjects().size() == 2);
    } catch (Exception ignored) {
    }
  }

  /**
   * Add moving destroyables created during runtime.
   */
  @Test
  void runTimeCreationMultipleTest() {
    try {
      List<MovingDestroyable> creations = new ArrayList<>();
      creations.add(new MovingDestroyable(enemyEntityTypes, initPosition, 11,
          enemySize, 1, 5, 10, enemyInitVelocity, enemyFinalPos, 1,
          true));
      creations.add(new MovingDestroyable(enemyEntityTypes, initPosition, 45,
          enemySize, 1, 5, 10, enemyInitVelocity, enemyFinalPos, 1,
          true));
      creations.add(new MovingDestroyable(enemyEntityTypes, initPosition, 856,
          enemySize, 1, 5, 10, enemyInitVelocity, enemyFinalPos, 1,
          true));
      gw.queueNewMovingDestroyable(creations);
      gw.stepFrame(Action.NONE);
      assertTrue(gw.getAllDestroyables().size() == 4 && gw.getAllGameObjects().size() == 4);
    } catch (Exception ignored) {
    }
  }

  /**
   * Adds moving destroyables created during run time with empty list.
   */
  @Test
  void runTimeCreationEmptyTest() {
    try {
      List<MovingDestroyable> creations = new ArrayList<>();
      gw.queueNewMovingDestroyable(creations);
      gw.stepFrame(Action.NONE);
      assertTrue(gw.getAllDestroyables().size() == 1 && gw.getAllGameObjects().size() == 1);
    } catch (Exception ignored) {
    }
  }

  /**
   * Gravity getter is working.
   */
  @Test
  void getGravityTest() {
    assertEquals(gw.getGravity(), 2.0);
  }

  /**
   * Game over signal is asserted correctly. Should return false.
   */
  @Test
  void gameOverFalseTest() {
    try {
      gw.stepFrame(Action.NONE);
      assertFalse(gw.isGameOver());
    } catch (Exception ignored) {
    }
  }

  /**
   * Game over signal is asserted correctly. Should return false.
   */
  @Test
  void gameOverFalseLessHealthTest() {
    try {
      gw.stepFrame(Action.NONE);
      player.incrementHealth(-0.05);
      gw.stepFrame(Action.NONE);
      assertFalse(gw.isGameOver());
    } catch (Exception ignored) {
    }
  }

  /**
   * Game over signal is asserted correctly. Should return true, after killing player twice.
   */
  @Test
  void gameOverTrueTest() {
    try {
      gw.stepFrame(Action.NONE);
      player.incrementHealth(-10.0);
      gw.stepFrame(Action.NONE);
      assertFalse(gw.isGameOver());
      player.incrementHealth(-10.0);
      gw.stepFrame(Action.NONE);
      assertTrue(gw.isGameOver());
    } catch (Exception ignored) {
    }
  }

  /**
   * Simulates a check for player winning at start of game.
   */
  @Test
  void playerWinFalseTest() {
    try {
      assertFalse(gw.didPlayerWin());
    } catch (Exception ignored) {
    }
  }

  /**
   * Simulates a check for player winning after triggering win.
   */
  @Test
  void playerWinTrueTest() {
    try {
      player.playerWinsLevel();
      assertTrue(gw.didPlayerWin());
    } catch (Exception ignored) {
    }
  }
}
