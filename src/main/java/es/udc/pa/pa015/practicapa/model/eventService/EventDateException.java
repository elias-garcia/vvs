package es.udc.pa.pa015.practicapa.model.eventService;

/**
 * Exception that thrown out when the date is previous to the current date.
 */
@SuppressWarnings("serial")
public class EventDateException extends Exception {

  /** Event name. */
  private String eventName;

  /**
   * Constructor of the exception.
   * @param eventNameParam
   *          The event name that throw the exception
   */
  public EventDateException(final String eventNameParam) {

    super("Event Date is previous to the current date => " + "eventName = "
        + eventNameParam);

    this.eventName = eventNameParam;
  }

  /**
   * Get the event name.
   * @return event name
   */
  public final String getEventName() {
    return eventName;
  }
}
