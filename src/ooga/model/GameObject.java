package ooga.model;

import java.util.List;

/**
 * Represents GameObject - any component of the game that has position and associate movement
 * properties.
 *
 * @author Jin Cho, Jessica Yang
 */
public class GameObject {
  private List<String> entityTypes;
  protected int frameCount;
  private PhysicsEngine movement;
  protected Vector position;
  protected Vector velocity;
  protected Vector acceleration;
  protected double gravityScale;
  protected int id;
  protected boolean isActive;
  protected double size;

  /**
   * Default constructor
   */
  public GameObject(List<String> entityTypes, Vector position, Vector velocity, double gravityScale, int id, double size) {
    this.entityTypes = entityTypes;
    this.position = position;
    this.velocity = velocity;
    this.gravityScale = gravityScale;
    this.id = id;
    this.size = size;
    isActive = checkActiveStatus();
  }

  private boolean checkActiveStatus() {
    return true;
  }


  /**
   * Returns x coordinate of position.  // TODO return the position vector instead?
   *
   * @return x coordinate
   */
  public double getX() {
    return position.getX();
  }

  /**
   * Returns y coordinate of position. // TODO return the position vector instead?
   *
   * @return y coordinate
   */
  public double getY() {
    return position.getY();
  }

  /**
   *
   */
  public void getEntityType() {
    // TODO implement here
  }

  /**
   *
   */
  public String getEntitiyState() {
    // TODO implement here
    return null;
  }

  /**
   * @param b
   */
  public boolean isCollision(GameObject b) {
    // TODO implement here
    return true;
  }

  /**
   *
   */
  public boolean isActive() {
    // TODO implement here
    return true;
  }

  /**
   * @param frameCount
   * @param methods
   */
  public void step(int frameCount, List<String> methods) {
    // TODO implement here
  }

  /**
   * @param vel
   */
  public void setVelocity(Vector vel) {
    // TODO implement here
  }

  /**
   * Returns velocity of GameObject for child classes.
   *
   * @return velocity
   */
  protected Vector getVelocity() {
    return velocity;
  }
}
