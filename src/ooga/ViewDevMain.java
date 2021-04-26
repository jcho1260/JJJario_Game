package ooga;


import javafx.application.Application;
import javafx.stage.Stage;
import ooga.controller.Controller;
import ooga.model.util.Vector;
import ooga.view.launcher.ExceptionView;
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
    new LauncherView(primaryStage).start(new Controller(30));
  }
}
