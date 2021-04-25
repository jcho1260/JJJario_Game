package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import ooga.model.gameobjects.Destroyable;
import ooga.model.util.MethodBundle;
import ooga.model.util.Vector;
import org.junit.jupiter.api.Test;

public class DestroyableCollisionsTest {

  @Test
  void incrementHealthTest()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Destroyable d = createDestroyable();
    double[] params = new double[1];
    params[0] = 1;
    List<MethodBundle> methods = new ArrayList<>();
    MethodBundle incHealth = createMethodBundle("incrementHealth", params);
    methods.add(incHealth);
    d.addCollision(methods);
    d.executeCollisions();
    Method healthCheck = Destroyable.class.getDeclaredMethod("getHealth");
    healthCheck.setAccessible(true);
    assertEquals(2, (int) healthCheck.invoke(d));
  }

  @Test
  void incrementLivesTest()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Destroyable d = createDestroyable();
    double[] params = new double[1];
    params[0] = 1;
    List<MethodBundle> methods = new ArrayList<>();
    MethodBundle incLives = createMethodBundle("incrementLives", params);
    methods.add(incLives);
    d.addCollision(methods);
    d.executeCollisions();
    Method healthCheck = Destroyable.class.getDeclaredMethod("getLives");
    healthCheck.setAccessible(true);
    assertEquals(2, (int) healthCheck.invoke(d));
  }

  @Test
  void killTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Destroyable d = createDestroyable();
    d.kill();
    Method livesCheck = Destroyable.class.getDeclaredMethod("getLives");
    Method healthCheck = Destroyable.class.getDeclaredMethod("getHealth");
    livesCheck.setAccessible(true);
    healthCheck.setAccessible(true);
    assertEquals(1, (int) healthCheck.invoke(d));
    assertEquals(0, (int) livesCheck.invoke(d));
  }

  @Test
  void scoreTest() {
    Destroyable d = createDestroyable();
    assertEquals(1, d.getScore());
  }

  @Test
  void checkAliveTest() {
    Destroyable d = createDestroyable();
    assertEquals(true, d.isAlive());
    d.kill();
    assertEquals(true, d.isAlive());
    d.kill();
    assertEquals(false, d.isAlive());
  }

  private MethodBundle createMethodBundle(String name, double[] params) {
    return new MethodBundle(name, params);
  }

  private Destroyable createDestroyable() {
    List<String> tags = new ArrayList<>();
    tags.add("GameObject");
    tags.add("Destroyable");
    int startHealth = 1;
    int startLife = 1;
    Destroyable d = new Destroyable(tags, new Vector(0, 0), 1, new Vector(5, 5), startLife,
        startHealth, 1, true);
    return d;
  }

}
