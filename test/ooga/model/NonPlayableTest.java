package ooga.model;

import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NonPlayableTest {
  Vector start = new Vector(0, 0);
  Vector end = new Vector(5, 5);
  Vector velocity = new Vector(1, 1);
  NonPlayable npc;

  @BeforeEach
  public void init() {
    npc = new NonPlayable(new ArrayList<>(), start, velocity, 0, 0,
        new Vector(1, 1), end);
  }

  /**
   * NonPlayable can move from start to end while no turns are necessary.
   */
  @Test
  void movementNoTurn() {
    npc.stepMovement(1, 5);
    assertTrue(npc.getPosition().getX() == 1 && npc.getPosition().getY() == 1);
  }

  /**
   * NonPlayable can turn around once it passes the end position.
   */
  @Test
  void movementEndTurn() {
    npc.stepMovement(5, 5);
    assertTrue(npc.getPosition().getX() == 5 && npc.getPosition().getY() == 5);
    npc.stepMovement(1, 5);
    npc.stepMovement(1, 5);
    assertTrue(npc.getPosition().getX() == 5 && npc.getPosition().getY() == 5);
  }

  /**
   * NonPlayable can turn around once it passes the start position.
   */
  @Test
  void movementStartTurn() {
    npc.stepMovement(5, 5);
    npc.stepMovement(1, 5);
    npc.stepMovement(1, 5);
    assertTrue(npc.getPosition().getX() == 5 && npc.getPosition().getY() == 5);
    npc.stepMovement(5, 5);
    npc.stepMovement(1, 5);
    npc.stepMovement(1, 5);
    assertTrue(npc.getPosition().getX() == 0 && npc.getPosition().getY() == 0);
  }
}
