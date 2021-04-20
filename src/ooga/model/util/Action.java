package ooga.model.util;

import java.io.Serializable;

public enum Action implements Serializable {
  UP(new Vector(0, -1)),
  DOWN(new Vector(0, 1)),
  LEFT(new Vector(-1, 0)),
  RIGHT(new Vector(1, 0)),
  NONE(new Vector(0, 0));

  private Vector axis;

  Action(Vector axis) {
    this.axis = axis;
  }

  public Vector getAxis() {return axis;}

  public Action getOppositeAction(Action unit) {
    Vector goal = unit.getAxis().multiply(new Vector(-1, -1));
    return actionFactory(goal);
  }

  public static Action actionFactory(Vector velo) {
    Vector goal = velo.toUnit();
    for(Action a : Action.values()) {
      if (goal.equals(a.getAxis())) {
        return a;
      }
    }
    return null;
  }
}
