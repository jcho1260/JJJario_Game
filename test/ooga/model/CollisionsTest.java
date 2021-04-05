package ooga.model;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CollisionsTest {
  private CollisionCheck collisionCheck;
  private Map<String, Map<String, List<String>>> collisionMethods;
  private List<GameObject> allGameObjects;


  @BeforeEach
  public void init() {
  }

  /**
   *
   */
//  @Test
//  void testCollisionMethodConstructingActor() {
//    List<MethodBundle> collisionList = new ArrayList<>();
//    collisionList.add("incrementHealth");
//    Actor actor = constructTestActor();
//    actor.addCollision(collisionList);
//    List<Method> collisions = actor.collisions;
//    assertEquals(1, collisions.size());
//    assertEquals("incrementHealth", collisions.remove(0).getName());
//  }

  /**
   * helper methods
   */
  private Actor constructTestActor() {
    List<String> entityTypes = new ArrayList<>();
    entityTypes.add("Player");
    entityTypes.add("Mario");
    Vector position = new Vector(0, 0);
    Vector velocity = new Vector(0, 0);
    double gravity = 1;
    Actor ret = new Actor(entityTypes, position, velocity, gravity, 1, new Vector(1,1));
    return ret;
  }

}