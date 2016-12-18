package es.udc.pa.pa015.practicapa.model.eventService;

import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;

import java.util.List;

/**
 * EventInfoBlock class.
 */
public class EventInfoBlock {

  /** Events list. */
  private List<EventInfo> events;

  /** Indicates if exist more events. */
  private boolean existMoreEvents;

  /**
   * EventInfoBlock constructor.
   * @param eventsParam
   *            List of events
   * @param existMoreEventsParam
   *            Indicates if exist more events
   */
  public EventInfoBlock(final List<EventInfo> eventsParam,
                                    final boolean existMoreEventsParam) {
    this.events = eventsParam;
    this.existMoreEvents = existMoreEventsParam;
  }

  /**
   * Get events.
   * @return List of events
   */
  public final List<EventInfo> getEvents() {
    return events;
  }

  /**
   * Get if exist more events.
   * @return boolean
   */
  public final boolean getExistMoreEvents() {
    return existMoreEvents;
  }

  /**
   * Set events.
   * @param eventsParam
   *            List of events
   */
  public final void setEvents(final List<EventInfo> eventsParam) {
    this.events = eventsParam;
  }

  /**
   * Set if exist more events.
   * @param existMoreEventsParam
   *                Indicates if exist more events
   */
  public final void setExistMoreEvents(final boolean existMoreEventsParam) {
    this.existMoreEvents = existMoreEventsParam;
  }

}
