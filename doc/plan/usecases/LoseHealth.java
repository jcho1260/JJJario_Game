class GameObject {
  double[] location = new double[2];
  int health;

  public handleCollision(GameObject b) {
    int oldHealth = health;
    incrementHealth(-3);
    notifyListeners("health", oldHealth, health);
  }
}