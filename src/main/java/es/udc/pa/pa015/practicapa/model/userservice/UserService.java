package es.udc.pa.pa015.practicapa.model.userservice;

import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;
import es.udc.pojo.modelutil.exceptions.DuplicateInstanceException;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

/**
 * User service.
 */
public interface UserService {

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
  UserProfile registerUser(String loginName, String clearPassword,
      UserProfileDetails userProfileDetails) throws DuplicateInstanceException;

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
  UserProfile login(String loginName, String password,
      boolean passwordIsEncrypted) throws InstanceNotFoundException,
      IncorrectPasswordException;

  /**
   * This method find an userProfile by an id passed.
   * @param userProfileId
   *          userProfile id
   * @return userProfile found
   * @throws InstanceNotFoundException
   *          userProfile doesn't exist
   */
  UserProfile findUserProfile(Long userProfileId)
      throws InstanceNotFoundException;

  /**
   * This method update the user passed.
   * @param userProfileId
   *          userProfile id of the user to update
   * @param userProfileDetails
   *          userProfileDetails to update
   * @throws InstanceNotFoundException
   *          thrown out when the userProfil doesn't exists
   */
  void updateUserProfileDetails(Long userProfileId,
      UserProfileDetails userProfileDetails) throws InstanceNotFoundException;

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
  void changePassword(Long userProfileId, String oldClearPassword,
      String newClearPassword) throws IncorrectPasswordException,
      InstanceNotFoundException;

}
