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
    controller.saveProfile("noah", defaultProfile);

    Profile fetched = controller.getProfile("noah");

    assertEquals("noah", fetched.name());
    assertEquals("profiles/pictures/default.png", fetched.picture());
    assertEquals(fetched.keybinds().get(KeyCode.W), Action.UP);
    assertEquals(fetched.keybinds().get(KeyCode.A), Action.LEFT);
    assertEquals(fetched.keybinds().get(KeyCode.S), Action.DOWN);
    assertEquals(fetched.keybinds().get(KeyCode.D), Action.RIGHT);

  }
}
