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

  public Sprite(String spriteType, double h, double w, double x, double y) {
    imageView = new ImageView();
    ResourceBundle imgKeys = ResourceBundle
        .getBundle("view_resources/game/SpriteImageKeys/JJJarioSpriteKeys");
    String imgPath = imgKeys.getString(spriteType);
    Image img =  new Image(
        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imgPath)));
    imageView.setImage(img);
    imageView.setFitHeight(h);
    imageView.setFitWidth(w);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) { // propertyName = changeHeight, oldValue = null, newValue = 100
    String mName = evt.getPropertyName();
    Object[] mArgs = parseEventArgs((String) evt.getNewValue());
    new Statement(this, mName, mArgs);
  }

  private Object[] parseEventArgs(String evtArgs) {
    return evtArgs.split("\\|");
  }

  private void changeHeight(String s) {
    imageView.setFitHeight(Double.parseDouble(s));
  }

  private void changeWidth(String s) {
    imageView.setFitWidth(Double.parseDouble(s));
  }
}
