package ooga.model.gameobjects;

import java.io.Serializable;
import java.util.List;
import ooga.Observable;
import ooga.model.gameobjectcomposites.Rectangle;
import ooga.model.util.Vector;

/**
 * Represents GameObject - any component of the game that has position and should be displayed.
 * Connected to front end Sprite via observer pattern.
 *
 * @author Jin Cho, Juhyoung Lee, Jessica Yang
 */
public class GameObject extends Observable implements Serializable {

  private final List<String> entityTypes;
  private final int id;
  private boolean isActive;
  private final boolean isVisible;
  protected Rectangle rect;

  /**
   * Default constructor with values from datafiles.
   *
   * @param entityTypes tags
   * @param position location
   * @param id unique id
   * @param size size
   * @param visible visibility
   */
  public GameObject(List<String> entityTypes, Vector position, int id, Vector size,
      boolean visible) {
    this.entityTypes = entityTypes;
    this.id = id;
    isActive = false;
    rect = new Rectangle(size, position);
    isVisible = visible;
  }

  /**
   * Returns position vector.
   *
   * @return position
   */
  public Vector getPosition() {
    return rect.getPosition();
  }

  /**
   * Sets predicted location as current location.
   */
  public void updatePosition() {
    rect.updatePosition();
  }

  /**
   * Returns rectangle object that stores location and size.
   *
   * @return rectangle object
   */
  protected Rectangle getRect() {
    return rect;
  }

  /**
   * Returns predicted location.
   *
   * @return predicted location
   */
  public Vector getPredictedPosition() {
    return rect.getPredictedPos();
  }

  /**
   * Updates predicted location.
   *
   * @param pos predicted location.
   */
  public void setPredictedPosition(Vector pos) {
    rect.setPredictedPos(pos);
  }

  /**
   * Returns tags.
   *
   * @return entity tags
   */
  public List<String> getEntityType() {
    return entityTypes;
  }

  /**
   * Returns id.
   *
   * @return unique id
   */
  public int getId() {
    return id;
  }

  /**
   * Returns size.
   *
   * @return object size
   */
  public Vector getSize() {
    return rect.getSize();
  }

  /**
   * Step method to be overwritten.
   *
   * @param elapsedTime frame duration
   * @param gameGravity gravity
   */
  public void step(double elapsedTime, double gameGravity) {

  }

  /**
   * Returns velocity. Should be overwritten by moving implementations.
   *
   * @return velocity
   */
  public Vector getVelocity() {
    return new Vector(0, 0);
  }

  /**
   * Updates active state and visibility.
   *
   * @param activeState new state
   */
  public void setActive(boolean activeState) {
    isActive = activeState;
    notifyListenerKey("sprite", "changeVisibility", null, isActive && isVisible);
  }

  /**
   * Converts model coordinates to view coordinates, and sends GameObject position.
   *
   * @param frameTopL frame
   */
  public void sendToView(Vector frameTopL) {
    double viewPositionX;
    double viewPositionY;
    viewPositionX = rect.getPosition().getX() - frameTopL.getX();
    viewPositionY = rect.getPosition().getY() - frameTopL.getY();
    notifyListenerKey("sprite", "changeX", null, viewPositionX);
    notifyListenerKey("sprite", "changeY", null, viewPositionY);
  }
}
