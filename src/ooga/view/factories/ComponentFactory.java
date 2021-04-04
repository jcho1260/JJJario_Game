package ooga.view.factories;

import java.beans.Statement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ResourceBundle;
import javafx.scene.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class ComponentFactory {
  //TODO: FIX THE THROWS
  public abstract Object make(Element e) throws Exception;

  protected void addChild(ResourceBundle rb, Node component, Element e)
      throws Exception {
    String mName = getMethodNameFromXML(rb, e);
    Object[] mArgs = new Object[]{make(e)};
    new Statement(component, mName, mArgs).execute();
  }

  protected void editProperty(ResourceBundle rb, Node component, Element e)
      throws Exception {
    String mName = getMethodNameFromXML(rb, e);
    Object[] mArgs = getMethodArgsFromXML(rb, e);
    new Statement(component, mName, mArgs).execute();
  }

  protected String getMethodNameFromXML(ResourceBundle rb, Element e) {
    return rb.getString(e.getNodeName().toUpperCase());
  }

  protected Object[] getMethodArgsFromXML(ResourceBundle rb, Element e)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method parseMethod = getTagRetrieval(rb, e);
    parseMethod.setAccessible(true);
    return new Object[]{parseMethod.invoke(this, e)};
  }

  protected Object makeComponentBase(ResourceBundle rb, String compName)
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
      InvocationTargetException, InstantiationException {
    Class<?> compClass = Class.forName(rb.getString(compName.toUpperCase()));
    return compClass.getDeclaredConstructor().newInstance();
  }

  protected Method getTagRetrieval(ResourceBundle rb, Element e) throws NoSuchMethodException {
    String dataType = rb.getString(e.getNodeName().toUpperCase() + "_PARAM");
    String mName = "get" + dataType + "FromTag";
    return ComponentFactory.class.getDeclaredMethod(mName, Element.class);
  }

  protected boolean hasChildElements(Element el) {
    NodeList children = el.getChildNodes();
    for (int i = 0; i < children.getLength(); i++) {
      if (children.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
        return true;
      }
    }
    return false;
  }

  private String getStringFromTag(Element e) {
    return e.getTextContent();
  }

  private double getDoubleFromTag(Element e) {
    return Double.parseDouble(e.getTextContent());
  }

  private boolean getBooleanFromTag(Element e) {
    return Boolean.getBoolean(e.getTextContent());
  }
}
