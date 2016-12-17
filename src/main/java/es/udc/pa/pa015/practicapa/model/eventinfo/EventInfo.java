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

@Entity
@Immutable
@BatchSize(size = 10)
public class EventInfo {

  private Long eventId;
  private String eventName;
  private Calendar eventDate;
  private CategoryInfo category;
  private Set<BetType> betTypes = new HashSet<BetType>();

  public EventInfo() {

  }

  /**
   * Constructor of the eventInfo.
   * @param eventName
   *          Name of the event
   * @param eventDate
   *          Date of the event
   * @param category
   *          Category of the event
   */
  public EventInfo(String eventName, Calendar eventDate,
      CategoryInfo category) {

    /**
     * NOTE: "eventId" *must* be left as "null" since its value is automatically
     * generated.
     */

    this.eventName = eventName;
    this.eventDate = eventDate;
    this.category = category;
  }

  @Id
  @SequenceGenerator( // It only takes effect for
      name = "EventInfoIdGenerator", // databases providing identifier
      sequenceName = "EventInfoSeq") // generators.
  @GeneratedValue(strategy = GenerationType.AUTO, 
                                generator = "EventInfoIdGenerator")
  public Long getEventId() {
    return eventId;
  }

  public void setEventId(Long eventId) {
    this.eventId = eventId;
  }

  public String getEventName() {
    return eventName;
  }

  public void setEventName(String eventName) {
    this.eventName = eventName;
  }

  @Temporal(TemporalType.TIMESTAMP)
  public Calendar getEventDate() {
    return eventDate;
  }

  public void setEventDate(Calendar eventDate) {
    this.eventDate = eventDate;
  }

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "categoryId")
  public CategoryInfo getCategory() {
    return category;
  }

  public void setCategory(CategoryInfo category) {
    this.category = category;
  }

  @OneToMany(mappedBy = "event")
  public Set<BetType> getBetTypes() {
    return betTypes;
  }

  public void setBetTypes(Set<BetType> betTypes) {
    this.betTypes = betTypes;
  }

  public void addBetType(BetType betType) {
    this.betTypes.add(betType);
    betType.setEvent(this);
  }

  @Override
  public String toString() {
    return "EventInfo [eventId=" + eventId + ", eventName=" + eventName
        + ", eventDate=" + eventDate + ", category=" + category.getCategoryId()
        + "]";
  }
}
