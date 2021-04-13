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
    Vector[] collisionRect = determineCollisionRectangle(myself, o);
    if (collisionRect == null) {
      return ret;
    }

    return getCollisionMethods(myself, o.getEntityType(), collisionRect);
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

  /**
   * returns the top left and bottom right coord of the rectangle made by the intersection
   * @param myself
   * @param o
   * @return ret[0] is top left, ret[1] is bot right
   */
  private Vector[] determineCollisionRectangle(GameObject myself, GameObject o) {
    double x5 = Math.max(myself.getPosition().getX(), o.getPosition().getX());
    double y5 = Math.max(myself.getPosition().getY(), o.getPosition().getY());
    Vector myBotRight = myself.getPosition().add(myself.getSize());
    Vector oBotRight = o.getPosition().add(o.getSize());
    double x6 = Math.min(myBotRight.getX(), oBotRight.getX());
    double y6 = Math.min(myBotRight.getY(), oBotRight.getY());
    if (x5 > x6 || y5 > y6) {
      return null;
    }

    Vector[] ret = new Vector[2];
    ret[0] = new Vector(x5, y5);
    ret[1] = new Vector(x6, y6);
    return ret;
  }

  private GameObject determineCollider(GameObject myself, GameObject o) {
    //TODO: 
    double oX = o.getPosition().getX();
    double oY = o.getPosition().getY();
    double oWidth = o.getSize().getX();
    double oHeight = o.getSize().getY();
    double myX = myself.getPosition().getX();
    double myY = myself.getPosition().getY();
    double myWidth = myself.getSize().getX();
    double myHeight = myself.getSize().getY();

    //add that object to the array
    return null;
  }

  // determine directionality of collision
  private List<String> getCollisionMethods(GameObject myself, List<String> oTags, Vector[] collisionBox) {
    // send to method map
    // TODO REFACTOR
    Vector collisionCenter = (collisionBox[0].add(collisionBox[1]))
        .multiply(new Vector(0.5, 0.5));
    Vector myCenter = (myself.getPosition().add(myself.getSize().multiply(new Vector(0.5, 0.5))));

    List<Vector> edgeMidpoints = getEdgeMidpoints(myself);
    double minDistance = Double.MAX_VALUE;
    Vector minDistanceEdge = new Vector(-1, -1);
    for (Vector edgeMidpoint : edgeMidpoints) {
      double dist = collisionCenter.subtract(edgeMidpoint).calculateMagnitude();
      if (dist < minDistance) {
        minDistance = dist;
        minDistanceEdge = edgeMidpoint;
      }
    }

    Vector edgeDirection = minDistanceEdge.subtract(myCenter).toUnit();

//    System.out.println("getCollisionMethods");
//    Vector myVelocity = myself.getVelocity();
//    Vector oVelocity = o.getVelocity();
//
//    Action myDirection = myVelocity.getDirection();
//    Action oDirection = oVelocity.getDirection();
//    String collisionDirection = "";
//
//    if (myDirection.equals(oDirection)) {
//      System.out.println("same direction");
//      if (!myVelocity.equals(new Vector(0,0))) {
//        System.out.println(oDirection.toString());
//        collisionDirection = oDirection.toString();
//      } else {
//        System.out.println("my velo is 0");
//        return new ArrayList<>();
//      }
//    } else if(myDirection.sameAxis(oDirection)){
//      collisionDirection = myVelocity.getDirection().toString();
//    } else {
//      String collisionAxis = collisionAxis(myself.getPosition(), o.getPosition());
//      if (myDirection.getAxis().equals(collisionAxis)) {
//        collisionDirection = myVelocity.getDirection().toString();
//      }
//      else {
//        collisionDirection = oVelocity.multiply(new Vector(-1, -1)).getDirection().toString();
//      }
//    }
//    List<String> ret = new ArrayList<>();
//    for (String s : o.getEntityType()) {
//      ret.add(s + "-" + collisionDirection);
//    }
//    System.out.println(ret);
//    return ret;
  }

  private List<Vector> getEdgeMidpoints(GameObject o) {
    List<Vector> edges = new ArrayList<>();

    Vector center = new Vector(o.getPosition().getX() + 0.5 * o.getSize().getX(), o.getPosition().getY() + 0.5 * o.getSize().getY());
    edges.add(center.add(new Vector(0, -0.5 * o.getSize().getY())));
    edges.add(center.add(new Vector(0, 0.5 * o.getSize().getY())));
    edges.add(center.add(new Vector(-0.5 * o.getSize().getX(), 0)));
    edges.add(center.add(new Vector(0.5 * o.getSize().getX(), 0)));

    return edges;
  }

  private String collisionAxis(Vector myPos, Vector oPos) {
    if (myPos.getY() > oPos.getY() || myPos.getY() < oPos.getY()) { return "y"; }
    if (myPos.getX() > oPos.getX() || myPos.getX() < oPos.getX()) { return "x"; }
    return "x oy y";
  }

}


