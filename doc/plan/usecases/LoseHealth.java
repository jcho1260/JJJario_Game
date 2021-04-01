class GameObject {
  double[] location = new double[2];
  int health;
  List<Method> collisionEffects;

  public void handleCollision(GameObject b, List<String> methods) {
    for(String m : methods) {
      Method m = this.getClass().getDeclaredMethod(methodName);
      collisionEffects.add(m);
    }
    for(Method m : collisionEffects) {
      m.invoke(NanoBrowserView.this);
    }
  }

  public void incrementHealth (int change) {
    int oldHealth = health;
    health += change;
    notifyListeners("health", oldHealth, health);
  }
}
