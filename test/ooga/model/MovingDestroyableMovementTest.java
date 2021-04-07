package ooga.model;

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

  @BeforeEach
  public void init() {
    npc = new MovingDestroyable(new ArrayList<>(), start, 1, new Vector(1, 1),
    3, 3, velocity, end, 0);
  }

  /**
   * NonPlayable can move from start to end while no turns are necessary.
   */
  @Test
  void movementNoTurn() {
    npc.step(1, 5);
    assertTrue(npc.getPosition().getX() == 1 && npc.getPosition().getY() == -1);
  }

  /**
   * NonPlayable can turn around once it passes the end position.
   */
  @Test
  void movementEndTurn() {
    npc.step(5, 5);
    assertTrue(npc.getPosition().getX() == 5 && npc.getPosition().getY() == -5);
    npc.step(1, 5);
    npc.step(1, 5);
    assertTrue(npc.getPosition().getX() == 5 && npc.getPosition().getY() == -5);
    npc.step(1, 5);
    assertTrue(npc.getPosition().getX() == 4 && npc.getPosition().getY() == -4);
  }

  /**
   * NonPlayable can turn around once it passes the start position.
   */
  @Test
  void movementStartTurn() {
    npc.step(5, 5);
    npc.step(1, 5);
    npc.step(1, 5);
    assertTrue(npc.getPosition().getX() == 5 && npc.getPosition().getY() == -5);
    npc.step(5, 5);
    npc.step(1, 5);
    npc.step(1, 5);
    assertTrue(npc.getPosition().getX() == 0 && npc.getPosition().getY() == 0);
    npc.step(1, 5);
    assertTrue(npc.getPosition().getX() == 1 && npc.getPosition().getY() == -1);
  }
}
