package ooga.view.factories;

import java.beans.Statement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.scene.image.Image;
import javafx.scene.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class ComponentFactory {

  private ResourceBundle componentKeys;

  public Object makeComponent(Element elem)
      throws Exception {
    if (elem.getNodeName().equals("Image")) {
      return makeImage(elem);
    }

    String compName = elem.getNodeName();
    componentKeys = ResourceBundle.getBundle("view/factory_bundles/" + compName + "Keys");
    Node component = (Node) makeComponentBase(compName);
    component.setId(elem.getAttribute("id"));
    NodeList nl = elem.getChildNodes();

    for (int i = 0; i < nl.getLength(); i++) {
      org.w3c.dom.Node tempNode = nl.item(i);
      if (tempNode.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
        Element childElem = (Element) tempNode;
        if (hasChildElements(childElem)) {
          editComponentParent(component, childElem);
        } else {
          editComponentLeaf(component, childElem);
        }
      }
    }

    return component;
  }

  private Image makeImage(Element elem) {
    String imagePath = elem.getElementsByTagName("Path").item(0).getTextContent();
    return new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(imagePath)));
  }

  private void editComponentParent(Node component, Element e)
      throws Exception {
    String mName = getMethodNameFromXML(e);
    Object[] mArgs = new Object[]{makeComponent(e)};
    new Statement(component, mName, mArgs).execute();
  }

  private void editComponentLeaf(Node component, Element e)
      throws Exception {
    String mName = getMethodNameFromXML(e);
    Object[] mArgs = getMethodArgsFromXML(e);
    new Statement(component, mName, mArgs).execute();
  }

  private String getMethodNameFromXML(Element e) {
    return componentKeys.getString(e.getNodeName().toUpperCase());
  }

  private Object[] getMethodArgsFromXML(Element e)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method parseMethod = getTagRetrieval(e);
    parseMethod.setAccessible(true);
    return new Object[]{parseMethod.invoke(this, e)};
  }

  private Object makeComponentBase(String compName)
      throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException,
      InvocationTargetException, InstantiationException {
    Class<?> compClass = Class.forName(componentKeys.getString(compName.toUpperCase()));
    return compClass.getDeclaredConstructor().newInstance();
  }

  private Method getTagRetrieval(Element e) throws NoSuchMethodException {
    String dataType = componentKeys.getString(e.getNodeName().toUpperCase() + "_PARAM");
    String mName = "get" + dataType + "FromTag";
    return ComponentFactory.class.getDeclaredMethod(mName, Element.class);
  }

  private boolean hasChildElements(Element el) {
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
