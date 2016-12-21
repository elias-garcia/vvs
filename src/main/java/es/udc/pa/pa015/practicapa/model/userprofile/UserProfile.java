package es.udc.pa.pa015.practicapa.model.userprofile;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * UserProfile class.
 */
@Entity
public class UserProfile {

	/** userProfileId. */
	private Long userProfileId;

	/** user loginName. */
	private String loginName;

	/** user encrypted password. */
	private String encryptedPassword;

	/** user firstName. */
	private String firstName;

	/** user lastName. */
	private String lastName;

	/** user email. */
	private String email;

	/** Blank constructor. */
	public UserProfile() {
	}

	/**
	 * UnerProfile constructor.
	 * 
	 * @param loginNameParam
	 *            User's login name
	 * @param encryptedPasswordParam
	 *            User's encrypted password
	 * @param firstNameParam
	 *            User's first name
	 * @param lastNameParam
	 *            User's last name
	 * @param emailParam
	 *            User's email
	 */
	public UserProfile(final String loginNameParam, final String encryptedPasswordParam, final String firstNameParam,
			final String lastNameParam, final String emailParam) {

		/**
		 * NOTE: "userProfileId" *must* be left as "null" since its value is
		 * automatically generated.
		 */

		this.loginName = loginNameParam;
		this.encryptedPassword = encryptedPasswordParam;
		this.firstName = firstNameParam;
		this.lastName = lastNameParam;
		this.email = emailParam;
	}

	/**
	 * Get userProfile id.
	 * 
	 * @return userProfile id
	 */
	@Id
	@Column(name = "usrId")
	@SequenceGenerator(// It only takes effect for
			name = "UserProfileIdGenerator", // databases providing identifier
			sequenceName = "UserProfileSeq") // generators.
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "UserProfileIdGenerator")
	public final Long getUserProfileId() {
		return userProfileId;
	}

	/**
	 * Set userProfileId.
	 * 
	 * @param userProfileIdParam
	 *            userProfile id
	 */
	public final void setUserProfileId(final Long userProfileIdParam) {
		this.userProfileId = userProfileIdParam;
	}

	/**
	 * Get user loginName.
	 * 
	 * @return loginName
	 */
	public String getLoginName() {
		return loginName;
	}

	/**
	 * Set user loginName.
	 * 
	 * @param loginNameParam
	 *            user loginName
	 */
	public void setLoginName(final String loginNameParam) {
		this.loginName = loginNameParam;
	}

	/**
	 * Get encrypted password.
	 * 
	 * @return encrypted password
	 */
	@Column(name = "enPassword")
	public String getEncryptedPassword() {
		return encryptedPassword;
	}

	/**
	 * Set encrypted password.
	 * 
	 * @param encryptedPasswordParam
	 *            user encrypted password
	 */
	public void setEncryptedPassword(final String encryptedPasswordParam) {
		this.encryptedPassword = encryptedPasswordParam;
	}

	/**
	 * Get first name.
	 * 
	 * @return user firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Set user firstName.
	 * 
	 * @param firstNameParam
	 *            user firstName
	 */
	public void setFirstName(final String firstNameParam) {
		this.firstName = firstNameParam;
	}

	/**
	 * Get user lastName.
	 * 
	 * @return user lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Set user lastName.
	 * 
	 * @param lastNameParam
	 *            user lastName
	 */
	public void setLastName(final String lastNameParam) {
		this.lastName = lastNameParam;
	}

	/**
	 * Get user email.
	 * 
	 * @return user email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Set user email.
	 * 
	 * @param emailParam
	 *            user email
	 */
	public void setEmail(final String emailParam) {
		this.email = emailParam;
	}

}
