package es.udc.pa.pa015.practicapa.web.pages.user;

public class BetPlaced {

	private Long eventId;
	
	private Long betId;
	
	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Long getBetId() {
		return betId;
	}

	public void setBetId(Long betId) {
		this.betId = betId;
	}

	Object[] onPassivate() {
		return new Object[] {eventId, betId};
	}
	
	void onActivate(Long eventId, Long betId) {
		this.eventId = eventId;
		this.betId = betId;
	}
	
}
