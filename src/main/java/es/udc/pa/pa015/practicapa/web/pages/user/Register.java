package es.udc.pa.pa015.practicapa.web.pages.user;

import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa015.practicapa.model.userservice.UserProfileDetails;
import es.udc.pa.pa015.practicapa.model.userservice.UserService;
import es.udc.pa.pa015.practicapa.web.pages.Index;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa015.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Class of the register page.
 */
@AuthenticationPolicy(AuthenticationPolicyType.NON_AUTHENTICATED_USERS)
public class Register {

  /** loginName. */
  @Property
  private String loginName;

  /** password. */
  @Property
  private String password;

  /** retypePassword. */
  @Property
  private String retypePassword;

  /** firstName. */
  @Property
  private String firstName;

  /** lastName. */
  @Property
  private String lastName;

  /** email. */
  @Property
  private String email;

  /** userSession. */
  @SessionState(create = false)
  private UserSession userSession;

  /** userService. */
  @Inject
  private UserService userService;

  /** registrationForm. */
  @Component
  private Form registrationForm;

  /** loginName TextField. */
  @Component(id = "loginName")
  private TextField loginNameField;

  /** passwordField. */
  @Component(id = "password")
  private PasswordField passwordField;

  /** Messages. */
  @Inject
  private Messages messages;

  /** userProfileId. */
  private Long userProfileId;

  /**
   * Method to validate the registration form.
   */
  final void onValidateFromRegistrationForm() {

    if (!registrationForm.isValid()) {
      return;
    }

    if (!password.equals(retypePassword)) {
      registrationForm.recordError(passwordField, messages.get(
          "error-passwordsDontMatch"));
    } else {

      try {
        UserProfile userProfile = userService.registerUser(loginName, password,
            new UserProfileDetails(firstName, lastName, email));
        userProfileId = userProfile.getUserProfileId();
      } catch (DuplicateInstanceException e) {
        registrationForm.recordError(loginNameField, messages.get(
            "error-loginNameAlreadyExists"));
      }

    }

  }

  /**
   * Method when the result is success.
   * @return index class
   */
  final Object onSuccess() {

    userSession = new UserSession();
    userSession.setUserProfileId(userProfileId);
    userSession.setFirstName(firstName);
    return Index.class;

  }

}
