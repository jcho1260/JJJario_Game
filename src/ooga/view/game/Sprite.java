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
 *
 */
public class Sprite implements PropertyChangeListener {

  private final ImageView imageView;

  /**
   * @param gameName
   * @param imageName
   * @param w
   * @param h
   * @param x
   * @param y
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
   * @param evt
   */
  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String mName = evt.getPropertyName();
    Object[] mArgs = new Object[]{evt.getNewValue()};
    try {
      new Statement(this, mName, mArgs).execute();
    } catch (Exception e) {
      e.printStackTrace();
      new ExceptionView().displayError(e);
    }
  }

  /**
   * @return
   */
  public ImageView getImageView() {
    return this.imageView;
  }

  /**
   * @param h
   */
  public void changeHeight(Double h) {
    imageView.setFitHeight(h);
  }

  /**
   * @param w
   */
  public void changeWidth(Double w) {
    imageView.setFitWidth(w);
  }

  /**
   * @param b
   */
  public void changeVisibility(Boolean b) {
    imageView.setVisible(b);
  }

  /**
   * @param x
   */
  public void changeX(Double x) {
    imageView.setLayoutX(x);
  }

  /**
   * @param y
   */
  public void changeY(Double y) {
    imageView.setLayoutY(y);
  }
}
