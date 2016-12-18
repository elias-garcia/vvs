package es.udc.pa.pa015.practicapa.test.model.userservice;

import static es.udc.pa.pa015.practicapa.model.util.GlobalNames.SPRING_CONFIG_FILE;
import static es.udc.pa.pa015.practicapa.test.util.GlobalNames.SPRING_CONFIG_TEST_FILE;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa015.practicapa.model.userservice.IncorrectPasswordException;
import es.udc.pa.pa015.practicapa.model.userservice.UserProfileDetails;
import es.udc.pa.pa015.practicapa.model.userservice.UserService;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { SPRING_CONFIG_FILE,
    SPRING_CONFIG_TEST_FILE })
@Transactional
public class UserServiceTest {

  private final long NON_EXISTENT_USER_PROFILE_ID = -1;

  @Autowired
  private UserService userService;

  /*
   * 
   * PR-IT-007 AND PR-IT-014
   * 
   */

  @Test
  public void testRegisterUserAndFindUserProfile()
      throws DuplicateInstanceException, InstanceNotFoundException {

    /* Call */
    UserProfile userProfile = userService.registerUser("user", "userPassword",
        new UserProfileDetails("name", "lastName", "user@udc.es"));

    UserProfile userProfile2 = userService.findUserProfile(userProfile
        .getUserProfileId());

    /* Assertion */
    assertEquals(userProfile, userProfile2);

  }

  /*
   * 
   * PR-IT-008
   * 
   */

  @Test(expected = DuplicateInstanceException.class)
  public void testRegisterDuplicatedUser() throws DuplicateInstanceException,
      InstanceNotFoundException {

    /* Setup */
    String loginName = "user";
    String clearPassword = "userPassword";
    UserProfileDetails userProfileDetails = new UserProfileDetails("name",
        "lastName", "user@udc.es");

    userService.registerUser(loginName, clearPassword, userProfileDetails);

    /* Call */
    userService.registerUser(loginName, clearPassword, userProfileDetails);

  }

  /*
   * 
   * PR-IT-009
   * 
   */

  @Test
  public void testLoginClearPassword() throws IncorrectPasswordException,
      InstanceNotFoundException {

    /* Setup */
    String clearPassword = "userPassword";
    UserProfile userProfile = registerUser("user", clearPassword);

    /* Call */
    UserProfile userProfile2 = userService.login(userProfile.getLoginName(),
        clearPassword, false);

    /* Assertion */
    assertEquals(userProfile, userProfile2);

  }

  /*
   * 
   * PR-IT-010
   * 
   */

  @Test(expected = IncorrectPasswordException.class)
  public void testLoginIncorrectPasword() throws IncorrectPasswordException,
      InstanceNotFoundException {

    /* Setup */
    String clearPassword = "userPassword";
    UserProfile userProfile = registerUser("user", clearPassword);

    /* Call */
    userService.login(userProfile.getLoginName(), 'X' + clearPassword, false);

  }

  /*
   * 
   * PR-IT-011
   * 
   */

  @Test(expected = InstanceNotFoundException.class)
  public void testLoginWithNonExistentUser() throws IncorrectPasswordException,
      InstanceNotFoundException {

    /* Call */
    userService.login("user", "userPassword", false);

  }

  /*
   * 
   * PR-IT-012
   * 
   */

  @Test
  public void testLoginEncryptedPassword() throws IncorrectPasswordException,
      InstanceNotFoundException {

    /* Setup */
    UserProfile userProfile = registerUser("user", "clearPassword");

    /* Call */
    UserProfile userProfile2 = userService.login(userProfile.getLoginName(),
        userProfile.getEncryptedPassword(), true);

    /* Assertion */
    assertEquals(userProfile, userProfile2);

  }

  /*
   * 
   * PR-IT-013
   * 
   */

  @Test(expected = InstanceNotFoundException.class)
  public void testFindNonExistentUser() throws InstanceNotFoundException {

    /* Call */
    userService.findUserProfile(NON_EXISTENT_USER_PROFILE_ID);

  }

  /*
   * 
   * PR-IT-015
   * 
   */

  @Test
  public void testChangePassword() throws InstanceNotFoundException,
      IncorrectPasswordException {

    /* Setup */
    String clearPassword = "userPassword";
    UserProfile userProfile = registerUser("user", clearPassword);
    String newClearPassword = 'X' + clearPassword;

    /* Call */
    userService.changePassword(userProfile.getUserProfileId(), clearPassword,
        newClearPassword);

    /* Check new password. */
    userService.login(userProfile.getLoginName(), newClearPassword, false);

  }

  /*
   * 
   * PR-IT-016
   * 
   */

  @Test(expected = IncorrectPasswordException.class)
  public void testChangePasswordWithIncorrectPassword()
      throws InstanceNotFoundException, IncorrectPasswordException {

    /* Setup */
    String clearPassword = "userPassword";
    UserProfile userProfile = registerUser("user", clearPassword);

    /* Call */
    userService.changePassword(userProfile.getUserProfileId(), 'X'
        + clearPassword, 'Y' + clearPassword);

  }

  /*
   * 
   * PR-IT-017
   * 
   */

  @Test(expected = InstanceNotFoundException.class)
  public void testChangePasswordWithNonExistentUser()
      throws InstanceNotFoundException, IncorrectPasswordException {

    /* Call */
    userService.changePassword(NON_EXISTENT_USER_PROFILE_ID, "userPassword",
        "XuserPassword");

  }

  private UserProfile registerUser(String loginName, String clearPassword) {

    /* Setup */
    UserProfileDetails userProfileDetails = new UserProfileDetails("name",
        "lastName", "user@udc.es");

    /* Call */
    try {

      return userService.registerUser(loginName, clearPassword,
          userProfileDetails);

    } catch (DuplicateInstanceException e) {
      throw new RuntimeException(e);
    }

  }

  /*
   * 
   * PR-IT-018
   * 
   */

  @Test
  public void testUpdate() throws InstanceNotFoundException,
      IncorrectPasswordException {

    /* Setup */
    String clearPassword = "userPassword";
    UserProfile userProfile = registerUser("user", clearPassword);

    UserProfileDetails newUserProfileDetails = new UserProfileDetails('X'
        + userProfile.getFirstName(), 'X' + userProfile.getLastName(), 'X'
            + userProfile.getEmail());

    /* Call */
    userService.updateUserProfileDetails(userProfile.getUserProfileId(),
        newUserProfileDetails);

    userService.login(userProfile.getLoginName(), clearPassword, false);
    UserProfile userProfile2 = userService.findUserProfile(userProfile
        .getUserProfileId());

    /* Assertion */
    assertEquals(newUserProfileDetails.getFirstName(), userProfile2
        .getFirstName());
    assertEquals(newUserProfileDetails.getLastName(), userProfile2
        .getLastName());
    assertEquals(newUserProfileDetails.getEmail(), userProfile2.getEmail());

  }

  /*
   * 
   * PR-IT-019
   * 
   */

  @Test(expected = InstanceNotFoundException.class)
  public void testUpdateWithNonExistentUser() throws InstanceNotFoundException {

    /* Call */
    userService.updateUserProfileDetails(NON_EXISTENT_USER_PROFILE_ID,
        new UserProfileDetails("name", "lastName", "user@udc.es"));

  }
}
