package es.udc.pa.pa015.practicapa.model.betinfo;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.BatchSize;

import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.model.userprofile.UserProfile;

/**
 * This is the bet info class.
 */
@Entity
@BatchSize(size = 10)
public class BetInfo {

	private Long betId;
	private Calendar betDate;
	private double amount;
	private UserProfile user;
	private TypeOption option;

	/**
	 * This constructor is in blank.
	 */
	public BetInfo() {

	}

	/**
	 * This constructor initialize bet info.
	 * 
	 * @param betDate
	 *            The bet date
	 * @param amount
	 *            The bet amount
	 * @param user
	 *            The user that do the bet
	 * @param option
	 *            The bet option
	 */
	public BetInfo(Calendar betDate, double amount, UserProfile user, TypeOption option) {

		/**
		 * NOTE: "betId" *must* be left as "null" since its value is
		 * automatically generated.
		 */

		this.betDate = betDate;
		this.amount = amount;
		this.user = user;
		this.option = option;
	}

	@Id
	@SequenceGenerator(// It only takes effect for
			name = "BetInfoIdGenerator", // databases providing identifier
			sequenceName = "BetInfoSeq") // generators.
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "BetInfoIdGenerator")
	public Long getBetId() {
		return betId;
	}

	public void setBetId(Long betId) {
		this.betId = betId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Calendar getBetDate() {
		return betDate;
	}

	public void setBetDate(Calendar betDate) {
		this.betDate = betDate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "userId")
	public UserProfile getUser() {
		return user;
	}

	public void setUser(UserProfile user) {
		this.user = user;
	}

	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "optionId")
	public TypeOption getOption() {
		return option;
	}

	public void setOption(TypeOption option) {
		this.option = option;
	}

}
