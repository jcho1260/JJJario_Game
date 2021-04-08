package ooga.model.gameobjects;

import java.util.List;
import ooga.Observable;
import ooga.model.util.Vector;
import ooga.model.gameobjectcomposites.Size;
import ooga.model.util.MethodBundle;

/**
 * Represents GameObject - any component of the game that has position and associate movement
 * properties.
 *
 * @author Jin Cho, Juhyoung Lee, Jessica Yang
 */
public class GameObject extends Observable {
  private List<String> entityTypes;
  private Vector position;
  private int id;
  private boolean isActive;
  protected Size size;

  /**
   * Default constructor
   */
  public GameObject(List<String> entityTypes, Vector position, int id, Vector size) {
    this.entityTypes = entityTypes;
    this.position = position.multiply(new Vector(20,20));
    this.id = id;
    this.size = new Size(size.multiply(new Vector(20,20)));
    isActive = false;
  }

  /**
   * Returns x coordinate of position.  // TODO return the position vector instead?
   *
   * @return x coordinate
   */
  public Vector getPosition() {
    return position.copy();
  }

  protected void setPosition(Vector newPosition) {
    Vector prevPosition = position;
    position = newPosition;
    notifyListeners("position", prevPosition, position);
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
    return size.getSize();
  }

  /**
   *
   * @param elapsedTime
   * @param gameGravity
   */
  public void step(double elapsedTime, double gameGravity) {

  }

  public Vector getVelocity() {
    return new Vector(0,0);
  }

  public void setActive(boolean activeState) {
    isActive = activeState;
    notifyListeners("changeVisibility", null, isActive);
  }

  /**
   * Converts model coordinates to view coordinates, and sends GameObject position.
   */
  public void sendToView(Vector frameTopL) {
    // TODO LOGIC
    double viewPositionX;
    double viewPositionY;
    viewPositionX = position.getX() - frameTopL.getX();
    viewPositionY = position.getY() - frameTopL.getY();
    System.out.println("view x: "+viewPositionX+ " view pos y: "+viewPositionY);
    java.util.Vector<Double> viewPosition = new java.util.Vector();
    viewPosition.add(viewPositionX);
    viewPosition.add(viewPositionY);
    notifyListeners("changePos", null, viewPosition);
  }
}
