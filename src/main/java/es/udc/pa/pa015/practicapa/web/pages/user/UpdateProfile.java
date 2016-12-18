package es.udc.pa.pa015.practicapa.web.pages.user;

import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa015.practicapa.model.userservice.UserProfileDetails;
import es.udc.pa.pa015.practicapa.model.userservice.UserService;
import es.udc.pa.pa015.practicapa.web.pages.Index;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa015.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Class of the updateProfile page.
 */
@AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
public class UpdateProfile {

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

  /**
   * on Prepare For Render.
   * @throws InstanceNotFoundException
   *                     thrown out when the user doesn't exist
   */
  final void onPrepareForRender() throws InstanceNotFoundException {

    UserProfile userProfile;

    userProfile = userService.findUserProfile(userSession.getUserProfileId());
    firstName = userProfile.getFirstName();
    lastName = userProfile.getLastName();
    email = userProfile.getEmail();

  }

  /**
   * Method when the result is success.
   * @return index class
   * @throws InstanceNotFoundException
   *                    thrown out when the category doesn't exist
   */
  final Object onSuccess() throws InstanceNotFoundException {

    userService.updateUserProfileDetails(userSession.getUserProfileId(),
        new UserProfileDetails(firstName, lastName, email));
    userSession.setFirstName(firstName);
    return Index.class;

  }

}
