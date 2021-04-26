package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import ooga.model.gameobjects.MovingDestroyable;
import ooga.model.util.Vector;
import org.junit.jupiter.api.Test;

public class MovingDestroyableCollisionsTest {


  @Test
  void testGetVelocity(){
    MovingDestroyable p = createMovingDestroyable();
    assertEquals(new Vector(50, 0), p.getVelocity());
  }

  @Test
  void testScaleVelocity(){
    MovingDestroyable p = createMovingDestroyable();
    p.scaleVelocity(2.0, 3.0);
    assertEquals(new Vector(100, 0), p.getVelocity());
  }

  @Test
  void testCreateMovingDestroyable(){
    MovingDestroyable p = createMovingDestroyable();
    assertNotNull(p);
  }

  private MovingDestroyable createMovingDestroyable(){
    List<String> tags = new ArrayList<>();
    tags.add("GameObject");
    tags.add("MovingDestroyable");
    int startHealth = 1;
    int startLife = 1;
    Vector velo = new Vector(50, 0);
    MovingDestroyable d = new MovingDestroyable(tags, new Vector(0, 0), 1, new Vector(5, 5), startLife,
        startHealth, 2, velo, new Vector(5,0), 1, true);
    return d;
  }
}
