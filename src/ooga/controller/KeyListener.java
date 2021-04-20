package ooga.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ooga.model.util.Action;


public class KeyListener implements PropertyChangeListener {

  private Action current;
  private final Map<KeyCode, Action> keybinds;

  public KeyListener(Map<KeyCode, Action> keybinds) {
    current = Action.NONE;
    this.keybinds = keybinds;
  }

  public void reset() {
    current = Action.NONE;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    KeyEvent key = (KeyEvent) evt.getNewValue();

    KeyCode code = key.getCode();
    EventType<KeyEvent> event = key.getEventType();

    if (event == KeyEvent.KEY_PRESSED && isValid(code)) {
      current = getKey(code);
    } else if (event == KeyEvent.KEY_RELEASED && getKey(code) == current &&isValid(code)) {
      current = Action.NONE;
    }
  }

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
