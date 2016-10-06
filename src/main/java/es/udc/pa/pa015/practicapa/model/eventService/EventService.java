package es.udc.pa.pa015.practicapa.model.eventService;

import java.util.Calendar;
import java.util.List;

import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public interface EventService {

	public EventInfo createEvent(String eventName, Calendar eventDate, Long categoryId)
			throws InstanceNotFoundException, EventDateException;
	
	public EventInfoBlock findEvents(String keywords, Long categoryId,
			boolean eventsStarted, int startIndex, int count);
	
	public void addBetType(Long eventId, BetType type, List<TypeOption> options)
		throws InstanceNotFoundException;
	
	public EventInfo findEvent(Long eventId)throws InstanceNotFoundException;
	
	public List<CategoryInfo> findAllCategories();
}