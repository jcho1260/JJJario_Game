package ooga.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VectorTest {
  private Vector v1;
  private Vector v2;

  @BeforeEach
  public void init() {
    v1 = new Vector(3, -1);
    v2 = new Vector(0, 4);
  }

  @Test
  void testNewVector() {

  }
}
