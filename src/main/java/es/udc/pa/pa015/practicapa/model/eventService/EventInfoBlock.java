package es.udc.pa.pa015.practicapa.model.eventService;

import java.util.List;

import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;

public class EventInfoBlock {

	private List<EventInfo> events;
	private boolean existMoreEvents;

	public EventInfoBlock(List<EventInfo> events, boolean existMoreEvents) {
		this.events = events;
		this.existMoreEvents = existMoreEvents;
	}

	public List<EventInfo> getEvents() {
		return events;
	}

	public boolean getExistMoreEvents() {
		return existMoreEvents;
	}

	public void setEvents(List<EventInfo> events) {
		this.events = events;
	}

	public void setExistMoreEvents(boolean existMoreEvents) {
		this.existMoreEvents = existMoreEvents;
	}

}
