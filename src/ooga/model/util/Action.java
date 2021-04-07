package ooga.model.util;

public enum Action {
  UP("y"),
  DOWN("y"),
  LEFT("x"),
  RIGHT("x"),
  NONE("x or y");

  private String axis;

  Action(String axis) {
    this.axis = axis;
  }

  public boolean sameAxis(Action a) {
    return axis.equals(a.getAxis()) || this.toString().equals("NONE") || a.toString().equals("NONE");
  }

  public String getAxis() {return axis;}
}
