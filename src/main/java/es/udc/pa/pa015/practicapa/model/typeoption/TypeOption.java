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

import es.udc.pa.pa015.practicapa.model.bettype.BetType;

@Entity
public class TypeOption {

	private Long optionId;
	private double odd;
	private String result;
	private Boolean isWinner;
	private Long version;
	private BetType type;
	
	public TypeOption() {
		
	}

	public TypeOption(double odd, String result, BetType type) {

		/**
		 * NOTE: "optionId" *must* be left as "null" since its value is
		 * automatically generated.
		 * Also, isWinner must be left as "null" so the option is not
		 * winner or looser.
		 */
		
		this.odd = odd;
		this.result = result;
		this.type = type;
	}
	
	@Id
	@SequenceGenerator( 					// It only takes effect for
			name = "BetOptionIdGenerator", 	// databases providing identifier
			sequenceName = "BetOptionSeq")	// generators.
	@GeneratedValue(
		strategy = GenerationType.AUTO,
		generator = "BetOptionIdGenerator")
	public Long getOptionId() {
		return optionId;
	}

	public void setOptionId(Long optionId) {
		this.optionId = optionId;
	}

	public double getOdd() {
		return odd;
	}

	public void setOdd(double odd) {
		this.odd = odd;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Boolean getIsWinner() {
		return isWinner;
	}

	public void setIsWinner(Boolean isWinner) {
		this.isWinner = isWinner;
	}

	@Version
	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@ManyToOne(optional = false, fetch=FetchType.LAZY)
	@JoinColumn(name = "typeId")
	public BetType getType() {
		return type;
	}

	public void setType(BetType type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "TypeOption [optionId=" + optionId + ", odd="
				+ odd + ", result=" + result + ", isWinner="
				+ isWinner + ", type=" + type.getTypeId() + "]";
	}
}
