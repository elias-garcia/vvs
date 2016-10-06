package es.udc.pa.pa015.practicapa.model.eventService;

@SuppressWarnings("serial")
public class EventDateException extends Exception {
	
	private String eventName;

	public EventDateException(String eventName) {
		
		super("Event Date is previous to the current date => " +
	            "eventName = " + eventName);
		
		this.eventName = eventName;
	}
	
	public String getEventName() {
		return eventName;
	}
}
