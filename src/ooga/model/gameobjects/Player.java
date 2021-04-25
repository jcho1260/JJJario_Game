package ooga.model.gameobjects;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import ooga.model.gameobjectcomposites.UserInputActions;
import ooga.model.util.Action;
import ooga.model.util.Vector;

/**
 * Represents Player object that receives movement input from the user.
 *
 * @author Jessica Yang
 */
public class Player extends Destroyable {

  private List<GameObject> activePowerUps;
  private final UserInputActions userActions;
  private Class<?> userInputActions;
  private final double invincibilityLimit;
  private double framesSinceDamage = 0;
  private boolean win;
  private Vector initialPosition;

  /**
   * Default constructor for Player.
   */
  public Player(List<String> entityTypes, Vector initPosition, int id, Vector objSize,
      int startLife, int startHealth, double jumpTime, Vector velocityMagnitude, double gravity,
      Vector drivingVelocity, int continuousJumpLimit, double shootingCooldown, boolean vis, double
      invincibility)
      throws ClassNotFoundException {
    super(entityTypes, initPosition, id, objSize, startLife, startHealth, 0, vis);
    userActions = new UserInputActions(jumpTime, velocityMagnitude, gravity, drivingVelocity,
        continuousJumpLimit, shootingCooldown);
    userInputActions = Class.forName("ooga.model.gameobjectcomposites.UserInputActions");
    invincibilityLimit = invincibility;
    win = false;
    initialPosition = initPosition;
  }

  /**
   * Handles player movement given user input.
   *
   * @param direction
   * @param elapsedTime
   * @param gameGravity
   */
  public void userStep(Action direction, double elapsedTime, double gameGravity, int currentFrame)
      throws NoSuchMethodException, SecurityException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException {
    notifyListenerIndex(0, "changeHealth", null, super.getHealth());
    notifyListenerIndex(0, "changeLife", null, super.getLives());
    framesSinceDamage++;

    String methodName = direction.toString().toLowerCase();

    if (direction.equals(Action.SHOOT)){
      move(Action.NONE, elapsedTime, gameGravity);
      if (userActions.shoot(getPosition().getX(), getPosition().getY(), currentFrame)) {
        notifyListenerIndex(0, "newMovingDestroyable", null, getPosition().add(new Vector(getSize().getX()/2, 0)));
      }
    } else {
      move(direction, elapsedTime, gameGravity);
    }
  }

  private void move(Action direction, double elapsedTime, double gameGravity)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    String methodName = direction.toString().toLowerCase();
    Class<?>[] paramClasses = new Class[2];
    for (int i = 0; i < 2; i++) {
      paramClasses[i] = Double.class;
    }
    Method[] test = userInputActions.getMethods();
    Method moveMethod = userInputActions.getMethod(methodName, paramClasses);
    Vector deltaPosition = (Vector) moveMethod.invoke(userActions, elapsedTime, gameGravity);
    setPredictedPosition(getPredictedPosition().add(deltaPosition));
  }

  /**
   * Collision method for whenever the bottom of player lands on something.
   */
  public void generalBottomCollision() {
    userActions.hitGround();
  }

  /**
   * gets velocity of player
   * @return
   */
  public Vector getVelocity() {return userActions.getVelocity();}

  /**
   * collision method called whenever the player hits the win checkpoint for that level
   */
  public void playerWinsLevel() {
    win = true;
  }

  /**
   * gives winning status of the player to see if player completed level
   * @return
   */
  public boolean getWinStatus() {
    return win;
  }

  /**
   * Increments health of player by given amount. Notifies listeners of change in health, and if
   * applicable, change in lives.
   *
   * @param increment
   */
  @Override
  public void incrementHealth(Double increment) {
    if (canBeHurt(increment)) {
      framesSinceDamage = 0;
      super.incrementHealth(increment);
      notifyListenerIndex(0, "changeHealth", null, super.getHealth());
    }
    checkReSpawn();
  }

  /**
   * Increments lives of a player by a given amount. Notifies listeners of change in lives.
   *
   * @param increment
   */
  @Override
  public void incrementLives(Double increment) {
    if (canBeHurt(increment)) {
      super.incrementLives(increment);
      notifyListenerIndex(0, "changeLife", null, super.getLives());
    }
    checkReSpawn();
  }

  /**
   *
   */
  @Override
  public void kill() {
    super.kill();
    checkReSpawn();
  }

  private void checkReSpawn() {
    if(health.getLives()>0) {
      rect.setPredictedPos(initialPosition);
    }
  }

  private boolean canBeHurt(double value) {
    return value < 0 && framesSinceDamage > invincibilityLimit;
  }

  /**
   * Scales velocity by given amount.
   *
   * @param x
   * @param y
   */
  public void scaleVelocity(Double x, Double y) {
    Vector newVelocity = userActions.getVelocity().multiply(new Vector(x, y));
    userActions.setVelocity(newVelocity);
  }

  /**
   * scales the size the player
   * @param scaleFactor factor to scale by
   */
  public void scaleSize(Double scaleFactor) {
    getRect().scaleSize(scaleFactor);
    notifyListeners("changeX", null, getPredictedPosition().getX());
    notifyListeners("changeY", null, getPredictedPosition().getY());
    notifyListeners("changeWidth", null, getSize().getX());
    notifyListeners("changeHeight", null, getSize().getX());
  }
}