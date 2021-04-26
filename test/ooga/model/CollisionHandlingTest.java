package ooga.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ooga.model.gameobjects.Destroyable;
import ooga.model.gameobjects.DestroyableCollisionHandling;
import ooga.model.gameobjects.MovingDestroyable;
import ooga.model.gameobjects.Player;
import ooga.model.util.MethodBundle;
import ooga.model.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CollisionHandlingTest {

  DestroyableCollisionHandling collisionHandling;

  @BeforeEach
  public void init() {
    collisionHandling = new DestroyableCollisionHandling();
  }

  @Test
  void testBottomDiffAxisCollision(){
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
    MovingDestroyable b = new MovingDestroyable(bTags, bPos, 2, bSize, 1, 1, 5, bVel, bPosEnd, 1,
        true);
    MovingDestroyable a = new MovingDestroyable(aTags, aPos, 1, aSize, 1, 1, 5, aVel, aPosEnd, 1,
        true);

    List<String> expected = new ArrayList<>();
    expected.add("Enemy");
    expected.add("Enemy-DOWN");
    assertEquals(expected, collisionHandling.determineCollisionMethods(a, b));

  }

  @Test
  void testTopDiffAxisCollision() {
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
    MovingDestroyable b = new MovingDestroyable(bTags, bPos, 2, bSize, 1, 1, 5, bVel, bPosEnd, 1,
        true);
    MovingDestroyable a = new MovingDestroyable(aTags, aPos, 1, aSize, 1, 1, 5, aVel, aPosEnd, 1,
        true);

    List<String> expected = new ArrayList<>();
    expected.add("Enemy");
    expected.add("Enemy-UP");
    assertEquals(expected, collisionHandling.determineCollisionMethods(b, a));
  }

  @Test
  void testTopSameAxisCollision() {
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
    MovingDestroyable b = new MovingDestroyable(bTags, bPos, 2, bSize, 1, 1, 5, bVel, bPosEnd, 1,
        true);
    MovingDestroyable a = new MovingDestroyable(aTags, aPos, 1, aSize, 1, 1, 5, aVel, aPosEnd, 1,
        true);

    List<String> expected = new ArrayList<>();
    expected.add("Enemy");
    expected.add("Enemy-UP");
    assertEquals(expected, collisionHandling.determineCollisionMethods(b, a));
  }

  @Test
  void testBottomSameAxisCollision() {
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
    MovingDestroyable b = new MovingDestroyable(bTags, bPos, 2, bSize, 1, 1, 5, bVel, bPosEnd, 1,
        true);
    MovingDestroyable a = new MovingDestroyable(aTags, aPos, 1, aSize, 1, 1, 5, aVel, aPosEnd, 1,
        true);

    List<String> expected = new ArrayList<>();
    expected.add("Enemy");
    expected.add("Enemy-DOWN");
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

    MovingDestroyable b = new MovingDestroyable(bTags, bPos, 2, bSize, 1, 1, 5, bVel, bPosEnd, 1,
        true);
    MovingDestroyable a = new MovingDestroyable(aTags, aPos, 1, aSize, 1, 1, 5, aVel, aPosEnd, 1,
        true);

    List<String> expected = new ArrayList<>();
    expected.add("Enemy");
    expected.add("Enemy-RIGHT");
    assertEquals(expected, collisionHandling.determineCollisionMethods(a, b));
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

    MovingDestroyable b = new MovingDestroyable(bTags, bPos, 2, bSize, 1, 1, 5, bVel, bPosEnd, 1,
        true);
    MovingDestroyable a = new MovingDestroyable(aTags, aPos, 1, aSize, 1, 1, 5, aVel, aPosEnd, 1,
        true);

    List<String> expected = new ArrayList<>();
    expected.add("Enemy");
    expected.add("Enemy-LEFT");
    assertEquals(expected, collisionHandling.determineCollisionMethods(b, a));
  }

  @Test
  void testCornerCollision() {
    List<String> bTags = new ArrayList<>();
    bTags.add("Enemy");
    Vector bPos = new Vector(10, 20);
    Vector bSize = new Vector(5, 5);
    Vector bPosEnd = new Vector(40, 20);
    Vector bVel = new Vector(0, -1);
    List<String> aTags = new ArrayList<>();
    aTags.add("Enemy");
    Vector aPos = new Vector(14, 15);
    Vector aSize = new Vector(5, 5);
    Vector aPosEnd = new Vector(40, 15);
    Vector aVel = new Vector(0, 1);
    MovingDestroyable b = new MovingDestroyable(bTags, bPos, 2, bSize, 1, 1, 5, bVel, bPosEnd, 1,
        true);
    MovingDestroyable a = new MovingDestroyable(aTags, aPos, 1, aSize, 1, 1, 5, aVel, aPosEnd, 1,
        true);

    assertEquals(true, collisionHandling.smallCorner(a, b));
  }

  @Test
  void playerCollisionInvokeFakeTest() throws NoSuchMethodException {
    Map<String, Map<String, List<MethodBundle>>> methodMap = makeBadMap();
    Destroyable p = createDestroyable();
    p.addCollision(methodMap.get("Player").get("GameObject"));
    assertThrows(NoSuchMethodException.class, () -> p.executeCollisions());
  }

  @Test
  void playerCollisionInvokeRealTest()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Map<String, Map<String, List<MethodBundle>>> methodMap = makeGoodMap();
    Destroyable p = createDestroyable();
    p.addCollision(methodMap.get("Destroyable").get("GameObject"));
    p.executeCollisions();
    Method healthCheck = Destroyable.class.getDeclaredMethod("getHealth");
    healthCheck.setAccessible(true);
    assertEquals(2, (double) healthCheck.invoke(p));
  }


  private Map<String, Map<String, List<MethodBundle>>> makeBadMap() {
    Map<String, Map<String, List<MethodBundle>>> bad = new HashMap<>();
    Map<String, List<MethodBundle>> badObj = new HashMap<>();
    badObj.put("GameObject", new ArrayList<>());
    double[] params = new double[1];
    params[0] = 1;
    MethodBundle m = new MethodBundle("fakeMethod", params);
    badObj.get("GameObject").add(m);
    bad.put("Player", badObj);
    return bad;
  }

  private Map<String, Map<String, List<MethodBundle>>> makeGoodMap() {
    Map<String, Map<String, List<MethodBundle>>> bad = new HashMap<>();
    Map<String, List<MethodBundle>> badObj = new HashMap<>();
    badObj.put("GameObject", new ArrayList<>());
    double[] params = new double[1];
    params[0] = 1;
    MethodBundle m = new MethodBundle("incrementHealth", params);
    badObj.get("GameObject").add(m);
    bad.put("Destroyable", badObj);
    return bad;
  }

  private Player makePlayer() throws ClassNotFoundException {
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