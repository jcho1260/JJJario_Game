package ooga.model;

import java.util.List;

public class GameObject {

  private List<String> entityTypes;
  protected int frameCount;
  private PhysicsEngine movement;
  protected Vector position;
  protected Vector velocity;
  protected Vector acceleration;
  protected double gravity;

  /**
   * Default constructor
   */
  public GameObject(List<String> entityTypes, Vector position, Vector velocity, double gravity) {

  }


  /**
   *
   */
  public int getX() {
    // TODO implement here
    return 0;
  }

  /**
   *
   */
  public int getY() {
    // TODO implement here
    return 0;
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

}
