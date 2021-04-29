package ooga.view.factories;

import org.w3c.dom.Element;

/**
 *
 */
public class ViewFactoryException extends Exception {

  /**
   * @param cause
   * @param loc
   */
  public ViewFactoryException(String cause, Element loc) {
    super(cause + " caused by " + loc.getNodeName());
  }

  /**
   * @param cause
   */
  public ViewFactoryException(String cause) {
    super(cause);
  }
}
