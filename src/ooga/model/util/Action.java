package ooga.model.util;

import java.io.Serializable;

/**
 * This is the enumerated class that defines the different actions a user can input with key presses.
 * Within each enumerated value, there is also a direction defined when associated with a directioned action
 * such as left and right.
 *
 * @author jincho juhyounglee jessicayang
 */
public enum Action implements Serializable {
  UP(new Vector(0, -1)),
  DOWN(new Vector(0, 1)),
  LEFT(new Vector(-1, 0)),
  RIGHT(new Vector(1, 0)),
  NONE(new Vector(0, 0)),
  SHOOT(new Vector(0, 0));


  private final Vector axis;

  /**
   * creates a unique Action enumerated value with a given vector defining direction when appropriate
   * @param axis
   */
  Action(Vector axis) {
    this.axis = axis;
  }

}
