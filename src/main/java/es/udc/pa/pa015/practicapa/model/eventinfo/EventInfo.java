package es.udc.pa.pa015.practicapa.model.eventinfo;

import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;

import org.hibernate.annotations.BatchSize;

import java.util.Calendar;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Event info class.
 */
@Entity
@Immutable
@BatchSize(size = 10)
public class EventInfo {

  /** event id. */
  private Long eventId;

  /** event name. */
  private String eventName;

  /** event date. */
  private Calendar eventDate;

  /** category associated. */
  private CategoryInfo category;

  /** list of betTypes. */
  private Set<BetType> betTypes = new HashSet<BetType>();

  /**
   * Blank constructor.
   */
  public EventInfo() {

  }

  /**
   * Constructor of the eventInfo.
   * @param eventNameParam
   *          Name of the event
   * @param eventDateParam
   *          Date of the event
   * @param categoryParam
   *          Category of the event
   */
  public EventInfo(final String eventNameParam,
      final Calendar eventDateParam, final CategoryInfo categoryParam) {

    /**
     * NOTE: "eventId" *must* be left as "null" since its value is automatically
     * generated.
     */

    this.eventName = eventNameParam;
    this.eventDate = eventDateParam;
    this.category = categoryParam;
  }

  /**
   * Get event id.
   * @return event id
   */
  @Id
  @SequenceGenerator(// It only takes effect for
      name = "EventInfoIdGenerator", // databases providing identifier
      sequenceName = "EventInfoSeq") // generators.
  @GeneratedValue(strategy = GenerationType.AUTO,
                                    generator = "EventInfoIdGenerator")
  public final Long getEventId() {
    return eventId;
  }

  /**
   * Set event id.
   * @param eventIdParam
   *          event id
   */
  public final void setEventId(final Long eventIdParam) {
    this.eventId = eventIdParam;
  }

  /**
   * Get event name.
   * @return event name
   */
  public String getEventName() {
    return eventName;
  }

  /**
   * Set event name.
   * @param eventNameParam
   *            event name
   */
  public void setEventName(final String eventNameParam) {
    this.eventName = eventNameParam;
  }

  /**
   * Get event date.
   * @return event date
   */
  @Temporal(TemporalType.TIMESTAMP)
  public Calendar getEventDate() {
    return eventDate;
  }

  /**
   * Set event date.
   * @param eventDateParam
   *          event date
   */
  public void setEventDate(final Calendar eventDateParam) {
    this.eventDate = eventDateParam;
  }

  /**
   * Get category associated.
   * @return category associated
   */
  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "categoryId")
  public CategoryInfo getCategory() {
    return category;
  }

  /**
   * Set category associated.
   * @param categoryParam
   *          category associated
   */
  public void setCategory(final CategoryInfo categoryParam) {
    this.category = categoryParam;
  }

  /**
   * Get betTypes.
   * @return list of betTypes
   */
  @OneToMany(mappedBy = "event")
  public Set<BetType> getBetTypes() {
    return betTypes;
  }

  /**
   * Set betTypes.
   * @param betTypesParam
   *      List of betTypes
   */
  public void setBetTypes(final Set<BetType> betTypesParam) {
    this.betTypes = betTypesParam;
  }

  /**
   * This method add a betType to the list.
   * @param betType
   *        BetType to adding
   */
  public final void addBetType(final BetType betType) {
    this.betTypes.add(betType);
    betType.setEvent(this);
  }

  /**
   * Transform event to string.
   */
  @Override
  public final String toString() {
    return "EventInfo [eventId=" + eventId + ", eventName=" + eventName
        + ", eventDate=" + eventDate + ", category=" + category.getCategoryId()
        + "]";
  }
}
