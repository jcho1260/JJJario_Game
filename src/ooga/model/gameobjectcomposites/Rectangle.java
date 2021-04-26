package ooga.model.gameobjectcomposites;

import java.io.Serializable;
import ooga.model.util.Action;
import ooga.model.util.Vector;

public class Rectangle implements Serializable {
  private Vector position;
  private Vector size;
  private Vector predictedPos;


  public Rectangle(Vector objSize, Vector objPos) {
    position = objPos;
    size = objSize;
    predictedPos = position;
  }

  public Vector getSize() {return size.copy();}

  public void scaleSize(double scaleFactor) {
    size = size.multiply(new Vector(scaleFactor, scaleFactor));
    predictedPos = predictedPos.subtract(new Vector(0, size.getY() * (scaleFactor - 1)));
  }

  public Vector getPosition() { return position.copy(); }

  public Vector getPredictedPos() { return predictedPos.copy(); }

  public void setPredictedPos(Vector pos) { predictedPos = pos; }

  public void updatePosition() {
    position = predictedPos;
  }

}
