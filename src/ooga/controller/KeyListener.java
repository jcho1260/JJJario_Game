package ooga.controller;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ooga.model.Action;


public class KeyListener implements PropertyChangeListener {

  private Action current;

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
    return switch (code) {
      case W -> Action.UP;
      case A -> Action.LEFT;
      case S -> Action.DOWN;
      case D -> Action.RIGHT;
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
