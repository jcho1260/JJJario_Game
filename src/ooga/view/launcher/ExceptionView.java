package ooga.view.launcher;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * This class is used to display errors thrown during the runtime fo the application to the user.
 * Standard usage of this class is to instantiate a new instance for the error and then call the
 * either of the methods on that instance.
 *
 * @author Adam Hufstetler
 */
public class ExceptionView {

  /**
   * Displays an error to the user that occurred during runtime.
   *
   * @param e Exception
   */
  public void displayError(Exception e) {
    Alert alert = new Alert(AlertType.ERROR);
    alert.setTitle("Error Dialog");
    alert.setHeaderText("Runtime Error");
    alert.setContentText(e.getMessage());

    alert.showAndWait();
  }

  /**
   * Displays a warning to the user that needs to be known during runtime
   *
   * @param heading String
   * @param message String
   */
  public void displayWarning(String heading, String message) {
    Alert alert = new Alert(AlertType.WARNING);
    alert.setTitle("Error Dialog");
    alert.setHeaderText(heading);
    alert.setContentText(message);

    alert.showAndWait();
  }
}
