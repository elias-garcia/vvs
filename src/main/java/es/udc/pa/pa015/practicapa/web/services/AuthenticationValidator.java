package es.udc.pa.pa015.practicapa.web.services;

import es.udc.pa.pa015.practicapa.web.util.UserSession;

import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.MetaDataLocator;

public class AuthenticationValidator {

  private static final String LOGIN_PAGE = "user/Login";

  private static final String INIT_PAGE = "Index";

  private static final String NOT_ALLOWED = "NotAllowed";

  public static final String 
                  PAGE_AUTHENTICATION_TYPE = "page-authentication-type";
  public static final String 
        EVENT_HANDLER_AUTHENTICATION_TYPE = "event-handler-authentication-type";

  /**
   * This method check for a page.
   * @param pageName
   *          the page name to check
   * @param applicationStateManager
   *          application state manager
   * @param componentSource
   *          component source
   * @param locator
   *          metaDataLocator
   * @return String with the page
   */
  public static String checkForPage(String pageName,
      ApplicationStateManager applicationStateManager,
      ComponentSource componentSource, MetaDataLocator locator) {

    String redirectPage = null;
    Component page = componentSource.getPage(pageName);
    try {
      String policyAsString = locator.findMeta(PAGE_AUTHENTICATION_TYPE, page
          .getComponentResources(), String.class);

      AuthenticationPolicyType policy = AuthenticationPolicyType.valueOf(
          policyAsString);
      redirectPage = check(policy, applicationStateManager);
    } catch (RuntimeException e) {
      System.out.println("Page: '" + pageName + "': " + e.getMessage());
    }
    return redirectPage;

  }

  /**
   * this method check for component event.
   * @param pageName
   *          Page name
   * @param componentId
   *          Component id to check
   * @param eventId
   *          Event Id
   * @param eventType
   *          Event type
   * @param applicationStateManager
   *          ApplicationSateManager
   * @param componentSource
   *          ComponentSource
   * @param locator
   *          MetaDataLocator
   * @return String with the page
   */
  public static String checkForComponentEvent(String pageName,
      String componentId, String eventId, String eventType,
      ApplicationStateManager applicationStateManager,
      ComponentSource componentSource, MetaDataLocator locator) {

    String redirectPage = null;
    String authenticationPolicyMeta = EVENT_HANDLER_AUTHENTICATION_TYPE + "-"
        + eventId + "-" + eventType;
    authenticationPolicyMeta = authenticationPolicyMeta.toLowerCase();

    Component component = null;
    if (componentId == null) {
      component = componentSource.getPage(pageName);
    } else {
      component = componentSource.getComponent(pageName + ":" + componentId);
    }
    try {
      String policyAsString = locator.findMeta(authenticationPolicyMeta,
          component.getComponentResources(), String.class);
      AuthenticationPolicyType policy = AuthenticationPolicyType.valueOf(
          policyAsString);
      redirectPage = AuthenticationValidator.check(policy,
          applicationStateManager);
    } catch (RuntimeException e) {
      System.out.println("Component: '" + pageName + ":" + componentId + "': "
          + e.getMessage());
    }
    return redirectPage;

  }

  /**
   * This method check the authentication.
   * @param policy
   *          Authentication policy
   * @param applicationStateManager
   *          Authentication state manager
   * @return string
   */
  public static String check(AuthenticationPolicy policy,
      ApplicationStateManager applicationStateManager) {

    if (policy != null) {
      return check(policy.value(), applicationStateManager);
    } else {
      return null;
    }

  }

  /**
   * This method check the authentication.
   * @param policyType
   *          Authentication policy type
   * @param applicationStateManager
   *          Application state manager
   * @return string
   */
  public static String check(AuthenticationPolicyType policyType,
      ApplicationStateManager applicationStateManager) {
    String redirectPage = null;

    boolean userAuthenticated = applicationStateManager.exists(
        UserSession.class);

    switch (policyType) {

    case AUTHENTICATED_USERS:

      if (!userAuthenticated) {
        redirectPage = LOGIN_PAGE;
      }
      break;

    case NON_AUTHENTICATED_USERS:

      if (userAuthenticated) {
        redirectPage = INIT_PAGE;
      }
      break;

    case ADMIN_USER:

      if (!userAuthenticated) {
        redirectPage = NOT_ALLOWED;
      }
      if (userAuthenticated) {
        UserSession userSession = applicationStateManager.get(
            UserSession.class);
        if (!userSession.getAdmin()) {
          redirectPage = NOT_ALLOWED;
        }
      }
      break;

    case NON_ADMIN_AUTHENTICATED_USERS:

      if (!userAuthenticated) {
        redirectPage = NOT_ALLOWED;
      }
      if (userAuthenticated) {
        UserSession userSession = applicationStateManager.get(
            UserSession.class);
        if (userSession.getAdmin()) {
          redirectPage = NOT_ALLOWED;
        }
      }
      break;

    default:

      break;

    }

    return redirectPage;
  }

}
