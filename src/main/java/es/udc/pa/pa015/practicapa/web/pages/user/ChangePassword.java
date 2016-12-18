package es.udc.pa.pa015.practicapa.web.pages.user;

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
 * Class of the changePassword page.
 */
@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class ChangePassword {

  /** oldPassword. */
  @Property
  private String oldPassword;

  /** newPassword. */
  @Property
  private String newPassword;

  /** retypeNewPassword. */
  @Property
  private String retypeNewPassword;

  /** userSession. */
  @SessionState(create = false)
  private UserSession userSession;

  /** changePasswordForm. */
  @Component
  private Form changePasswordForm;

  /** cookies. */
  @Inject
  private Cookies cookies;

  /** Messages. */
  @Inject
  private Messages messages;

  /** userService. */
  @Inject
  private UserService userService;

  /**
   * Validate the changePasswordForm.
   * @throws InstanceNotFoundException
   *                  Thrown out when the user doesn't exist
   */
  final void onValidateFromChangePasswordForm()
                                          throws InstanceNotFoundException {

    if (!changePasswordForm.isValid()) {
      return;
    }

    if (!newPassword.equals(retypeNewPassword)) {
      changePasswordForm.recordError(messages.get("error-passwordsDontMatch"));
    } else {

      try {
        userService.changePassword(userSession.getUserProfileId(), oldPassword,
            newPassword);
      } catch (IncorrectPasswordException e) {
        changePasswordForm.recordError(messages.get("error-invalidPassword"));
      }

    }

  }

  /**
   * Method when the result is success.
   * @return index class
   */
  final Object onSuccess() {

    CookiesManager.removeCookies(cookies);
    return Index.class;

  }

}
