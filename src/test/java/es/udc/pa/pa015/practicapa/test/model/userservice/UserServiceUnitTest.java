package es.udc.pa.pa015.practicapa.test.model.userservice;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa015.practicapa.model.userprofile.UserProfileDao;
import es.udc.pa.pa015.practicapa.model.userservice.IncorrectPasswordException;
import es.udc.pa.pa015.practicapa.model.userservice.UserProfileDetails;
import es.udc.pa.pa015.practicapa.model.userservice.UserServiceImpl;
import es.udc.pa.pa015.practicapa.model.userservice.util.PasswordEncrypter;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceUnitTest {

	private final long NON_EXISTENT_USER_PROFILE_ID = -1;

	@InjectMocks
	private UserServiceImpl userService = new UserServiceImpl();

	@Mock
	private UserProfileDao userProfileDaoMock;

	UserProfile userProfile;

	private void initializeUser() {
		userProfile = new UserProfile("user1", "pass", "nombre", "apellido", "user@gmail.com");
	}

	/*
	 * PR-UN-038 PR-UN-045
	 */
	@Test
	public void testRegisterUserAndFindUserProfile() throws DuplicateInstanceException, InstanceNotFoundException {

		/* Setup */

		initializeUser();

		/* Mock behavior */
		doThrow(InstanceNotFoundException.class).when(userProfileDaoMock).findByLoginName("user1");
		doNothing().when(userProfileDaoMock).save(userProfile);

		/* Call Register user and find profile. */

		UserProfile userProfile = userService.registerUser("user1", "pass",
				new UserProfileDetails("nombre", "apellido", "user@gmail.com"));

		userProfile.setUserProfileId((long) 1);

		when(userProfileDaoMock.find(userProfile.getUserProfileId())).thenReturn(userProfile);

		UserProfile userProfile2 = userService.findUserProfile(userProfile.getUserProfileId());

		/* Assertion */
		assertEquals(userProfile, userProfile2);
		verify(userProfileDaoMock, times(1)).save(any(UserProfile.class));
		verify(userProfileDaoMock, times(1)).findByLoginName(anyString());
		verify(userProfileDaoMock, times(1)).find((long) 1);
	}

	/*
	 * PR-UN-039
	 */
	@Test(expected = DuplicateInstanceException.class)
	public void testRegisterDuplicatedUser() throws DuplicateInstanceException, InstanceNotFoundException {

		/* Mock behavior */
		doThrow(DuplicateInstanceException.class).when(userProfileDaoMock).findByLoginName("user1");

		/* Call Register duplicate user */
		userService.registerUser("user1", "pass", new UserProfileDetails("nombre", "apellido", "user@gmail.com"));

		/* DuplicateInstanceException expected */

	}

	/*
	 * PR-UN-040
	 */
	@Test
	public void testLoginClearPassword() throws IncorrectPasswordException, InstanceNotFoundException {

		/* Setup */

		initializeUser();

		userProfile.setEncryptedPassword(PasswordEncrypter.crypt("pass"));

		/* Mock Behavior */

		when(userProfileDaoMock.findByLoginName("user1")).thenReturn(userProfile);

		/* Call login */
		UserProfile userProfile2 = userService.login(userProfile.getLoginName(), "pass", false);

		/* Assertion */
		assertEquals(userProfile, userProfile2);
		verify(userProfileDaoMock, times(1)).findByLoginName(anyString());

	}

	/*
	 * PR-UN-041
	 */
	@Test(expected = IncorrectPasswordException.class)
	public void testLoginIncorrectPasword() throws IncorrectPasswordException, InstanceNotFoundException {

		/* Setup */

		initializeUser();

		/* Mock Behavior */

		when(userProfileDaoMock.findByLoginName("user1")).thenReturn(userProfile);

		/* Call login */
		userService.login(userProfile.getLoginName(), "pass2", false);

		/* IncorrectPasswordException expected */

	}

	/*
	 * PR-UN-042
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = InstanceNotFoundException.class)
	public void testLoginWithNonExistentUser() throws IncorrectPasswordException, InstanceNotFoundException {

		/* Mock Behavior */

		when(userProfileDaoMock.findByLoginName("user1")).thenThrow(InstanceNotFoundException.class);

		/* Call login */
		userService.login("user1", "pass", false);

	}

	/*
	 * PR-UN-043
	 */
	@Test
	public void testLoginEncryptedPassword() throws IncorrectPasswordException, InstanceNotFoundException {

		/* Setup */

		initializeUser();

		userProfile.setEncryptedPassword(PasswordEncrypter.crypt("pass"));

		/* Mock Behavior */

		when(userProfileDaoMock.findByLoginName("user1")).thenReturn(userProfile);

		/* Call login */
		UserProfile userProfile2 = userService.login(userProfile.getLoginName(), userProfile.getEncryptedPassword(),
				true);

		/* Assertion */

		assertEquals(userProfile, userProfile2);

	}

	/*
	 * PR-UN-044
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = InstanceNotFoundException.class)
	public void testFindNonExistentUser() throws InstanceNotFoundException {

		/* Mock Behavior */

		when(userProfileDaoMock.find(NON_EXISTENT_USER_PROFILE_ID)).thenThrow(InstanceNotFoundException.class);

		/* Call find */
		userService.findUserProfile(NON_EXISTENT_USER_PROFILE_ID);

	}

	/*
	 * PR-UN-046
	 */
	@Test
	public void testChangePassword() throws InstanceNotFoundException, IncorrectPasswordException {

		/* Setup */
		initializeUser();

		userProfile.setEncryptedPassword(PasswordEncrypter.crypt("pass"));

		/* Mock Behavior */

		when(userProfileDaoMock.find((long) 1)).thenReturn(userProfile);

		/* Call ChangePassword. */
		String newClearPassword = "Xpass";

		userService.changePassword((long) 1, "pass", newClearPassword);

		when(userProfileDaoMock.findByLoginName("user1")).thenReturn(userProfile);

		/* Check new password. */
		userService.login(userProfile.getLoginName(), newClearPassword, false);

	}

	/*
	 * PR-UN-047
	 */
	@Test(expected = IncorrectPasswordException.class)
	public void testChangePasswordWithIncorrectPassword() throws InstanceNotFoundException, IncorrectPasswordException {

		/* Setup */
		initializeUser();

		/* Mock Behavior */

		when(userProfileDaoMock.find(userProfile.getUserProfileId())).thenReturn(userProfile);

		/* Call changePassword */
		userService.changePassword(userProfile.getUserProfileId(), "Xpass", "Ypass");

	}

	/*
	 * PR-UN-048
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = InstanceNotFoundException.class)
	public void testChangePasswordWithNonExistentUser() throws InstanceNotFoundException, IncorrectPasswordException {
		/* Mock Behavior */

		when(userProfileDaoMock.find(NON_EXISTENT_USER_PROFILE_ID)).thenThrow(InstanceNotFoundException.class);

		userService.changePassword(NON_EXISTENT_USER_PROFILE_ID, "pass", "newPass");

	}

	/*
	 * PR-UN-049
	 */
	@Test
	public void testUpdate() throws InstanceNotFoundException, IncorrectPasswordException {

		/* Setup */

		initializeUser();

		/* Mock Behavior */

		when(userProfileDaoMock.find((long) 1)).thenReturn(userProfile);

		/* Call UpdateProfileDetails. */

		UserProfileDetails newUserProfileDetails = new UserProfileDetails('X' + userProfile.getFirstName(),
				'X' + userProfile.getLastName(), 'X' + userProfile.getEmail());

		userService.updateUserProfileDetails((long) 1, newUserProfileDetails);

		UserProfile userProfile2 = new UserProfile("use1", "pass", "Xnombre", "Xapellido", "Xuser@gmail.com");

		/* Assertion */
		assertEquals(newUserProfileDetails.getFirstName(), userProfile2.getFirstName());
		assertEquals(newUserProfileDetails.getLastName(), userProfile2.getLastName());
		assertEquals(newUserProfileDetails.getEmail(), userProfile2.getEmail());
		verify(userProfileDaoMock, times(1)).find((long) 1);

	}

	/*
	 * PR-UN-050
	 */
	@SuppressWarnings("unchecked")
	@Test(expected = InstanceNotFoundException.class)
	public void testUpdateWithNonExistentUser() throws InstanceNotFoundException {

		/* Mock Behavior */

		when(userProfileDaoMock.find(NON_EXISTENT_USER_PROFILE_ID)).thenThrow(InstanceNotFoundException.class);

		/* Call find */
		userService.updateUserProfileDetails(NON_EXISTENT_USER_PROFILE_ID,
				new UserProfileDetails("name", "lastName", "user@udc.es"));

	}
}