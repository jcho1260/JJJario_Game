package ooga.model.util;

import java.io.Serializable;

public enum Action implements Serializable {
  UP(new Vector(0, -1)),
  DOWN(new Vector(0, 1)),
  LEFT(new Vector(-1, 0)),
  RIGHT(new Vector(1, 0)),
  NONE(new Vector(0, 0)),
  SHOOT(new Vector(0, 0));


  private final Vector axis;

  Action(Vector axis) {
    this.axis = axis;
  }

}
