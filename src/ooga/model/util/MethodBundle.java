package ooga.model.util;

import java.lang.reflect.Method;

public class MethodBundle {

  private String methodName;
  private double[] parameters;

  public MethodBundle(String methodName, double[] parameters) {
    this.methodName = methodName;
    this.parameters = parameters;
  }

  public Method makeMethod(Class destroyableClass) throws NoSuchMethodException {
    Class[] paramTypes = new Class[parameters.length];
    Double doubleClass = 1.0;
    for(int i = 0; i < parameters.length; i++) {
      paramTypes[i] = doubleClass.getClass();
    }
    try {
      Method collisionResponse = destroyableClass.getMethod(methodName, paramTypes);
      return collisionResponse;
    } catch (Exception e){
      throw new NoSuchMethodException("method doesn't exist sorry :(");
    }
  }

  public Double[] getParameters() {
    Double[] d = new Double[parameters.length];
    for (int i = 0; i < parameters.length; i++) {
      d[i] = (Double) parameters[i];
    }
    return d;
  }

  public String getMethodName() {return methodName;}

}
