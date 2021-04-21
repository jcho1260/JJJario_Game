package ooga.model;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import ooga.model.gameobjects.GameObject;
import ooga.model.gameobjects.MovingDestroyable;
import ooga.model.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class MovingDestroyableMovementTest {
  Vector start = new Vector(0, 0);
  Vector end = new Vector(5, -5);
  Vector velocity = new Vector(1, -1);
  GameObject npc;
  GameObject npcReversed;
  int changes;

  @BeforeEach
  public void init() {
    npc = new MovingDestroyable(new ArrayList<>(), start, 1, new Vector(1, 1),
    3, 3, 5, velocity, end, 0, true);
    npcReversed = new MovingDestroyable(new ArrayList<>(), end, 1, new Vector(1, 1),
        3, 3,5,  velocity.multiply(new Vector(-1, -1)), start, 0, true);
    PropertyChangeListener standIn = evt -> {changes++;};
    npc.addListener(standIn);
  }

  /**
   * NonPlayable can move from start to end while no turns are necessary.
   */
  @Test
  void movementNoTurn() {
    npc.step(1, 5);
    assertTrue(npc.getPredictedPosition().getX() == 1 && npc.getPredictedPosition().getY() == -1);
  }

  /**
   * Reversed start/end NonPlayable can move from start to end while no turns are necessary.
   */
  @Test
  void movementNoTurnReverse() {
    npcReversed.step(1, 5);
    assertTrue(npcReversed.getPredictedPosition().getX() == 4 && npcReversed.getPredictedPosition().getY() == -4);
  }

  /**
   * NonPlayable can turn around once it passes the end position.
   */
  @Test
  void movementEndTurn() {
    npc.step(5, 5);
    assertTrue(npc.getPredictedPosition().getX() == 5 && npc.getPredictedPosition().getY() == -5);
    npc.step(1, 5);
    assertFalse(npc.getPredictedPosition().getX() == 5 && npc.getPredictedPosition().getY() == -5);
    npc.step(1, 5);
    assertFalse(npc.getPredictedPosition().getX() == 4 && npc.getPredictedPosition().getY() == -4);
  }

  /**
   * Reversed start/end NonPlayable can turn around once it passes the end position.
   */
  @Test
  void movementEndTurnReverse() {
    npcReversed.step(5, 5);
    assertTrue(npcReversed.getPredictedPosition().getX() == 0 && npcReversed.getPredictedPosition().getY() == 0);
    npcReversed.step(1, 5);
    assertFalse(npcReversed.getPredictedPosition().getX() == 0 && npcReversed.getPredictedPosition().getY() == 0);
    npcReversed.step(1, 5);
    assertFalse(npcReversed.getPredictedPosition().getX() == 1 && npcReversed.getPredictedPosition().getY() == -1);
  }

  /**
   * NonPlayable can turn around once it passes the start position.
   */
  @Test
  void movementStartTurn() {
    npc.step(5, 5);
    npc.step(1, 5);
    assertFalse(npc.getPredictedPosition().getX() == 5 && npc.getPredictedPosition().getY() == -5);
    npc.step(5, 5);
    npc.step(1, 5);
    npc.step(1, 5);
    assertFalse(npc.getPredictedPosition().getX() == 0 && npc.getPredictedPosition().getY() == 0);
    npc.step(1, 5);
    assertFalse(npc.getPredictedPosition().getX() == 1 && npc.getPredictedPosition().getY() == -1);
  }

  /**
   * Reversed start/end NonPlayable can turn around once it passes the start position.
   */
  @Test
  void movementStartTurnReverse() {
    npcReversed.step(5, 5);
    npcReversed.step(1, 5);
    assertFalse(npcReversed.getPredictedPosition().getX() == 0 && npcReversed.getPredictedPosition().getY() == 0);
    npcReversed.step(5, 5);
    npcReversed.step(1, 5);
    npcReversed.step(1, 5);
    assertFalse(npcReversed.getPredictedPosition().getX() == 5 && npcReversed.getPredictedPosition().getY() == -5);
    npcReversed.step(1, 5);
    assertFalse(npcReversed.getPredictedPosition().getX() == 4 && npcReversed.getPredictedPosition().getY() == -4);
  }
}
