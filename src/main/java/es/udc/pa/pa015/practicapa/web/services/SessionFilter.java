package es.udc.pa.pa015.practicapa.web.services;

import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa015.practicapa.model.userservice.IncorrectPasswordException;
import es.udc.pa.pa015.practicapa.model.userservice.UserService;
import es.udc.pa.pa015.practicapa.web.util.CookiesManager;
import es.udc.pa.pa015.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.Cookies;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;

import java.io.IOException;

/**
 * Session filter class.
 */
public class SessionFilter implements RequestFilter {

  /** application State Manager. */
  private ApplicationStateManager applicationStateManager;

  /** cookies. */
  private Cookies cookies;

  /** userService. */
  private UserService userService;

  /**
   * SessionFilter constructor.
   * @param applicationStateManagerParam
   *          ApplicationStateManager
   * @param cookiesParam
   *          Cookies
   * @param userServiceParam
   *          UserService
   */
  public SessionFilter(
      final ApplicationStateManager applicationStateManagerParam,
      final Cookies cookiesParam, final UserService userServiceParam) {

    this.applicationStateManager = applicationStateManagerParam;
    this.cookies = cookiesParam;
    this.userService = userServiceParam;

  }

  /**
   * This method service.
   * @param request request
   * @param response Response
   * @param handler RequestHandler
   * @return boolean
   * @throws IOException ioException
   */
  public boolean service(final Request request, final Response response,
      final RequestHandler handler) throws IOException {

    if (!applicationStateManager.exists(UserSession.class)) {

      String loginName = CookiesManager.getLoginName(cookies);
      if (loginName != null) {

        String encryptedPassword = CookiesManager.getEncryptedPassword(cookies);
        if (encryptedPassword != null) {

          try {

            UserProfile userProfile = userService.login(loginName,
                encryptedPassword, true);
            UserSession userSession = new UserSession();
            userSession.setUserProfileId(userProfile.getUserProfileId());
            userSession.setFirstName(userProfile.getFirstName());
            if (userProfile.getLoginName().equals("admin")) {
              userSession.setAdmin(true);
            } else {
              userSession.setAdmin(false);
            }
            applicationStateManager.set(UserSession.class, userSession);

          } catch (InstanceNotFoundException e) {
            CookiesManager.removeCookies(cookies);
          } catch (IncorrectPasswordException e) {
            CookiesManager.removeCookies(cookies);
          }

        }

      }

    }

    handler.service(request, response);

    return true;
  }

}
