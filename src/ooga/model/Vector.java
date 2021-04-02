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
}