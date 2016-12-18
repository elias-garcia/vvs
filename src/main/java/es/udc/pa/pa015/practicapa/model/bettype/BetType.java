package es.udc.pa.pa015.practicapa.model.bettype;

import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;

import org.hibernate.annotations.Type;

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

/**
 * Bet Type class.
 */
@Entity
@Immutable
public class BetType {

  /** Type id. */
  private Long typeId;

  /** Question. */
  private String question;

  /** Indicates if the betType is multiple. */
  private Boolean isMultiple;

  /** Indicate if it is picked winners. */
  private boolean pickedWinners;

  /** Event associated. */
  private EventInfo event;

  /** Type options list. */
  private Set<TypeOption> typeOptions = new HashSet<TypeOption>();

  /**
   * Blank constructor for betType.
   */
  public BetType() {

  }

  /**
   * BetType constructor.
   * @param questionParam
   *          Question of the bet type
   * @param multipleParam
   *          Indicates if it is multiple
   * @param eventParam
   *          Event associated
   */
  public BetType(final String questionParam,
                      final boolean multipleParam, final EventInfo eventParam) {

    /**
     * NOTE: "typeId" *must* be left as "null" since its value is automatically
     * generated.
     */
    this.question = questionParam;
    this.isMultiple = multipleParam;
    this.pickedWinners = false;
    this.event = eventParam;
  }

  /**
   * Get the type id.
   * @return type id
   */
  @Id
  @SequenceGenerator(// It only takes effect for
      name = "BetTypeIdGenerator", // databases providing identifier
      sequenceName = "BetTypeSeq") // generators.
  @GeneratedValue(strategy = GenerationType.AUTO,
                                    generator = "BetTypeIdGenerator")
  public final Long getTypeId() {
    return typeId;
  }

  /**
   * Set type id.
   * @param typeIdParam
   *          Type id
   */
  public final void setTypeId(final Long typeIdParam) {
    this.typeId = typeIdParam;
  }

  /**
   * Get the question.
   * @return question
   */
  public final String getQuestion() {
    return question;
  }

  /**
   * Set question.
   * @param questionParam
   *          question
   */
  public final void setQuestion(final String questionParam) {
    this.question = questionParam;
  }

  /**
   * Get if it is multiple.
   * @return boolean
   */
  @Type(type = "org.hibernate.type.BooleanType")
  public final Boolean getIsMultiple() {
    return isMultiple;
  }

  /**
   * Set if it is multiple.
   * @param isMultipleParam
   *            indicates if it is multiple
   */
  public final void setIsMultiple(final Boolean isMultipleParam) {
    this.isMultiple = isMultipleParam;
  }

  /**
   * Set if it is picked winners.
   * @return boolean
   */
  @Type(type = "org.hibernate.type.BooleanType")
  public final boolean getPickedWinners() {
    return pickedWinners;
  }

  /**
   * Set if it is picked winners.
   * @param pickedWinnersParam
   *            Indicates if it is picked winners
   */
  public final void setPickedWinners(final boolean pickedWinnersParam) {
    this.pickedWinners = pickedWinnersParam;
  }

  /**
   * Get event associated.
   * @return event
   */
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "eventId")
  public final EventInfo getEvent() {
    return event;
  }

  /**
   * Set event associated.
   * @param eventParam
   *        event
   */
  public final void setEvent(final EventInfo eventParam) {
    this.event = eventParam;
  }

  /**
   * Get typeOptions.
   * @return List of typeOptions
   */
  @OneToMany(mappedBy = "type")
  public final Set<TypeOption> getTypeOptions() {
    return typeOptions;
  }

  /**
   * Set typeOptions.
   * @param typeOptionsParam
   *            TypeOptions list
   */
  public final void setTypeOptions(final Set<TypeOption> typeOptionsParam) {
    this.typeOptions = typeOptionsParam;
  }

  /**
   * Add a type option to the TypeOptions list.
   * @param typeOption
   *            TypeOption to add
   */
  public final void addTypeOption(final TypeOption typeOption) {
    this.typeOptions.add(typeOption);
    typeOption.setType(this);
  }

  /**
   * Transform betType to string.
   */
  @Override
  public final String toString() {
    return "BetType [typeId=" + typeId + ", question=" + question
        + ", isMultiple=" + isMultiple + ", event=" + event.getEventId() + "]";
  }
}
