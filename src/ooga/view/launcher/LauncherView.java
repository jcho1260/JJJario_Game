package ooga.view.launcher;

import javafx.scene.Scene;
import javafx.stage.Stage;
import ooga.controller.Controller;
import ooga.view.factories.SceneFactory;

/**
 * This class is the entrance to the user interface side of the application. It is dependant on
 * ooga.controller.Controller and ooga.view.factories.SceneFactory.
 */
public class LauncherView {

  private final Stage stage;

  /**
   * Constructs a LauncherView which can be started later.
   *
   * @param stage Stage
   */
  public LauncherView(Stage stage) {
    this.stage = stage;
  }

  /**
   * Starts the JJJan Launcher user interface.
   *
   * @param controller Controller
   */
  public void start(Controller controller) {
    try {
      SceneFactory sf = new SceneFactory(controller);
      Scene scene = sf.make("resources/view_resources/launcher/LauncherRoot.XML");
      scene.getStylesheets().add("view_resources/color_themes/JJJarioTheme.css");
      stage.setTitle("JJJario Launcher");
      stage.setScene(scene);
      stage.show();
    } catch (Exception e) {
      new ExceptionView().displayError(e);
    }
  }
}
