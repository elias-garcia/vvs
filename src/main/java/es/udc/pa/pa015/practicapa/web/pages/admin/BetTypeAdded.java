package es.udc.pa.pa015.practicapa.web.pages.admin;

public class BetTypeAdded {

  private Long eventId;

  private Long betTypeId;

  public Long getEventId() {
    return eventId;
  }

  public void setEventId(Long eventId) {
    this.eventId = eventId;
  }

  public Long getBetTypeId() {
    return betTypeId;
  }

  public void setBetTypeId(Long betTypeId) {
    this.betTypeId = betTypeId;
  }

  Object onPassivate() {
    return new Object[] { eventId, betTypeId };
  }

  void onActivate(Long eventId, Long betId) {
    this.eventId = eventId;
    this.betTypeId = betId;
  }

}
