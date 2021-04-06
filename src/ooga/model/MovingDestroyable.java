package ooga.model;

import java.util.List;

public class MovingDestroyable extends Destroyable{

  /**
   * Default constructor with default lives, health values
   *
   * @param entityTypes
   * @param position
   * @param id
   * @param size
   * @param startLife
   * @param startHealth
   */
  public MovingDestroyable(List<String> entityTypes, Vector position, int id,
      Vector size, int startLife, int startHealth) {
    super(entityTypes, position, id, size, startLife, startHealth);
  }
}
