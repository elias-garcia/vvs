package es.udc.pa.pa015.practicapa.model.userservice;

import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa015.practicapa.model.userprofile.UserProfileDao;
import es.udc.pa.pa015.practicapa.model.userservice.util.PasswordEncrypter;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * User service implementation.
 */
@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

  /** UserProfileDao. */
  @Autowired
  private UserProfileDao userProfileDao;

  /**
   * Method that register an user.
   * @param loginName
   *            User loginName to register.
   * @param clearPassword
   *            User clear password to register.
   * @param userProfileDetails
   *            UserProfilDetails to register.
   * @return the userProfile registered
   * @throws DuplicateInstanceException
   *            Thrown out when the loginName already exists
   */
  public final UserProfile registerUser(final String loginName,
      final String clearPassword, final UserProfileDetails userProfileDetails)
      throws DuplicateInstanceException {

    try {
      userProfileDao.findByLoginName(loginName);
      throw new DuplicateInstanceException(loginName, UserProfile.class
          .getName());
    } catch (InstanceNotFoundException exception) {
      String encryptedPassword = PasswordEncrypter.crypt(clearPassword);

      UserProfile userProfile = new UserProfile(loginName, encryptedPassword,
          userProfileDetails.getFirstName(), userProfileDetails.getLastName(),
          userProfileDetails.getEmail());

      userProfileDao.save(userProfile);
      return userProfile;
    }

  }

  /**
   * Method that login an user.
   * @param loginName
   *            User loginName to login
   * @param password
   *            User password to login
   * @param passwordIsEncrypted
   *            Indicates if the password is encrypted
   * @return UserProdile login
   * @throws InstanceNotFoundException
   *            The loginName doesn't exist
   * @throws IncorrectPasswordException
   *            The password passed isn't correct
   */
  @Transactional(readOnly = true)
  public final UserProfile login(final String loginName, final String password,
      final boolean passwordIsEncrypted) throws InstanceNotFoundException,
      IncorrectPasswordException {

    UserProfile userProfile = userProfileDao.findByLoginName(loginName);
    String storedPassword = userProfile.getEncryptedPassword();

    if (passwordIsEncrypted) {
      if (!password.equals(storedPassword)) {
        throw new IncorrectPasswordException(loginName);
      }
    } else {
      if (!PasswordEncrypter.isClearPasswordCorrect(password, storedPassword)) {
        throw new IncorrectPasswordException(loginName);
      }
    }
    return userProfile;

  }

  /**
   * This method find an userProfile by an id passed.
   * @param userProfileId
   *          userProfile id
   * @return userProfile found
   * @throws InstanceNotFoundException
   *          userProfile doesn't exist
   */
  @Transactional(readOnly = true)
  public final UserProfile findUserProfile(final Long userProfileId)
      throws InstanceNotFoundException {

    return userProfileDao.find(userProfileId);
  }

  /**
   * This method update the user passed.
   * @param userProfileId
   *          userProfile id of the user to update
   * @param userProfileDetails
   *          userProfileDetails to update
   * @throws InstanceNotFoundException
   *          thrown out when the userProfil doesn't exists
   */
  public final void updateUserProfileDetails(final Long userProfileId,
      final UserProfileDetails userProfileDetails)
      throws InstanceNotFoundException {

    UserProfile userProfile = userProfileDao.find(userProfileId);
    userProfile.setFirstName(userProfileDetails.getFirstName());
    userProfile.setLastName(userProfileDetails.getLastName());
    userProfile.setEmail(userProfileDetails.getEmail());

  }

  /**
   * This method change the password of an user passed.
   * @param userProfileId
   *                userProfile id
   * @param oldClearPassword
   *                old clear password
   * @param newClearPassword
   *                new clear password
   * @throws IncorrectPasswordException
   *                thrown out when the password is incorrect
   * @throws InstanceNotFoundException
   *                thrown out when the user doesn't exist
   */
  public final void changePassword(final Long userProfileId,
      final String oldClearPassword, final String newClearPassword)
      throws IncorrectPasswordException, InstanceNotFoundException {

    UserProfile userProfile;
    userProfile = userProfileDao.find(userProfileId);

    String storedPassword = userProfile.getEncryptedPassword();

    if (!PasswordEncrypter.isClearPasswordCorrect(oldClearPassword,
        storedPassword)) {
      throw new IncorrectPasswordException(userProfile.getLoginName());
    }

    userProfile.setEncryptedPassword(
                            PasswordEncrypter.crypt(newClearPassword));

  }

}
