package es.udc.pa.pa015.practicapa.model.userservice;

import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;
import es.udc.pa.pa015.practicapa.model.userprofile.UserProfileDao;
import es.udc.pa.pa015.practicapa.model.userservice.util.PasswordEncrypter;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

  @Autowired
  private UserProfileDao userProfileDao;

  /**
   * This method register an user.
   */
  public UserProfile registerUser(String loginName, String clearPassword,
      UserProfileDetails userProfileDetails) throws DuplicateInstanceException {

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
   * This method is the responsible to login an user.
   */
  @Transactional(readOnly = true)
  public UserProfile login(String loginName, String password,
      boolean passwordIsEncrypted) throws InstanceNotFoundException,
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

  @Transactional(readOnly = true)
  public UserProfile findUserProfile(Long userProfileId)
      throws InstanceNotFoundException {

    return userProfileDao.find(userProfileId);
  }

  /**
   * This method is the responsible to update an user.
   */
  public void updateUserProfileDetails(Long userProfileId,
      UserProfileDetails userProfileDetails) throws InstanceNotFoundException {

    UserProfile userProfile = userProfileDao.find(userProfileId);
    userProfile.setFirstName(userProfileDetails.getFirstName());
    userProfile.setLastName(userProfileDetails.getLastName());
    userProfile.setEmail(userProfileDetails.getEmail());

  }

  /**
   * This method is the responsible to change the user's password.
   */
  public void changePassword(Long userProfileId, String oldClearPassword,
      String newClearPassword) throws IncorrectPasswordException,
      InstanceNotFoundException {

    UserProfile userProfile;
    userProfile = userProfileDao.find(userProfileId);

    String storedPassword = userProfile.getEncryptedPassword();

    if (!PasswordEncrypter.isClearPasswordCorrect(oldClearPassword,
        storedPassword)) {
      throw new IncorrectPasswordException(userProfile.getLoginName());
    }

    userProfile.setEncryptedPassword(PasswordEncrypter.crypt(newClearPassword));

  }

}
