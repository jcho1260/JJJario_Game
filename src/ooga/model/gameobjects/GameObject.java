package ooga.model.gameobjects;

import java.io.Serializable;
import java.util.List;
import ooga.Observable;
import ooga.model.gameobjectcomposites.Rectangle;
import ooga.model.util.Vector;

/**
 * Represents GameObject - any component of the game that has position and associate movement
 * properties.
 *
 * @author Jin Cho, Juhyoung Lee, Jessica Yang
 */
public class GameObject extends Observable implements Serializable {
  private List<String> entityTypes;
  private int id;
  private boolean isActive, isVisible;
  protected Rectangle rect;

  /**
   * Default constructor
   */
  public GameObject(List<String> entityTypes, Vector position, int id, Vector size, boolean visible) {
    this.entityTypes = entityTypes;
    this.id = id;
    isActive = false;
    rect = new Rectangle(size, position);
    isVisible = visible;
  }

  /**
   * Returns x coordinate of position.  // TODO return the position vector instead?
   *
   * @return x coordinate
   */
  public Vector getPosition() {
    return rect.getPosition();
  }

  public void updatePosition() {
    rect.updatePosition();
  }

  protected Rectangle getRect() {
    return rect;
  }

  // TODO REFACTOR FOR reasons
  public Vector getPredictedPosition() {
    return rect.getPredictedPos();
  }

  public void setPredictedPosition(Vector pos) {
    rect.setPredictedPos(pos);
  }

  /**
   *
   */
  public List<String> getEntityType() {
    // TODO implement here
    return entityTypes;
  }

  public int getId() {
    return id;
  }

  public Vector getSize() {
    return rect.getSize();
  }

  /**
   *
   * @param elapsedTime
   * @param gameGravity
   */
  public void step(double elapsedTime, double gameGravity) {
    // TODO :D
  }

  public Vector getVelocity() {
    return new Vector(0,0);
  }

  public void setActive(boolean activeState) {
    isActive = activeState;
    notifyListeners("changeVisibility", null, isActive && isVisible);
  }

  /**
   * Converts model coordinates to view coordinates, and sends GameObject position.
   */
  public void sendToView(Vector frameTopL) {
    // TODO LOGIC
    double viewPositionX;
    double viewPositionY;
    viewPositionX = rect.getPosition().getX() - frameTopL.getX();
    viewPositionY = rect.getPosition().getY() - frameTopL.getY();
    notifyListeners("changeX", null, viewPositionX);
    notifyListeners("changeY", null, viewPositionY);
  }
}
