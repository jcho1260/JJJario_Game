package ooga.controller;

import java.lang.reflect.Method;

public class MethodBundle {

  private String methodName;
  private double[] parameters;

  public MethodBundle(String methodName, double[] parameters) {
    this.methodName = methodName;
    this.parameters = parameters;
  }
}
