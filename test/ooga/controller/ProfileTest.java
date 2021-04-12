package ooga.controller;

import java.io.IOException;
import javafx.scene.input.KeyCode;
import ooga.model.util.Action;
import ooga.model.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProfileTest {

  Controller controller;

  @BeforeEach
  void beforeEach() {
    controller = new Controller(new Vector(0, 0), 0);
  }

  @Test
  void testProfile() throws IOException, ClassNotFoundException {
    Profile defaultProfile = new Profile("noah");
    controller.setProfile("noah", defaultProfile);

    Profile fetched = controller.getProfile("noah");

    assertEquals("noah", fetched.name());
    assertEquals("profiles/pictures/default.png", fetched.picture());
    assertEquals(KeyCode.W, fetched.keybinds().get(Action.UP));
    assertEquals(KeyCode.A, fetched.keybinds().get(Action.LEFT));
    assertEquals(KeyCode.S, fetched.keybinds().get(Action.DOWN));
    assertEquals(KeyCode.D, fetched.keybinds().get(Action.RIGHT));
  }
}
