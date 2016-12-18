package es.udc.pa.pa015.practicapa.web.pages.search;

/**
 * Class of eventAdded page.
 */
public class EventAdded {

  /** eventId. */
  private Long eventId;

  /**
   * Get eventId.
   * @return eventId
   */
  public final Long getEventId() {
    return eventId;
  }

  /**
   * Set eventId.
   * @param accountId accountId
   */
  public final void setEventId(final Long accountId) {
    this.eventId = accountId;
  }

  /**
   * onPassivate.
   * @return eventId
   */
  final Long onPassivate() {
    return eventId;
  }

  /**
   * onActivate.
   * @param accountId accountId
   */
  final void onActivate(final Long accountId) {
    this.eventId = accountId;
  }

}
