package es.udc.pa.pa015.practicapa.web.util;

/**
 * UserSession class.
 */
public class UserSession {

  /** userProfileId. */
  private Long userProfileId;

  /** firstName. */
  private String firstName;

  /** indicates if it is admin. */
  private boolean admin;

  /**
   * Get userProfileId.
   * @return userProfileId
   */
  public final Long getUserProfileId() {
    return userProfileId;
  }

  /**
   * Set userProfileId.
   * @param userProfileIdParam user id
   */
  public final void setUserProfileId(final Long userProfileIdParam) {
    this.userProfileId = userProfileIdParam;
  }

  /**
   * Get firstName.
   * @return firstName
   */
  public final String getFirstName() {
    return firstName;
  }

  /**
   * Set firstName.
   * @param firstNameParam user's firstName
   */
  public final void setFirstName(final String firstNameParam) {
    this.firstName = firstNameParam;
  }

  /**
   * Get if the user is admin.
   * @return boolean
   */
  public final boolean getAdmin() {
    return admin;
  }

  /**
   * Set if the user is admin.
   * @param adminParam
   *            Indicates if the user is admin
   */
  public final void setAdmin(final boolean adminParam) {
    this.admin = adminParam;
  }

}
