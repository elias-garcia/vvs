package es.udc.pa.pa015.practicapa.model.eventService;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.bettype.BetTypeDao;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfoDao;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfoDao;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOptionDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@Service("eventService")
@Transactional
public class EventServiceImpl implements EventService {

	@Autowired
	private EventInfoDao eventInfoDao;
	
	@Autowired
	private BetTypeDao betTypeDao;
	
	@Autowired
	private TypeOptionDao typeOptionDao;
	
	@Autowired
	private CategoryInfoDao categoryInfoDao;
	
	public EventInfo createEvent(String eventName, Calendar eventDate, Long categoryId)
			throws InstanceNotFoundException, EventDateException {
		
		CategoryInfo category = categoryInfoDao.find(categoryId);

		/* If date is previous to the current date, then throw an exception*/
		if (eventDate.before(Calendar.getInstance()))
			throw new EventDateException(eventName);
		
		EventInfo newEvent = new EventInfo(eventName, eventDate, category);
		
		eventInfoDao.save(newEvent);
		
		return newEvent;
	}

	@Transactional(readOnly=true)
	public EventInfoBlock findEvents(String keywords, Long categoryId,
			boolean eventsStarted, int startIndex, int count) {

		/*
		 * Find count+1 events to determine if there exist more events above
		 * the specified range.
		 */
		List<EventInfo> events = eventInfoDao.findEvents(keywords, categoryId,
				eventsStarted, startIndex , count + 1);

		boolean existMoreEvents = events.size() == (count + 1);

		/*
		 * Remove the last event from the returned list if there exist more
		 * events above the specified range.
		 */
		if (existMoreEvents) {
			events.remove(events.size() - 1);
		}

		/* Return EventInfoBlock. */
		return new EventInfoBlock(events, existMoreEvents);
	}
	
	public void addBetType(Long eventId, BetType type, List<TypeOption> options)
			throws InstanceNotFoundException {
		
		EventInfo event = eventInfoDao.find(eventId);
		
		event.addBetType(type);
		betTypeDao.save(type);
		
		for (TypeOption option : options) {
			type.addTypeOption(option);
			typeOptionDao.save(option);
		}
	}
	
	public EventInfo findEvent(Long eventId)
			throws InstanceNotFoundException {
		EventInfo event = eventInfoDao.find(eventId);
		return event;
	}
	
	public List<CategoryInfo> findAllCategories() {
		return categoryInfoDao.findAllCategories();
	}
	
}