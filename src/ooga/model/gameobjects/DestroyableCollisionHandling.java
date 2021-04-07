package ooga.model.gameobjects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import ooga.model.util.MethodBundle;
import ooga.model.util.Vector;

public class DestroyableCollisionHandling {

  public DestroyableCollisionHandling() { }

  /**
   * Checks for collision between two objects and returns second object entity tags if they
   * collided. Returns empty list if there is no collision.
   *
   * @param myself reference GameObject
   * @param o secondary GameObject
   * @return secondary GameObject's entity tags or empty list if no collision
   */
  public List<String> determineCollisionMethods(GameObject myself, GameObject o) {
    List<String> ret = new ArrayList<>();
    if (!isCollision(myself, o)) {
      return ret;
    }
    return getCollisionMethods(myself, o);
  }

  /**
   * execute all impacts of collisions with other GameObjects to self
   */
  public void executeCollisions(Object destroyable, Queue<MethodBundle> collisions)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    while (!collisions.isEmpty()) {
      MethodBundle mb = collisions.remove();
      Method m = mb.makeMethod(destroyable.getClass());
      m.invoke(destroyable, mb.getParameters());
    }
  }

  private boolean isCollision(GameObject myself, GameObject o) {
    double oX = o.getPosition().getX();
    double oY = o.getPosition().getY();
    double oWidth = o.getSize().getX();
    double oHeight = o.getSize().getY();
    double myX = myself.getPosition().getX();
    double myY = myself.getPosition().getY();
    double myWidth = myself.getSize().getX();
    double myHeight = myself.getSize().getY();
    if (isXOverlapSides(myX, oX, myWidth, oWidth) || isXCompleteOverlap(myX, oX, myWidth, oWidth)) {
      System.out.println("xMatch\n");
      if (isYOverlapSides(myY, oY, myHeight, oHeight) || isYCompleteOverlap(myY, oY, myHeight, oHeight)) {
        System.out.println("yMatch\n");
        return true;
      }
    }
    return false;
  }

  private boolean isXOverlapSides(double myX, double oX, double myWidth, double oWidth) {
    return (oX <= myX && oX+oWidth <=myX+myWidth) || (oX >= myX && oX+oWidth >=myX+myWidth);
  }

  private boolean isXCompleteOverlap(double myX, double oX, double myWidth, double oWidth) {
    return oX <= myX && oX+oWidth >=myX+myWidth;
  }

  private boolean isYOverlapSides(double myY, double oY, double myHeight, double oHeight) {
    return (oY <= myY && oY + oHeight <= myY + myHeight) || (oY >= myY && oY + oHeight >= myY + myHeight);
  }

  private boolean isYCompleteOverlap(double myY, double oY, double myHeight, double oHeight) {
    return oY <= myY && oY + oHeight >= myY + myHeight;
  }

  private List<String> getCollisionMethods(GameObject myself, GameObject o) {
    Vector myVelocity = myself.getVelocity();
    Vector oVelocity = o.getVelocity();

    String myDirection = myVelocity.getDirection().toString();
    String oDirection = oVelocity.getDirection().toString();
    String collisionDirection = "";

    System.out.println("myDir: "+myDirection);
    System.out.println("oDir: "+oDirection);
    if (myDirection.equals(oDirection)) {
      if (!myVelocity.equals(new Vector(0,0))) {
        collisionDirection = oDirection;
      } else {
        return new ArrayList<>();
      }
    } else {
      collisionDirection = myVelocity.multiply(new Vector(-1,-1)).getDirection().toString();
      System.out.println("collision dir: "+collisionDirection);
    }
    System.out.println(o.getEntityType());

    List<String> ret = new ArrayList<>();
    for (String s : o.getEntityType()) {
      ret.add(s + collisionDirection);
    }
    return ret;
  }
}
