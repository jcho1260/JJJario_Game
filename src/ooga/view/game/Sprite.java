package ooga.view.game;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.Statement;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ooga.view.launcher.ExceptionView;

/**
 * This class is used as the communication liaison between the model and view. All image updates go
 * through this class. It is dependent on ooga.view.launcher.ExceptionView.
 *
 * @author Adam Hufstetler
 */
public class Sprite implements PropertyChangeListener {

  private final ImageView imageView;

  /**
   * Constructs a sprite with the given parameters.
   *
   * @param gameName String
   * @param imageName String
   * @param w width
   * @param h height
   * @param x x-position
   * @param y y-position
   */
  public Sprite(String gameName, String imageName, double w, double h, double x, double y) {
    imageView = new ImageView();
    imageView.setId(imageName);
    ResourceBundle imgKeys = ResourceBundle
        .getBundle("view_resources/game/SpriteImageKeys/" + gameName + "SpriteKeys");
    String imgPath = imgKeys.getString(imageName.toUpperCase());
    Image img = new Image(
        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imgPath)));
    imageView.setImage(img);
    changeVisibility(false);
    changeWidth(w);
    changeHeight(h);
    changeX(x);
    changeY(y);
  }

  /**
   * Used to make changes to the sprite's ImageView.
   *
   * @param evt the property change event that encapsulates the neccesary information.
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String mName = evt.getPropertyName();
    Object[] mArgs = new Object[]{evt.getNewValue()};
    try {
      new Statement(this, mName, mArgs).execute();
    } catch (Exception e) {
      new ExceptionView().displayError(e);
    }
  }

  /**
   * Returns the sprite's ImageView.
   *
   * @return ImageView that the sprite contains
   */
  public ImageView getImageView() {
    return this.imageView;
  }

  /**
   * Changes the sprite's height.
   *
   * @param h Double
   */
  public void changeHeight(Double h) {
    imageView.setFitHeight(h);
  }

  /**
   * Changes the sprite's width.
   *
   * @param w Double
   */
  public void changeWidth(Double w) {
    imageView.setFitWidth(w);
  }

  /**
   * Changes the sprite's visibility.
   *
   * @param b Boolean
   */
  public void changeVisibility(Boolean b) {
    imageView.setVisible(b);
  }

  /**
   * Changes the sprite's x-position.
   *
   * @param x Double
   */
  public void changeX(Double x) {
    imageView.setLayoutX(x);
  }

  /**
   * Changes the sprite's y-position.
   *
   * @param y Double
   */
  public void changeY(Double y) {
    imageView.setLayoutY(y);
  }
}
