package ooga.model;

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

  private Vector toCardinal() {
    if (Math.abs(yVal) >= Math.abs(xVal)) {
      return new Vector(0, yVal);
    }
      return new Vector(xVal, 0);
  }

  public Movement getDirection() {
    Vector cardinal = toCardinal();
    if(cardinal.getX() < 0) {
      return Movement.LEFT;
    } else if(cardinal.getX() > 0) {
      return Movement.RIGHT;
    } else if(cardinal.getY() < 0) {
      return Movement.UP;
    } else if(cardinal.getY() > 0) {
      return Movement.DOWN;
    } else {
      return Movement.DOWN;
      // todo: fix later
    }
  }

  public boolean equals(Object o) {
    if (o instanceof Vector) {
      Vector vec = (Vector) o;
      return xVal == vec.getX() && yVal == vec.getY();
    }
    return false;
  }
}