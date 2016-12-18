package es.udc.pa.pa015.practicapa.model.userservice;

/**
 * Exception that thrown out when the password isn't correct.
 */
@SuppressWarnings("serial")
public class IncorrectPasswordException extends Exception {

  /** LoginName. */
  private String loginName;

  /**
   * Exception constructor.
   * @param loginNameParam
   *            loginName
   */
  public IncorrectPasswordException(final String loginNameParam) {
    super("Incorrect password exception => loginName = " + loginNameParam);
    this.loginName = loginNameParam;
  }

  /**
   * Get loginName.
   * @return loginName
   */
  public final String getLoginName() {
    return loginName;
  }

}
