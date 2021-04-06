package ooga;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import ooga.view.factories.RootFactory;
import ooga.view.launcher.LauncherView;

/**
 * Feel free to completely change this code or delete it entirely.
 */
public class ViewDevMain extends Application {

  /**
   * Start of the program.
   */
  public static void main (String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) {
    new LauncherView().start(primaryStage);
  }
}
