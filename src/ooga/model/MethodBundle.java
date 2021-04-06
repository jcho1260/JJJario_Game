package ooga.model;

import java.lang.reflect.Method;

public class MethodBundle {

  private String methodName;
  private double[] parameters;

  public MethodBundle(String methodName, double[] parameters) {
    this.methodName = methodName;
    this.parameters = parameters;
  }

  public Method makeMethod(Actor actor) throws NoSuchMethodException {
    Class[] paramTypes = new Class[parameters.length];
    Double doubleClass = 1.0;
    for(int i = 0; i < parameters.length; i++) {
      paramTypes[i] = doubleClass.getClass();
    }
    try {
      Method collisionResponse = actor.getClass().getDeclaredMethod(methodName, paramTypes);
      return collisionResponse;
    } catch (Exception e){
      throw new NoSuchMethodException("method doesn't exist sorry :(");
    }
  }

  public double[] getParameters() {
    return parameters;
  }

}
