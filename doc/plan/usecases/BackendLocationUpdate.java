class GameObject {
  double[] location = new double[2];
  Vector velocity = new Vector();

  public step(int frameCount, List<String> methodNames) {
    Movement moveCalc = new Movement();
    double[] oldLocation = location;
    moveCalc.move(location, veloctity);
    notifyListeners("location", oldLocation, location);
  }
}