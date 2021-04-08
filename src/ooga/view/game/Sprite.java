package ooga.view.game;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.Statement;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.util.Vector;

public class Sprite implements PropertyChangeListener {
  private final ImageView imageView;

  public Sprite(String imageName, double h, double w, double x, double y) {
    imageView = new ImageView();
    ResourceBundle imgKeys = ResourceBundle
        .getBundle("view_resources/game/SpriteImageKeys/JJJarioSpriteKeys");
    String imgPath = imgKeys.getString(imageName.toUpperCase());
    Image img =  new Image(
        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imgPath)));
    imageView.setImage(img);
    changeVisibility(false);
    changeWidth(w);
    changeHeight(h);
    imageView.setX(x);
    imageView.setY(y);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) { // propertyName = changeHeight, oldValue = null, newValue = 100
    String mName = evt.getPropertyName();
    Object[] mArgs = new Object[]{evt.getNewValue()};
    new Statement(this, mName, mArgs);
  }

  public ImageView getImageView() { return this.imageView; }

  private Object[] parseEventArgs(String evtArgs) {
    return evtArgs.split("\\|");
  }

  private void changeHeight(double h) { imageView.setFitHeight(h); }

  private void changeWidth(double w) { imageView.setFitWidth(w); }

  private void changeVisibility(boolean b) { imageView.setVisible(b); }

  private void changePos(Vector<Double> v) {
    imageView.setX(v.get(0));
    imageView.setY(v.get(1));
  }
}
