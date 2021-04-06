package ooga.model;

import java.util.ArrayList;
import java.util.List;

public class DestroyableCollisionHandling {

  /**
   * @param b
   */
  public List<String> isCollision(GameObject b) {
    List<String> ret = new ArrayList<>();
    double bX = b.getPosition().getX();
    double bY = b.getPosition().getY();
    if (bX== position.getX() && bX<= position.getX() + size.getX()) {
      if (bY >= position.getY() && bY <= position.getY() + size.getY()) {
        List<String> collisionInfo = b.getEntityType();
        Vector bDirection = b.getVelocity().multiply(new Vector(-1,-1));
        String collisionDirection = bDirection.getDirection().toString();
        for (String s : collisionInfo) {
          ret.add(s + collisionDirection);
        }
      }
    }
    return ret;
  }

}
