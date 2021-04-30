package ooga.view.factories;

import java.beans.Statement;
import java.lang.reflect.Method;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * This class provides the required framework for parsing the data file format used to build javafx
 * frontend components. A class which extends ComponentFactory should implement the make method and
 * use the addChild and editProperty methods to construct a frontend component.
 * ViewFactoryExceptions are thrown when there is an issue parsing the data file or when invalid
 * javafx component changes are attempted.
 *
 * @author Adam Hufstetler
 */
public abstract class ComponentFactory {

  /**
   * Returns an object constructed by parsing the attributes and child elements of e.
   *
   * @param e element which contains the requisite information to construct the expected object
   * @return an object constructed by parsing the attributes and child elements of e
   * @throws ViewFactoryException if the element is not properly formatted or contains requests that
   *                              are unsupported
   */
  public abstract Object make(Element e) throws ViewFactoryException;

  /**
   * Adds a child to the given component constructed by a make() call on the given element using the
   * provided resource bundle. The resource bundle provided is assumed to contain the proper keys to
   * construct the object with the information provided in e.
   *
   * @param rb the resource bundle that contains the class and method information for the given
   *           component to be added to
   * @param component the component to which the new component wil be added to
   * @param e the element that contains the necessary information to make the child component.
   * @throws ViewFactoryException if the element is not properly formatted or contains requests that
   *                              are unsupported
   */
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

  /**
   * Edits a property of the given component using the information provided in the element and the
   * provided resource bundle. The resource bundle provided is assumed to contain the proper keys to
   * edit the property with the information provided in e.
   *
   * @param rb the resource bundle that contains the class and method information for the given
   *           component to be edited
   * @param component the component that will be edited
   * @param e the element that contains the necessary information to edit the component
   * @throws ViewFactoryException if the element is not properly formatted or contains requests that
   *                              are unsupported
   */
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

  private String getMethodNameFromXML(ResourceBundle rb, Element e) throws ViewFactoryException {
    try {
      return rb.getString(e.getNodeName().toUpperCase());
    } catch (MissingResourceException mre) {
      throw new ViewFactoryException(mre.getMessage());
    }
  }

  /**
   * Reflexively constructs and returns an object of the given class name.
   *
   * @param rb resource bundle for the given class
   * @param compName object class name that is tied with the class package path
   * @return class object with default constructor used
   * @throws ViewFactoryException if the resource bundle does not have the neccesary information or
   * if there was a problem during reflection.
   */
  protected Object makeComponentBase(ResourceBundle rb, String compName)
      throws ViewFactoryException {
    try {
      return Class.forName(rb.getString(compName.toUpperCase())).getDeclaredConstructor()
          .newInstance();
    } catch (Exception e) {
      throw new ViewFactoryException(e.getMessage());
    }
  }

  /**
   * Checks if a given XML element has children that are elements.
   *
   * @param el the element to be checked
   * @return true if it has child elements, false if not
   */
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

  String getStringFromTag(Element e) {
    return e.getTextContent();
  }

  double getDoubleFromTag(Element e) {
    return Double.parseDouble(e.getTextContent());
  }

  boolean getBooleanFromTag(Element e) {
    return Boolean.getBoolean(e.getTextContent());
  }

  ObservableList<?> getOALFromTag(Element e) {
    return FXCollections.observableArrayList(e.getTextContent().split(","));
  }
}
