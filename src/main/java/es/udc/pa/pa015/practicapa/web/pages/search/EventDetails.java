package es.udc.pa.pa015.practicapa.web.pages.search;

import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.eventService.EventService;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class EventDetails {
	
	private Long eventId;
	
	private EventInfo event;
	
	private BetType betType;
	
	@Inject
	private EventService eventService;
		
	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	
	public EventInfo getEvent() {
		return event;
	}
	
	public BetType getBetType() {
		return betType;
	}

	public void setBetType(BetType betType) {
		this.betType = betType;
	}
	
	void onActivate(Long eventId) {	
		this.eventId = eventId;
		
		try {
			event = eventService.findEvent(eventId);
		} catch (InstanceNotFoundException e) {
		}
	}
	
	Long onPassivate() {
		return eventId;
	}

}
