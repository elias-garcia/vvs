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

public class SessionFilter implements RequestFilter {

  private ApplicationStateManager applicationStateManager;
  private Cookies cookies;
  private UserService userService;

  /**
   * SessionFilter constructor.
   * @param applicationStateManager
   *          ApplicationStateManager
   * @param cookies
   *          Cookies
   * @param userService
   *          UserService
   */
  public SessionFilter(ApplicationStateManager applicationStateManager,
      Cookies cookies, UserService userService) {

    this.applicationStateManager = applicationStateManager;
    this.cookies = cookies;
    this.userService = userService;

  }

  /**
   * This method service.
   */
  public boolean service(Request request, Response response,
      RequestHandler handler) throws IOException {

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
