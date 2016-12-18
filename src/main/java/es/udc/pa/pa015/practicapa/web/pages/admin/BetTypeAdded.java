package es.udc.pa.pa015.practicapa.web.pages.admin;

/**
 * Class of betTypeAdded page.
 */
public class BetTypeAdded {

  /** event id. */
  private Long eventId;

  /** betType id. */
  private Long betTypeId;

  /**
   * Get event id.
   * @return event id
   */
  public final Long getEventId() {
    return eventId;
  }

  /**
   * Set event id.
   * @param eventIdParam event id
   */
  public final void setEventId(final Long eventIdParam) {
    this.eventId = eventIdParam;
  }

  /**
   * Get betTypeId.
   * @return betTypeId
   */
  public final Long getBetTypeId() {
    return betTypeId;
  }

  /**
   * Set betTypeId.
   * @param betTypeIdParam betTypeId
   */
  public final void setBetTypeId(final Long betTypeIdParam) {
    this.betTypeId = betTypeIdParam;
  }

  /**
   * onPassivate.
   * @return Object.
   */
  final Object onPassivate() {
    return new Object[] {eventId, betTypeId };
  }

  /**
   * onActivate.
   * @param eventIdParam eventId
   * @param betIdParam betId
   */
  final void onActivate(final Long eventIdParam, final Long betIdParam) {
    this.eventId = eventIdParam;
    this.betTypeId = betIdParam;
  }

}
