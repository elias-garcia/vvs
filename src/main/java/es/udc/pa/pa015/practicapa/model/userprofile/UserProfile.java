package es.udc.pa.pa015.practicapa.model.userprofile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
public class UserProfile {

  private Long userProfileId;
  private String loginName;
  private String encryptedPassword;
  private String firstName;
  private String lastName;
  private String email;

  public UserProfile() {
  }

  /**
   * UnerProfile constructor.
   * @param loginName
   *          User's login name
   * @param encryptedPassword
   *          User's encrypted password
   * @param firstName
   *          User's first name
   * @param lastName
   *          User's last name
   * @param email
   *          User's email
   */
  public UserProfile(String loginName, String encryptedPassword,
      String firstName, String lastName, String email) {

    /**
     * NOTE: "userProfileId" *must* be left as "null" since its value is
     * automatically generated.
     */

    this.loginName = loginName;
    this.encryptedPassword = encryptedPassword;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }

  @Id
  @Column(name = "usrId")
  @SequenceGenerator( // It only takes effect for
      name = "UserProfileIdGenerator", // databases providing identifier
      sequenceName = "UserProfileSeq") // generators.
  @GeneratedValue(strategy = GenerationType.AUTO, 
                                generator = "UserProfileIdGenerator")
  public Long getUserProfileId() {
    return userProfileId;
  }

  public void setUserProfileId(Long userProfileId) {
    this.userProfileId = userProfileId;
  }

  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

  @Column(name = "enPassword")
  public String getEncryptedPassword() {
    return encryptedPassword;
  }

  public void setEncryptedPassword(String encryptedPassword) {
    this.encryptedPassword = encryptedPassword;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return "UserProfile [userProfileId=" + userProfileId + ", loginName="
        + loginName + ", encryptedPassword=" + encryptedPassword
        + ", firstName=" + firstName + ", lastName=" + lastName + ", email="
        + email + "]";
  }
}
