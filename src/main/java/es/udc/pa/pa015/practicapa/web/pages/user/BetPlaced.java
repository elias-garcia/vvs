package es.udc.pa.pa015.practicapa.web.pages.user;

/**
 * Class of the betPlaced page.
 */
public class BetPlaced {

  /** eventId. */
  private Long eventId;

  /** betId. */
  private Long betId;

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
   * Get bet id.
   * @return betId
   */
  public final Long getBetId() {
    return betId;
  }

  /**
   * Set bet id.
   * @param betIdParam betId
   */
  public final void setBetId(final Long betIdParam) {
    this.betId = betIdParam;
  }

  /**
   * onPassivate.
   * @return Object
   */
  final Object[] onPassivate() {
    return new Object[] {eventId, betId };
  }

  /**
   * onActivate.
   * @param eventIdParam eventId
   * @param betIdParam betId
   */
  final void onActivate(final Long eventIdParam, final Long betIdParam) {
    this.eventId = eventIdParam;
    this.betId = betIdParam;
  }

}
