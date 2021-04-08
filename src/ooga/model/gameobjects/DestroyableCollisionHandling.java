package ooga.model.gameobjects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import ooga.model.util.Action;
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
      System.out.println(m.getName());
      System.out.println(mb.getParameters().length);
      System.out.println(m.getParameters().length);
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
      if (isYOverlapSides(myY, oY, myHeight, oHeight) || isYCompleteOverlap(myY, oY, myHeight, oHeight)) {
        System.out.print("Is Collision between: "+myself.getEntityType().get(myself.getEntityType().size()-1));
        System.out.println(" and "+o.getEntityType().get(o.getEntityType().size()-1));
        return true;
      }
    }
    return false;
  }

  private boolean isXOverlapSides(double myX, double oX, double myWidth, double oWidth) {
    return (oX <= myX && oX+oWidth <=myX+myWidth && oX+oWidth >=myX) || (oX >= myX && oX <=myX+myWidth  && oX+oWidth >=myX+myWidth);
  }

  private boolean isXCompleteOverlap(double myX, double oX, double myWidth, double oWidth) {
    return oX <= myX && oX+oWidth >=myX+myWidth;
  }

  private boolean isYOverlapSides(double myY, double oY, double myHeight, double oHeight) {
    return (oY <= myY && oY + oHeight <= myY + myHeight && oY + oHeight >= myY) || (oY >= myY && oY + myHeight <= myY && oY + oHeight >= myY + myHeight);
  }

  private boolean isYCompleteOverlap(double myY, double oY, double myHeight, double oHeight) {
    return oY <= myY && oY + oHeight >= myY + myHeight;
  }

  private List<String> getCollisionMethods(GameObject myself, GameObject o) {
    System.out.println("getCollisionMethods");
    Vector myVelocity = myself.getVelocity();
    Vector oVelocity = o.getVelocity();

    Action myDirection = myVelocity.getDirection();
    Action oDirection = oVelocity.getDirection();
    String collisionDirection = "";

    if (myDirection.equals(oDirection)) {
      System.out.println("same direction");
      if (!myVelocity.equals(new Vector(0,0))) {
        System.out.println(oDirection.toString());
        collisionDirection = oDirection.toString();
      } else {
        System.out.println("my velo is 0");
        return new ArrayList<>();
      }
    } else if(myDirection.sameAxis(oDirection)){
      collisionDirection = myVelocity.getDirection().toString();
    } else {
      String collisionAxis = collisionAxis(myself.getPosition(), o.getPosition());
      if (myDirection.getAxis().equals(collisionAxis)) {
        collisionDirection = myVelocity.getDirection().toString();
      }
      else {
        collisionDirection = oVelocity.multiply(new Vector(-1, -1)).getDirection().toString();
      }
    }
    List<String> ret = new ArrayList<>();
    for (String s : o.getEntityType()) {
      ret.add(s + collisionDirection);
    }
    System.out.println(ret);
    return ret;
  }

  private String collisionAxis(Vector myPos, Vector oPos) {
    if (myPos.getY() > oPos.getY() || myPos.getY() < oPos.getY()) { return "y"; }
    if (myPos.getX() > oPos.getX() || myPos.getX() < oPos.getX()) { return "x"; }
    return "x oy y";
  }

}


