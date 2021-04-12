package ooga.view.factories;

import java.beans.Statement;
import java.lang.reflect.Method;
import java.util.ResourceBundle;
import javafx.scene.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public abstract class ComponentFactory {

  //TODO: FIX THE THROWS
  public abstract Object make(Element e) throws ViewFactoryException;

  protected void addChild(ResourceBundle rb, Node component, Element e)
      throws ViewFactoryException {
    String mName = getMethodNameFromXML(rb, e);
    Object[] mArgs = new Object[]{make(e)};
    try {
      new Statement(component, mName, mArgs).execute();
    } catch (Exception exception) {
      throw new ViewFactoryException("Could not find method " + mName, e);
    }
  }

  protected void editProperty(ResourceBundle rb, Node component, Element e)
      throws ViewFactoryException {
    String mName = getMethodNameFromXML(rb, e);
    Object[] mArgs = getMethodArgsFromXML(rb, e);
    try {
      new Statement(component, mName, mArgs).execute();
    } catch (Exception exception) {
      throw new ViewFactoryException("Could not find method " + mName, e);
    }
  }

  protected String getMethodNameFromXML(ResourceBundle rb, Element e) {
    return rb.getString(e.getNodeName().toUpperCase());
  }

  protected Object makeComponentBase(ResourceBundle rb, String compName)
      throws ViewFactoryException {
    try {
      return Class.forName(rb.getString(compName.toUpperCase())).getDeclaredConstructor()
          .newInstance();
    } catch (Exception e) {
      throw new ViewFactoryException(e.getMessage());
    }
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

  private Object[] getMethodArgsFromXML(ResourceBundle rb, Element e)
      throws ViewFactoryException {
    Method parseMethod = getTagRetrieval(rb, e);
    parseMethod.setAccessible(true);
    try {
      return new Object[]{parseMethod.invoke(this, e)};
    } catch (Exception exception) {
      throw new ViewFactoryException(exception.getMessage());
    }
  }

  private Method getTagRetrieval(ResourceBundle rb, Element e) throws ViewFactoryException {
    String dataType = rb.getString(e.getNodeName().toUpperCase() + "_PARAM");
    String mName = "get" + dataType + "FromTag";
    try {
      return ComponentFactory.class.getDeclaredMethod(mName, Element.class);
    } catch (NoSuchMethodException noSuchMethodException) {
      throw new ViewFactoryException("No parse method for " + dataType, e);
    }
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
