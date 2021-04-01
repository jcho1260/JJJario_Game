class GameWorld {
  public void step(int frameCount) {
    player.step();
    if(!player.isAlive()) {
      gameOver();
    }
    // step through activeGameObjects
  }
}