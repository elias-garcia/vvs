package es.udc.pa.pa015.practicapa.web.pages.admin;

/**
 * Class for evenAdded page.
 */
public class EventAdded {

  /** eventId. */
  private Long eventId;

  /**
   * Get event id.
   * @return event id
   */
  public final Long getEventId() {
    return eventId;
  }

  /**
   * Set event id.
   * @param accountIdParam account id
   */
  public final void setEventId(final Long accountIdParam) {
    this.eventId = accountIdParam;
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
