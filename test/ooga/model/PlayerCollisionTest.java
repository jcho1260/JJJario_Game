package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import ooga.model.gameobjects.Player;
import ooga.model.util.Action;
import ooga.model.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerCollisionTest {
  Player player;
  @BeforeEach
  @Test
  public void init() {
    List<String> entityTags = new ArrayList<>(){
      {
        add("GameObject");
        add("MovingDestroyable");
        add("Player");
      }
    };
    Vector zeros = new Vector(0,0);
    Vector ones = new Vector(1,1);
    try {
      player = new Player(entityTags, zeros, 0, ones, 0, 1, 0.75,
          ones, 1, zeros, 1, 30, true, 1);
    } catch (ClassNotFoundException e) {
      System.out.println(e.getMessage());
    }
    assertNotNull(player);
  }

  @Test
  void testUserStepShoot() {
    try {
      player.userStep(Action.SHOOT, 1, 25, 0);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }

    //TODO: how to check if new bullet was made?
  }

  @Test
  void testGeneralBottomCollision() {

  }

  @Test
  void testGetVelocity() {

  }

  @Test
  void testPlayerWin() {

  }

  @Test
  void testIncrementHealth() {

  }

  @Test
  void testIncrementLives() {

  }

  @Test
  void testScaleVelocity() {

  }

  @Test
  void testScaleSize() {

  }
}


