package ooga.model;

import java.util.ArrayList;
import java.util.List;
import ooga.model.gameobjects.GameObject;
import ooga.model.util.Vector;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class GameObjectTest {

  @Test
  void testConstruct() {
    GameObject o = constructGameObject();
    assertNotNull(0);
  }

  @Test
  void testNoVelocity() {
    GameObject o = constructGameObject();
    assertEquals(new Vector(0,0), o.getVelocity());
  }

  @Test
  void testGetPos() {
    GameObject o = constructGameObject();
    assertEquals(new Vector(0,0), o.getPosition());
  }

  @Test
  void testGetID() {
    GameObject o = constructGameObject();
    assertEquals(1, o.getId());
  }

  @Test
  void testGetSize() {
    GameObject o = constructGameObject();
    assertEquals(new Vector(5,5), o.getSize());
  }

  @Test
  void testNewPos() {
    GameObject o = constructGameObject();
    assertEquals(new Vector(0,0), o.getPredictedPosition());
    o.setPredictedPosition(new Vector(5,0));
    assertEquals(new Vector(5,0), o.getPredictedPosition());
    assertEquals(new Vector(0,0), o.getPosition());
    o.updatePosition();
    assertEquals(new Vector(5,0), o.getPosition());
  }

  @Test
  void testTags() {
    GameObject o = constructGameObject();
    List<String> expected = new ArrayList<>();
    expected.add("GameObject");
    assertEquals(expected, o.getEntityType());
  }

  private GameObject constructGameObject() {
    List<String> tags = new ArrayList<>();
    tags.add("GameObject");
    GameObject o = new GameObject(tags, new Vector(0, 0), 1, new Vector(5, 5), true);
    return o;
  }
}
