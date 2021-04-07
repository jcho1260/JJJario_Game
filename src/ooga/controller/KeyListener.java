package ooga.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class KeyListener implements PropertyChangeListener {

  private KeyPress current;

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    KeyEvent key = (KeyEvent) evt.getNewValue();

    KeyCode code = key.getCode();
    EventType<KeyEvent> event = key.getEventType();

    if (event == KeyEvent.KEY_PRESSED && isValid(code)) {
      current = getKey(code);
    } else if (event == KeyEvent.KEY_RELEASED && getKey(code) == current &&isValid(code)) {
      current = KeyPress.NONE;
    }
  }

  public KeyPress getCurrentKey() {
    return current;
  }

  private KeyPress getKey(KeyCode code) {
    return switch (code) {
      case W -> KeyPress.UP;
      case A -> KeyPress.LEFT;
      case S -> KeyPress.DOWN;
      case D -> KeyPress.RIGHT;
      default -> null;
    };
  }

  private boolean isValid(KeyCode code) {
    return switch (code) {
      case W, A, S, D -> true;
      default -> false;
    };
  }
}
