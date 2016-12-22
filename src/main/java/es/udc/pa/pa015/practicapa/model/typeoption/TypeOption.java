package es.udc.pa.pa015.practicapa.model.typeoption;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Version;

import org.hibernate.annotations.Type;

import es.udc.pa.pa015.practicapa.model.bettype.BetType;

/**
 * TypeOption class.
 */
@Entity
public class TypeOption {

	/** Option id. */
	private Long optionId;

	/** Option odd. */
	private double odd;

	/** Option result. */
	private String result;

	/** Indicates if option is winner. */
	private Boolean isWinner;

	/** Version. */
	private Long version;

	/** betType associated. */
	private BetType type;

	/** Blank constructor. */
	public TypeOption() {

	}

	/**
	 * TypeOption constructor.
	 * 
	 * @param oddParam
	 *            Indicates if it is odd
	 * @param resultParam
	 *            The string with the result
	 * @param typeParam
	 *            The bet type associated
	 */
	public TypeOption(final double oddParam, final String resultParam, final BetType typeParam) {

		/**
		 * NOTE: "optionId" *must* be left as "null" since its value is
		 * automatically generated. Also, isWinner must be left as "null" so the
		 * option is not winner or looser.
		 */

		this.odd = oddParam;
		this.result = resultParam;
		this.type = typeParam;
	}

	/**
	 * Get option id.
	 * 
	 * @return option id
	 */
	@Id
	@SequenceGenerator(// It only takes effect for
			name = "BetOptionIdGenerator", // databases providing identifier
			sequenceName = "BetOptionSeq") // generators.
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "BetOptionIdGenerator")
	public final Long getOptionId() {
		return optionId;
	}

	/**
	 * Set option id.
	 * 
	 * @param optionIdParam
	 *            option id
	 */
	public final void setOptionId(final Long optionIdParam) {
		this.optionId = optionIdParam;
	}

	/**
	 * Get option odd.
	 * 
	 * @return option odd
	 */
	public double getOdd() {
		return odd;
	}

	/**
	 * Set option odd.
	 * 
	 * @param oddParam
	 *            option odd
	 */
	public void setOdd(final double oddParam) {
		this.odd = oddParam;
	}

	/**
	 * Get option result.
	 * 
	 * @return option result
	 */
	public String getResult() {
		return result;
	}

	/**
	 * Set option result.
	 * 
	 * @param resultParam
	 *            option result
	 */
	public void setResult(final String resultParam) {
		this.result = resultParam;
	}

	/**
	 * Get if it is winner.
	 * 
	 * @return boolean
	 */
	@Type(type = "org.hibernate.type.BooleanType")
	public Boolean getIsWinner() {
		return isWinner;
	}

	/**
	 * Set if it is winner.
	 * 
	 * @param isWinnerParam
	 *            indicates if it is winner
	 */
	public void setIsWinner(final Boolean isWinnerParam) {
		this.isWinner = isWinnerParam;
	}

	/**
	 * Get the version.
	 * 
	 * @return version
	 */
	@Version
	public Long getVersion() {
		return version;
	}

	/**
	 * Set the version.
	 * 
	 * @param versionParam
	 *            version
	 */
	public void setVersion(final Long versionParam) {
		this.version = versionParam;
	}

	/**
	 * Get betType associated.
	 * 
	 * @return betType
	 */
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	@JoinColumn(name = "typeId")
	public BetType getType() {
		return type;
	}

	/**
	 * Set betType associated.
	 * 
	 * @param typeParam
	 *            betType
	 */
	public void setType(final BetType typeParam) {
		this.type = typeParam;
	}

}
