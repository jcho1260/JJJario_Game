package ooga.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VectorTest {
  private Vector v1;
  private Vector v2;
  private Vector v3;

  @BeforeEach
  public void init() {
    v1 = new Vector(0.3, -1.8);
    v2 = new Vector(0, 4.5);
    v3 = new Vector(-8.1, 7);
  }

  @Test
  void testNewVector() {
    Vector vec = new Vector(0, 3.3);
    assertEquals(vec.getX(), 0);
    assertEquals(vec.getY(), 3.3);
  }

  @Test
  void testGetters() {
    assertEquals(v1.getX(), 0.3);
    assertEquals(v1.getY(), -1.8);
  }

  @Test
  void testAdd() {
    Vector vec = v1.add(v2);
    assertEquals(vec.getX(), 0.3);
    assertEquals(vec.getY(), 2.7);
  }

  @Test
  void testMultiply() {
    Vector vec = v1.multiply(v2);
    assertEquals(vec.getX(), 0);
    assertEquals(vec.getY(), -8.1);
  }

  @Test
  void testCopy() {
    assertEquals(v1, v1.copy());
  }

  @Test
  void testGetDirection() {
    assertEquals(v1.getDirection().toString(), "UP");
    assertEquals(v2.getDirection().toString(), "DOWN");
    assertEquals(v3.getDirection().toString(), "LEFT");
  }

  @Test
  void testInsideBox() {
    Vector topLeft = new Vector(-5, -5);
    Vector vec = new Vector((topLeft.getX()+v2.getX())/2, (topLeft.getY()+v2.getY())/2);
    assertEquals(true, vec.insideBox(topLeft, v2));
    assertEquals(false, v3.insideBox(v1, v2));
  }

  @Test
  void testEquals() {
    assertEquals(v1, new Vector(v1.getX(), v1.getY()));
    assertEquals(v2, new Vector(v2.getX(), v2.getY()));
    assertEquals(v3, new Vector(v3.getX(), v3.getY()));
  }
}
