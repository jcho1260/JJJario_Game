package ooga.model.util;

/**
 * Represents vectors and associated vector operations.
 */
public class Vector {

  private double xVal, yVal;

  /**
   * Default constructor.
   */
  public Vector(double x, double y) {
    xVal = x;
    yVal = y;
  }

  /**
   * Returns x direction of vector.
   *
   * @return xVal
   */
  public double getX() {
    return xVal;
  }

  /**
   * Returns y direction of vector.
   *
   * @return: yVal
   */
  public double getY() {
    return yVal;
  }

  /**
   * Creates a new vector resulting from elementwise addition.
   *
   * @param change change to be added to x and y value of vector
   * @return sum
   */
  public Vector add(Vector change) {
    return new Vector(xVal + change.getX(), yVal + change.getY());
  }

  /**
   * Element wise multiplication.
   *
   * @param change vector multiplying
   * @return product
   */
  public Vector multiply(Vector change) {
    return new Vector(xVal * change.getX(), yVal * change.getY());
  }

  /**
   * Creates a new vector with same x and y values.
   *
   * @return copy vector
   */
  public Vector copy() {
    return new Vector(xVal, yVal);
  }

  public Vector toUnit() {
    Vector ret = toCardinal();
    double diveFrac = 1.0/Math.max(Math.abs(xVal), Math.abs(yVal));
    return ret.multiply(new Vector(diveFrac, diveFrac));
  }

  private Vector toCardinal() {
    if (Math.abs(yVal) >= Math.abs(xVal)) {
      return new Vector(0, yVal);
    }
      return new Vector(xVal, 0);
  }

  public Action getDirection() {
    Vector cardinal = toCardinal();
    if(cardinal.getX() < 0) {
      return Action.LEFT;
    } else if(cardinal.getX() > 0) {
      return Action.RIGHT;
    } else if(cardinal.getY() < 0) {
      return Action.UP;
    } else if(cardinal.getY() > 0) {
      return Action.DOWN;
    } else {
      return Action.DOWN;
      // todo: fix later
    }
  }

  public boolean insideBox(Vector topL, Vector botR) {
    if (xVal > topL.getX() && xVal < botR.getX()) {
      if (yVal < botR.getY() && yVal > topL.getY()) {
        return true;
      }
    }
    return false;
  }

  public boolean equals(Object o) {
    if (o instanceof Vector) {
      Vector vec = (Vector) o;
      return xVal == vec.getX() && yVal == vec.getY();
    }
    return false;
  }

  @Override
  public String toString() {
    return "Vector{" + xVal +
        ", " + yVal +
        '}';
  }

  public double calculateMagnitude() {
    return Math.sqrt((xVal * xVal) + (yVal * yVal));
  }

  public Vector subtract(Vector v) {
    return this.copy().add(v.multiply(new Vector(-1, -1)));
  }

}