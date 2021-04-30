package ooga.model.gameobjectcomposites;

import java.io.Serializable;
import ooga.model.util.Vector;

/**
 * Rectangle represents the lowest level of a game object. It stores position, size, and next
 * location. Next location is used as an intermediary to store the next frame's location. This is
 * done to avoid committing to movement changes before checking to see if they cause collision
 * issues.
 *
 * @author Juhyoung Lee, Jin Cho, Jessica Yang
 */
public class Rectangle implements Serializable {

  private Vector position;
  private Vector size;
  private Vector predictedPos;


  /**
   * Default constructor.
   *
   * @param objSize size
   * @param objPos location
   */
  public Rectangle(Vector objSize, Vector objPos) {
    position = objPos;
    size = objSize;
    predictedPos = position;
  }

  /**
   * Returns size.
   *
   * @return size vector
   */
  public Vector getSize() {
    return size.copy();
  }

  /**
   * Applies scaling on size.
   *
   * @param scaleFactor multiplier
   */
  public void scaleSize(double scaleFactor) {
    size = size.multiply(new Vector(scaleFactor, scaleFactor));
    predictedPos = predictedPos.subtract(new Vector(0, size.getY() * (scaleFactor - 1)));
  }

  /**
   * Returns position.
   *
   * @return position vector
   */
  public Vector getPosition() {
    return position.copy();
  }

  /**
   * Returns a copy of predicted position.
   *
   * @return predicted position
   */
  public Vector getPredictedPos() {
    return predictedPos.copy();
  }

  /**
   * Updates predicted position.
   *
   * @param pos new position
   */
  public void setPredictedPos(Vector pos) {
    predictedPos = pos;
  }

  /**
   * Sets predicted position as current position.
   */
  public void updatePosition() {
    position = predictedPos;
  }

}
