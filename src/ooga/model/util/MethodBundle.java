package ooga.model.util;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * This class holds the information for methods and their parameters to be reflectively called and also
 * prepares them to be invoked using the Method class. This class was used after parsing through the collision
 * methods between game objects to store the information in preparation for reflective calls when collisions were
 * handled.
 *
 * @author jincho juhyounglee
 */
public class MethodBundle implements Serializable {

  private final String methodName;
  private final double[] parameters;

  /**
   * constructor for the MethodBundle object
   * @param methodName name of method to be reflectively called
   * @param parameters parameters required when invoking the method in reflection. all must be doubles
   */
  public MethodBundle(String methodName, double[] parameters) {
    this.methodName = methodName;
    this.parameters = parameters;
  }

  /**
   * creates an object of type java.lang.reflect.Method using the methodName, parameters, and given Class
   * to prepare for reflective invoking. Requires that the parameters of the method are of Double type as the
   * purpose of the MethodBundle class is for collision handling in which all methods have no parameters or
   * only parameter type double.
   *
   * @param destroyableClass class in which the method to be invoked is defined
   * @return Method object that can be invoked using the parameters defined
   * @throws NoSuchMethodException if the parameter count or type does not match or if the method is not defined at all in the given class
   */
  public Method makeMethod(Class destroyableClass) throws NoSuchMethodException {
    Class[] paramTypes = new Class[parameters.length];
    Double doubleClass = 1.0;
    for (int i = 0; i < parameters.length; i++) {
      paramTypes[i] = doubleClass.getClass();
    }
    try {
      Method collisionResponse = destroyableClass.getMethod(methodName, paramTypes);
      return collisionResponse;
    } catch (Exception e) {

      throw new NoSuchMethodException("method doesn't exist sorry :(");
    }
  }

  /**
   * returns the parameters defined for this bundle
   * @return array of doubles that are each the parameter(s) of the method represented by this bundle
   */
  public Double[] getParameters() {
    Double[] d = new Double[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      d[i] = parameters[i];
    }
    return d;
  }
}
