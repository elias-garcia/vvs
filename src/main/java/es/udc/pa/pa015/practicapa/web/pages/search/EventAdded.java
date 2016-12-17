package es.udc.pa.pa015.practicapa.web.pages.search;

public class EventAdded {

  private Long eventId;

  public Long getEventId() {
    return eventId;
  }

  public void setEventId(Long accountId) {
    this.eventId = accountId;
  }

  Long onPassivate() {
    return eventId;
  }

  void onActivate(Long accountId) {
    this.eventId = accountId;
  }

}
