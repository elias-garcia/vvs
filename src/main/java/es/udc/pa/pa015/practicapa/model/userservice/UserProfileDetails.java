package es.udc.pa.pa015.practicapa.model.userservice;

public class UserProfileDetails {

  private String firstName;
  private String lastName;
  private String email;

  /**
   * UserProfileDetails constructor.
   * @param firstName
   *          User's first name
   * @param lastName
   *          User's last name
   * @param email
   *          User's email
   */
  public UserProfileDetails(String firstName, String lastName, String email) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getEmail() {
    return email;
  }

}
