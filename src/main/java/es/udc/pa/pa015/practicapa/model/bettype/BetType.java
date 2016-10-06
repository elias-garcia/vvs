package es.udc.pa.pa015.practicapa.model.bettype;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.concurrent.Immutable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;

@Entity
@Immutable
public class BetType {

	private Long typeId;
	private String question;
	private Boolean isMultiple;
	private EventInfo event;
	private Set<TypeOption> typeOptions = new HashSet<TypeOption>();;
	
	public BetType() {
		
	}

	public BetType(String question, Boolean multiple, EventInfo event) {

		/**
		 * NOTE: "typeId" *must* be left as "null" since its value is
		 * automatically generated.
		 */
		this.question = question;
		this.isMultiple = multiple;
		this.event = event;
	}
	
	@Id
	@SequenceGenerator( 					// It only takes effect for
			name = "BetTypeIdGenerator", 	// databases providing identifier
			sequenceName = "BetTypeSeq")	// generators.
	@GeneratedValue(
			strategy = GenerationType.AUTO,
			generator = "BetTypeIdGenerator")
	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public Boolean getIsMultiple() {
		return isMultiple;
	}

	public void setIsMultiple(Boolean isMultiple) {
		this.isMultiple = isMultiple;
	}

	@ManyToOne(optional = false, fetch=FetchType.LAZY)
	@JoinColumn(name = "eventId")
	public EventInfo getEvent() {
		return event;
	}

	public void setEvent(EventInfo event) {
		this.event = event;
	}
	
	@OneToMany(mappedBy = "type")
	public Set<TypeOption> getTypeOptions() {
		return typeOptions;
	}

	public void setTypeOptions(Set<TypeOption> typeOptions) {
		this.typeOptions = typeOptions;
	} 

	public void addTypeOption(TypeOption typeOption) {
		this.typeOptions.add(typeOption);
		typeOption.setType(this);
	}
	
	@Override
	public String toString() {
		return "BetType [typeId=" + typeId + ", question="
				+ question + ", isMultiple=" + isMultiple
				+ ", event=" + event.getEventId() + "]";
	}
}
