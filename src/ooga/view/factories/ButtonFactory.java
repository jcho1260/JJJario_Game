package ooga.view.factories;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ButtonFactory {
  private final ResourceBundle buttonKeys = ResourceBundle.getBundle("view/images/factory_bundles/ButtonKeys");

  public Button makeButton(Element buttonElem)
      throws NoSuchMethodException, ClassNotFoundException, InvocationTargetException, IllegalAccessException {
    Button button = new Button();
    NodeList nl = buttonElem.getChildNodes();

    for (int i = 0; i < nl.getLength(); i++) {
      Node tempNode = nl.item(i);
      if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
        Method buttonMethod = xmlTagToMethod(tempNode);
        buttonMethod.setAccessible(true);
        Method parseMethod = getTagRetrieval(tempNode);
        parseMethod.setAccessible(true);
        buttonMethod.invoke(this, button, parseMethod.invoke(this, tempNode));
      }
    }

    return button;
  }

  private Method getTagRetrieval(Node n) throws NoSuchMethodException {
    String dataType = buttonKeys.getString(n.getNodeName().toUpperCase() + "_PARAM");
    String mName = "get" + dataType + "FromTag";
    return ButtonFactory.class.getDeclaredMethod(mName, Node.class);
  }

  private Method xmlTagToMethod(Node n) throws NoSuchMethodException, ClassNotFoundException {
    String mInfo = buttonKeys.getString(n.getNodeName().toUpperCase());
    return getButtonMethod(mInfo);
  }

  private Method getButtonMethod(String mInfo) throws ClassNotFoundException, NoSuchMethodException {
    String[] mSplit = mInfo.split("_");
    Class<?>[] cArr = getButtonMethodParamTypes(mSplit[1]);
    return ButtonFactory.class.getDeclaredMethod(mSplit[0], cArr);
  }

  private Class<?>[] getButtonMethodParamTypes(String cStr) throws ClassNotFoundException {
    String[] cStrArr = cStr.split("\\|");
    Class<?>[] cArr = new Class[cStrArr.length];
    for (int i = 0; i < cStrArr.length; i++) {
      cArr[i] = Class.forName(cStrArr[i]);
    }
    return cArr;
  }

  private String getStringFromTag(Node n) {
    return n.getTextContent();
  }

  private double getDoubleFromTag(Node n) {
    return Double.parseDouble(n.getTextContent());
  }

  private void setButtonText(Button b, String text) {
    b.setText(text);
  }

  private void setButtonHeight(Button b, Double height) {
    b.setPrefHeight(height);
  }

  private void setButtonWidth(Button b, Double width) {
    b.setPrefWidth(width);
  }

  private void setButtonImage(Button b, String imagePath) {
    Image image = new Image(
        Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath)));
    ImageView imageView = new ImageView(image);
    imageView.setPreserveRatio(true);
    imageView.setFitHeight(80);
    b.setGraphic(imageView);
  }

  private void setButtonStyle(Button b, String stylePath) {
    b.getStylesheets().add(stylePath);
  }
}
