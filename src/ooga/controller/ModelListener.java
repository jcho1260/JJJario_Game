package ooga.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import ooga.model.util.Vector;

/**
 * Model listener communicated changes in the model to the controller. It is dependent on ooga.model.Vector
 */
public class ModelListener implements PropertyChangeListener {

  double score;
  double health;
  double lives;
  Vector creatableLocation;
  Controller controller;

  /**
   * Gets the current score of the game
   *
   * @return  score
   */
  public double getScore() {
    return score;
  }

  /**
   * Gets the current health of the player
   *
   * @return  health
   */
  public double getHealth() {
    return health;
  }

  /**
   * Gets the current number of lives of the player
   *
   * @return  number of lives
   */
  public double getLives() {
    return lives;
  }

  /**
   * Gets the location of the current creatable
   *
   * @return  position of the current creatable
   */
  public Vector getCreatableLocation() {
    return creatableLocation;
  }

  /**
   * Sets the controller
   *
   * @param controller  current controller
   */
  public void addController(Controller controller) {
    this.controller = controller;
  }

  /**
   * Resets the score
   */
  public void reset() {
    score = 0;
  }

  /**
   * Handles incoming events
   *
   * @param evt incoming PropertyChangeEvent
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals("score")) {
      score = (double) evt.getNewValue();
    }
    if (evt.getPropertyName().equals("newMovingDestroyable")) {
      if (controller != null) {
        creatableLocation = (Vector) evt.getNewValue();
        controller.addCreatable(creatableLocation);
      }
    }
    if (evt.getPropertyName().equals("changeHealth")) {
      health = (double) evt.getNewValue();
    }
    if (evt.getPropertyName().equals("changeLife")) {
      lives = (double) evt.getNewValue();
    }
  }
}
