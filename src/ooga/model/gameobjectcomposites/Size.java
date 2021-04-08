package ooga.model.gameobjectcomposites;

import ooga.model.util.Vector;

public class Size {
  private Vector size;

  public Size(Vector objSize) {
    size = objSize;
  }

  public Vector getSize() {return size.copy();}

  public void scaleSize(double scaleFactor) {
    size.multiply(new Vector(scaleFactor, scaleFactor));
  }
}
