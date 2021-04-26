package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Method;
import ooga.model.gameobjects.GameObject;
import ooga.model.gameobjects.Player;
import ooga.model.util.MethodBundle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MethodBundleTest {

  MethodBundle m;
  MethodBundle m2;
  @BeforeEach
  @Test
  public void init() {
    double[] params = new double[1];
    double[] params2 = new double[2];
    params[0] = 1;
    params2[0] = 1;
    params2[1] = 1;
    m = new MethodBundle("incrementHealth", params);
    assertNotNull(m);
    m2 = new MethodBundle("incrementHealth", params2);
    assertNotNull(m2);
  }

  @Test
  void makeMethodTest() throws NoSuchMethodException {
    Method method = m.makeMethod(Player.class);
    assertEquals("incrementHealth", method.getName());
  }

  @Test
  void makeNoneExistMethodTest() {
    assertThrows(NoSuchMethodException.class, () ->m.makeMethod(GameObject.class));
  }

  @Test
  void makeWrongParamMethodTest() {
    assertThrows(NoSuchMethodException.class, () ->m2.makeMethod(Player.class));
  }

  @Test
  void getParamsTest() {
    Double[] expected = new Double[1];
    expected[0] = 1.0;
    assertEquals(expected[0], m.getParameters()[0]);
    assertEquals(1, m.getParameters().length);
  }


}
