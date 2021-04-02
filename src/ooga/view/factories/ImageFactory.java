package ooga.view.factories;

import javafx.scene.image.ImageView;
import org.w3c.dom.Element;

public class ImageFactory {
  public ImageView makeImage(Element imgElem) {
    return null;
  }

  private String getStringFromTag(Element elem, String tag) {
    String s = elem.getElementsByTagName(tag).item(0).getTextContent();
    System.out.println(s);
    return s;
  }
}
