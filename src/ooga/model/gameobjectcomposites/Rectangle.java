package ooga.model.gameobjectcomposites;

import ooga.model.util.Action;
import ooga.model.util.Vector;

public class Rectangle {
  private Vector position;
  private Vector size;
  private Vector predictedPos;


  public Rectangle(Vector objSize, Vector objPos) {
    position = objPos;
    size = objSize;
  }

  public Vector getSize() {return size.copy();}

  public void scaleSize(double scaleFactor) {
    size.multiply(new Vector(scaleFactor, scaleFactor));
  }

  public Action getFace(Vector velo) {
    return Action.actionFactory(velo);
  }

  /**
   * returns the coordinates of the endpoints of the rectangle edge defined as its face
   * @return array of vectors defining the endpoints
   */
  public Vector[] getFacePos(){
    Vector[] ret = new Vector[2];
    double x = predictedPos.getX() - position.getX();
    double y = predictedPos.getY() - position.getY();
    Vector dir = (new Vector(x, y)).toUnit();
    if (dir.getX() > 0 || dir.getY() > 0) {
      ret[0] = position.copy().add(size);
      Vector change = size.copy().multiply(dir);
      ret[1] = position.copy().add(change);
    } else if (dir.getX() < 0 || dir.getY() < 0) {
      ret[0] = position.copy();
      Vector flipped = new Vector(dir.getY(), dir.getX());
      Vector change = size.copy().multiply(flipped);
      ret[1] = position.copy().add(change);
    }
    return ret;
  }

  public Vector getPosition() { return position.copy(); }

  public void setPosition(Vector newPos) { position = newPos; }

  public Vector getPredictedPos() { return predictedPos.copy(); }

  public void setPredictedPos(Vector pos) { predictedPos = pos; }

}
