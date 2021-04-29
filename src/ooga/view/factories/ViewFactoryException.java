package ooga.view.factories;

import org.w3c.dom.Element;

/**
 * Exception class for the view factory packages to standardize exceptions begin thrown.
 *
 * @author Adam Hufstetler
 */
public class ViewFactoryException extends Exception {

  /**
   * Creates an exception when a cause and file element location is known
   *
   * @param cause String
   * @param loc element that is causing the exception
   */
  public ViewFactoryException(String cause, Element loc) {
    super(cause + " caused by " + loc.getNodeName());
  }

  /**
   * Creates an exception when only a cause is known
   *
   * @param cause String
   */
  public ViewFactoryException(String cause) {
    super(cause);
  }
}
