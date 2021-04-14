package ooga.view.factories;

import org.w3c.dom.Element;

public class ViewFactoryException extends Exception {

  public ViewFactoryException(String cause, Element loc) {
    super(cause + " caused by " + loc.getNodeName());
  }

  public ViewFactoryException(String cause) {
    super(cause);
  }
}
