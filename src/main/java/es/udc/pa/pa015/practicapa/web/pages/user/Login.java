package es.udc.pa.pa015.practicapa.web.pages.user;

import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa015.practicapa.model.userservice.IncorrectPasswordException;
import es.udc.pa.pa015.practicapa.model.userservice.UserService;
import es.udc.pa.pa015.practicapa.web.pages.Index;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa015.practicapa.web.util.CookiesManager;
import es.udc.pa.pa015.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;

/**
 * Class of the login page.
 */
@AuthenticationPolicy(AuthenticationPolicyType.NON_AUTHENTICATED_USERS)
public class Login {

  /** loginName. */
  @Property
  private String loginName;

  /** password. */
  @Property
  private String password;

  /** indicates if the password must remember. */
  @Property
  private boolean rememberMyPassword;

  /** userSession. */
  @SessionState(create = false)
  private UserSession userSession;

  /** cookies. */
  @Inject
  private Cookies cookies;

  /** loginForm. */
  @Component
  private Form loginForm;

  /** Messages. */
  @Inject
  private Messages messages;

  /** userService. */
  @Inject
  private UserService userService;

  /** userProfile. */
  private UserProfile userProfile = null;

  /**
   * Method to validate the login form.
   */
  final void onValidateFromLoginForm() {

    if (!loginForm.isValid()) {
      return;
    }

    try {
      userProfile = userService.login(loginName, password, false);
    } catch (InstanceNotFoundException e) {
      loginForm.recordError(messages.get("error-authenticationFailed"));
    } catch (IncorrectPasswordException e) {
      loginForm.recordError(messages.get("error-authenticationFailed"));
    }

  }

  /**
   * Method when the result is success.
   * @return index class
   */
  final Object onSuccess() {

    userSession = new UserSession();
    userSession.setUserProfileId(userProfile.getUserProfileId());
    userSession.setFirstName(userProfile.getFirstName());
    if (userProfile.getLoginName().equals("admin")) {
      userSession.setAdmin(true);
    } else {
      userSession.setAdmin(false);
    }

    if (rememberMyPassword) {
      CookiesManager.leaveCookies(cookies, loginName, userProfile
          .getEncryptedPassword());
    }
    return Index.class;

  }

}
