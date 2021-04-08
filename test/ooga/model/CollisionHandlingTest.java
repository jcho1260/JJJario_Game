package ooga.model;

import java.util.ArrayList;
import java.util.List;
import ooga.model.gameobjects.DestroyableCollisionHandling;
import ooga.model.gameobjects.MovingDestroyable;
import ooga.model.gameobjects.Player;
import ooga.model.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CollisionHandlingTest {
  DestroyableCollisionHandling collisionHandling;

  @BeforeEach
  public void init() {
    collisionHandling = new DestroyableCollisionHandling();
  }

  void testBottomDiffAxisCollision() throws ClassNotFoundException {
    List<String> bTags = new ArrayList<>();
    bTags.add("Enemy");
    Vector bPos = new Vector(10, 20);
    Vector bSize = new Vector(5, 5);
    Vector bPosEnd = new Vector(40, 20);
    Vector bVel = new Vector(1, 0);
    List<String> aTags = new ArrayList<>();
    aTags.add("Enemy");
    Vector aPos = new Vector(10, 15);
    Vector aSize = new Vector(5, 5);
    Vector aPosEnd = new Vector(40, 15);
    Vector aVel = new Vector(0, 1);
    MovingDestroyable b = new MovingDestroyable(bTags, bPos, 2, bSize, 1, 1, bVel, bPosEnd, 1);
    MovingDestroyable a = new MovingDestroyable(aTags, aPos, 1, aSize, 1, 1, aVel, aPosEnd, 1);

    List<String> expected = new ArrayList<>();
    expected.add("EnemyDOWN");
    assertEquals(expected, collisionHandling.determineCollisionMethods(a,b));

  }


  void testTopDiffAxisCollision() throws ClassNotFoundException {
    List<String> bTags = new ArrayList<>();
    bTags.add("Enemy");
    Vector bPos = new Vector(10, 20);
    Vector bSize = new Vector(5, 5);
    Vector bPosEnd = new Vector(40, 20);
    Vector bVel = new Vector(1, 0);
    List<String> aTags = new ArrayList<>();
    aTags.add("Enemy");
    Vector aPos = new Vector(10, 15);
    Vector aSize = new Vector(5, 5);
    Vector aPosEnd = new Vector(40, 15);
    Vector aVel = new Vector(0, 1);
    MovingDestroyable b = new MovingDestroyable(bTags, bPos, 2, bSize, 1, 1, bVel, bPosEnd, 1);
    MovingDestroyable a = new MovingDestroyable(aTags, aPos, 1, aSize, 1, 1, aVel, aPosEnd, 1);

    List<String> expected = new ArrayList<>();
    expected.add("EnemyUP");
    assertEquals(expected, collisionHandling.determineCollisionMethods(b,a));
  }

  @Test
  void testTopSameAxisCollision() throws ClassNotFoundException {
    List<String> bTags = new ArrayList<>();
    bTags.add("Enemy");
    Vector bPos = new Vector(10, 20);
    Vector bSize = new Vector(5, 5);
    Vector bPosEnd = new Vector(40, 20);
    Vector bVel = new Vector(0, -1);
    List<String> aTags = new ArrayList<>();
    aTags.add("Enemy");
    Vector aPos = new Vector(10, 15);
    Vector aSize = new Vector(5, 5);
    Vector aPosEnd = new Vector(40, 15);
    Vector aVel = new Vector(0, 1);
    MovingDestroyable b = new MovingDestroyable(bTags, bPos, 2, bSize, 1, 1, bVel, bPosEnd, 1);
    MovingDestroyable a = new MovingDestroyable(aTags, aPos, 1, aSize, 1, 1, aVel, aPosEnd, 1);

    List<String> expected = new ArrayList<>();
    expected.add("EnemyUP");
    assertEquals(expected, collisionHandling.determineCollisionMethods(b,a));
  }


  void testBottomSameAxisCollision() throws ClassNotFoundException {
    List<String> bTags = new ArrayList<>();
    bTags.add("Enemy");
    Vector bPos = new Vector(10, 20);
    Vector bSize = new Vector(5, 5);
    Vector bPosEnd = new Vector(40, 20);
    Vector bVel = new Vector(0, -1);
    List<String> aTags = new ArrayList<>();
    aTags.add("Enemy");
    Vector aPos = new Vector(10, 15);
    Vector aSize = new Vector(5, 5);
    Vector aPosEnd = new Vector(40, 15);
    Vector aVel = new Vector(0, 1);
    MovingDestroyable b = new MovingDestroyable(bTags, bPos, 2, bSize, 1, 1, bVel, bPosEnd, 1);
    MovingDestroyable a = new MovingDestroyable(aTags, aPos, 1, aSize, 1, 1, aVel, aPosEnd, 1);

    List<String> expected = new ArrayList<>();
    expected.add("EnemyDOWN");
    assertEquals(expected, collisionHandling.determineCollisionMethods(a, b));
  }

  @Test
  void testRightSameAxisCollision() {
    List<String> bTags = new ArrayList<>();
    bTags.add("Enemy");
    Vector bPos = new Vector(15, 15);
    Vector bSize = new Vector(5, 5);
    Vector bPosEnd = new Vector(40, 20);
    Vector bVel = new Vector(1, 0);
    List<String> aTags = new ArrayList<>();
    aTags.add("Enemy");
    Vector aPos = new Vector(10, 15);
    Vector aSize = new Vector(5, 5);
    Vector aPosEnd = new Vector(40, 20);
    Vector aVel = new Vector(1, 0);

    MovingDestroyable b = new MovingDestroyable(bTags, bPos, 2, bSize, 1, 1, bVel, bPosEnd, 1);
    MovingDestroyable a = new MovingDestroyable(aTags, aPos, 1, aSize, 1, 1, aVel, aPosEnd, 1);

    List<String> expected = new ArrayList<>();
    expected.add("EnemyRIGHT");
    assertEquals(expected, collisionHandling.determineCollisionMethods(a,b));
  }

  @Test
  void testLeftSameAxisCollision() {
    List<String> bTags = new ArrayList<>();
    bTags.add("Enemy");
    Vector bPos = new Vector(15, 15);
    Vector bSize = new Vector(5, 5);
    Vector bPosEnd = new Vector(40, 20);
    Vector bVel = new Vector(-1, 0);
    List<String> aTags = new ArrayList<>();
    aTags.add("Enemy");
    Vector aPos = new Vector(10, 15);
    Vector aSize = new Vector(5, 5);
    Vector aPosEnd = new Vector(40, 20);
    Vector aVel = new Vector(1, 0);

    MovingDestroyable b = new MovingDestroyable(bTags, bPos, 2, bSize, 1, 1, bVel, bPosEnd, 1);
    MovingDestroyable a = new MovingDestroyable(aTags, aPos, 1, aSize, 1, 1, aVel, aPosEnd, 1);

    List<String> expected = new ArrayList<>();
    expected.add("EnemyLEFT");
    assertEquals(expected, collisionHandling.determineCollisionMethods(b,a));
  }


}