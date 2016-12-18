package es.udc.pa.pa015.practicapa.model.userservice;

/**
 * UserProfileDetails class.
 */
public class UserProfileDetails {

  /** User FirstName. */
  private String firstName;

  /** User lastName. */
  private String lastName;

  /** User email. */
  private String email;

  /**
   * UserProfileDetails constructor.
   * @param firstNameParam
   *          User's first name
   * @param lastNameParam
   *          User's last name
   * @param emailParam
   *          User's email
   */
  public UserProfileDetails(final String firstNameParam,
      final String lastNameParam, final String emailParam) {
    this.firstName = firstNameParam;
    this.lastName = lastNameParam;
    this.email = emailParam;
  }

  /**
   * Get user firstName.
   * @return firstName
   */
  public final String getFirstName() {
    return firstName;
  }

  /**
   * Get user lastName.
   * @return lastName
   */
  public final String getLastName() {
    return lastName;
  }

  /**
   * Get user email.
   * @return email
   */
  public final String getEmail() {
    return email;
  }

}
