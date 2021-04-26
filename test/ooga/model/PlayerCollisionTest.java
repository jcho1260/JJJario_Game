package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import ooga.model.gameobjects.Destroyable;
import ooga.model.gameobjects.Player;
import ooga.model.util.MethodBundle;
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
  void testGetVelocity() throws ClassNotFoundException {
    Player p = createPlayer();
    assertEquals(new Vector(50, 0), p.getVelocity());
  }

  @Test
  void testPlayerWin() throws ClassNotFoundException {
    Player p = createPlayer();
    p.playerWinsLevel();
    assertEquals(true, p.getWinStatus());
  }

  @Test
  void testPlayerKill()
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Player p = createPlayer();
    p.setPredictedPosition(new Vector(50, 50));
    p.kill();
    Method healthCheck = Destroyable.class.getDeclaredMethod("getLives");
    healthCheck.setAccessible(true);
    assertEquals(0, (double) healthCheck.invoke(p));
    assertEquals(new Vector(0, 0), p.getPredictedPosition());
  }

  @Test
  void testRespawnAndHealth()
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Player p = createPlayer();
    addListenerPlayer(p);
    p.setPredictedPosition(new Vector(50, 50));
    p.incrementHealth(-1.0);
    Method healthCheck = Destroyable.class.getDeclaredMethod("getLives");
    healthCheck.setAccessible(true);
    assertEquals(0, (double) healthCheck.invoke(p));
    assertEquals(new Vector(0, 0), p.getPredictedPosition());
  }

  @Test
  void testIncrementLives()
      throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Player p = createPlayer();
    addListenerPlayer(p);
    p.incrementLives(1.0);
    Method healthCheck = Destroyable.class.getDeclaredMethod("getLives");
    healthCheck.setAccessible(true);
    assertEquals(2, (double) healthCheck.invoke(p));
  }

  @Test
  void testScaleVelocity() throws ClassNotFoundException {
    Player p = createPlayer();
    p.scaleVelocity(2.0, 3.0);
    assertEquals(new Vector(100, 0), p.getVelocity());
  }

  @Test
  void testScaleSize() throws ClassNotFoundException {
    Player p = createPlayer();
    p.scaleSize(2.0);
    assertEquals(new Vector(10, 10), p.getSize());
  }

  private Player createPlayer() throws ClassNotFoundException {
    List<String> tags = new ArrayList<>();
    tags.add("GameObject");
    tags.add("Player");
    int startHealth = 1;
    int startLife = 1;
    double jumpTime = 10;
    Vector velo = new Vector(50, 0);
    int comtJump = 1;
    double shootCool = 30;
    double invinc = 5;
    Player d = new Player(tags, new Vector(0, 0), 1, new Vector(5, 5), startLife,
        startHealth, jumpTime, velo, 1, new Vector(0,0), comtJump, shootCool, true, invinc);
    return d;
  }

  private void addListenerPlayer(Player p) {
    p.addListener("test", new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        System.out.println("added listener");
      }
    });
  }

  private MethodBundle createMethodBundle(String name, double[] params) {
    return new MethodBundle(name, params);
  }
}


