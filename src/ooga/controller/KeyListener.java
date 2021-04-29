package ooga.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ooga.model.util.Action;

/**
 * KeyListener is a PropertyChangeListener used for communicating key presses between the view and
 * controller. It is dependent on ooga.model.util.Action
 *
 * @author Noah Citron
 */
public class KeyListener implements PropertyChangeListener {

  private final Map<KeyCode, Action> keybinds;
  private Action current;

  /**
   * Creates a new KeyListener
   *
   * @param keybinds  a map representing the keybinding of the player profile
   */
  public KeyListener(Map<KeyCode, Action> keybinds) {
    current = Action.NONE;
    this.keybinds = keybinds;
  }

  /**
   * Resets the current key to Action.NONE
   */
  public void reset() {
    current = Action.NONE;
  }

  /**
   * Handles incoming events containing keypress information
   *
   * @param evt the property change event
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    KeyEvent key = (KeyEvent) evt.getNewValue();

    KeyCode code = key.getCode();
    EventType<KeyEvent> event = key.getEventType();

    if (event == KeyEvent.KEY_PRESSED && isValid(code)) {
      current = getKey(code);
    } else if (event == KeyEvent.KEY_RELEASED && getKey(code) == current && isValid(code)) {
      current = Action.NONE;
    }
  }

  /**
   * Gets the current action
   *
   * @return  current action
   */
  public Action getCurrentKey() {
    return current;
  }

  private Action getKey(KeyCode code) {
    return keybinds.get(code);
  }

  private boolean isValid(KeyCode code) {
    return keybinds.containsKey(code);
  }
}
