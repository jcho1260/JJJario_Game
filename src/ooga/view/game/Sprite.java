package ooga.view.game;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.Statement;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Sprite implements PropertyChangeListener {

  private final ImageView imageView;

  public Sprite(String imageName, double w, double h, double x, double y) {
    imageView = new ImageView();
    imageView.setId(imageName);
    ResourceBundle imgKeys = ResourceBundle
        .getBundle("view_resources/game/SpriteImageKeys/JJJarioSpriteKeys");
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

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    String mName = evt.getPropertyName();
    Object[] mArgs = new Object[]{evt.getNewValue()};
    try {
      new Statement(this, mName, mArgs).execute();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public ImageView getImageView() { return this.imageView; }

  public void changeHeight(Double h) {
    imageView.setFitHeight(h);
  }

  public void changeWidth(Double w) {
    imageView.setFitWidth(w);
  }

  public void changeVisibility(Boolean b) {
    imageView.setVisible(b);
  }

  public void changeX(Double x) {
    imageView.setX(x);
  }

  public void changeY(Double y) {
    imageView.setY(y);
  }
}
