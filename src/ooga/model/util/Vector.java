package ooga.model.util;

import java.io.Serializable;

/**
 * Represents vectors and associated simple vector operations. Uses vectors as arithmetic objects but also directional and locational
 * so that they could be used in a variety of contexts within the game.
 *
 * @authos jincho juhyounglee
 */
public class Vector implements Serializable {

  private final double xVal;
  private final double yVal;

  /**
   * constructs a vector
   * @param x x direction of vector
   * @param y y direction of vector
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
   * @param change change vector to be added to x and y value of vector
   * @return sum of 2 vectors (this and param)
   */
  public Vector add(Vector change) {
    return new Vector(xVal + change.getX(), yVal + change.getY());
  }

  /**
   * Element wise multiplication.
   *
   * @param change vector multiplying
   * @return product of this and param
   */
  public Vector multiply(Vector change) {
    return new Vector(xVal * change.getX(), yVal * change.getY());
  }

  /**
   * Creates a new vector with same x and y values as this
   *
   * @return copy vector
   */
  public Vector copy() {
    return new Vector(xVal, yVal);
  }

  /**
   * creates a unit vector in the closest cardinal direction to this vector
   * @return unit vector in closest cardinal direction
   */
  public Vector toUnit() {
    Vector ret = toCardinal();
    double diveFrac = 1.0 / Math.max(Math.abs(xVal), Math.abs(yVal));
    return ret.multiply(new Vector(diveFrac, diveFrac));
  }

  private Vector toCardinal() {
    if (Math.abs(yVal) >= Math.abs(xVal)) {
      return new Vector(0, yVal);
    }
    return new Vector(xVal, 0);
  }

  /**
   * maps the closest cardinal direction to an Action enum value based on the directions mapping
   * @return Action enum for closest cardinal direction
   */
  public Action getDirection() {
    Vector cardinal = toCardinal();
    if (cardinal.getX() < 0) {
      return Action.LEFT;
    } else if (cardinal.getX() > 0) {
      return Action.RIGHT;
    } else if (cardinal.getY() < 0) {
      return Action.UP;
    } else if (cardinal.getY() > 0) {
      return Action.DOWN;
    } else {
      return Action.DOWN;
    }
  }

  /**
   * checks if the vector is in a box/rectangle as defined by the top left and bottom right vectors of a box
   * @param topL top left coordinate of the rectangle
   * @param botR bottom right coordinate of the rectangle
   * @return true if vector is in box
   */
  public boolean insideBox(Vector topL, Vector botR) {
    if (xVal > topL.getX() && xVal < botR.getX()) {
      return yVal < botR.getY() && yVal > topL.getY();
    }
    return false;
  }

  /**
   * checks if given vector is equal in direction and magnitude to this vector
   * @param o vector to compare to
   * @return true if equal vectors
   */
  public boolean equals(Object o) {
    if (o instanceof Vector) {
      Vector vec = (Vector) o;
      return xVal == vec.getX() && yVal == vec.getY();
    }
    return false;
  }

  /**
   * allows for string representation of the vector as its X and Y components
   * @return String representation of vector
   */
  @Override
  public String toString() {
    return "Vector{" + xVal +
        ", " + yVal +
        '}';
  }

  /**
   * calculates magnitude of the vector
   * @return magnitude of vector
   */
  public double calculateMagnitude() {
    return Math.sqrt((xVal * xVal) + (yVal * yVal));
  }

  /**
   * calculated difference between param vector and this vector
   * @param v vector to subtract from this
   * @return difference between v and this vector
   */
  public Vector subtract(Vector v) {
    return this.copy().add(v.multiply(new Vector(-1, -1)));
  }

}