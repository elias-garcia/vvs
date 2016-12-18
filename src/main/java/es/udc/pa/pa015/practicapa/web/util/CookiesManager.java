package es.udc.pa.pa015.practicapa.web.util;

import org.apache.tapestry5.services.Cookies;

/**
 * Cookies manager class.
 */
public class CookiesManager {

  /** loginName cookie. */
  private static final String LOGIN_NAME_COOKIE = "loginName";

  /** encrypted password cookie. */
  private static final String ENCRYPTED_PASSWORD_COOKIE = "encryptedPassword";

  /** remember my password age. */
  private static final int REMEMBER_MY_PASSWORD_AGE = 30 * 24 * 3600;

  /**
   * This method leave the cookies.
   * @param cookies
   *          Cookies to leave
   * @param loginName
   *          Session login name
   * @param encryptedPassword
   *          Encrypted password
   */
  public static void leaveCookies(final Cookies cookies, final String loginName,
      final String encryptedPassword) {

    cookies.getBuilder(LOGIN_NAME_COOKIE, loginName).setMaxAge(
        REMEMBER_MY_PASSWORD_AGE).write();
    cookies.getBuilder(ENCRYPTED_PASSWORD_COOKIE, encryptedPassword).setMaxAge(
        REMEMBER_MY_PASSWORD_AGE).write();

  }

  /**
   * Remove cookies.
   * @param cookies Cookies
   */
  public static void removeCookies(final Cookies cookies) {
    cookies.removeCookieValue(LOGIN_NAME_COOKIE);
    cookies.removeCookieValue(ENCRYPTED_PASSWORD_COOKIE);
  }

  /**
   * Get loginName.
   * @param cookies Cookies
   * @return String of the cookie
   */
  public static String getLoginName(final Cookies cookies) {
    return cookies.readCookieValue(LOGIN_NAME_COOKIE);
  }

  /**
   * Get encryptedPassword.
   * @param cookies Cookies
   * @return String of the cookie
   */
  public static String getEncryptedPassword(final Cookies cookies) {
    return cookies.readCookieValue(ENCRYPTED_PASSWORD_COOKIE);
  }

}
