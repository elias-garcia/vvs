package es.udc.pa.pa015.practicapa.model.eventService;

import es.udc.pa.pa015.practicapa.model.betservice.TypeNotMultipleException;
import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.bettype.BetTypeDao;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfoDao;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfoDao;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOptionDao;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

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

  /**
   * This method create a new event.
   */
  public EventInfo createEvent(String eventName, Calendar eventDate,
      Long categoryId) throws InstanceNotFoundException, EventDateException,
      NullEventNameException {

    if (eventName == null) {
      throw new NullEventNameException();
    }

    CategoryInfo category = categoryInfoDao.find(categoryId);

		/*
		 * If date is previous to the current date or is null, then throw an
		 * exception
		 */
		if (eventDate == null || eventDate.before(Calendar.getInstance()))
			throw new EventDateException(eventName);

    EventInfo newEvent = new EventInfo(eventName, eventDate, category);

    eventInfoDao.save(newEvent);

    return newEvent;
  }

  @Transactional(readOnly = true)
  public EventInfo findEvent(Long eventId) throws InstanceNotFoundException {
    EventInfo event = eventInfoDao.find(eventId);
    return event;
  }

  /**
   * This method find events.
   */
  @Transactional(readOnly = true)
  public EventInfoBlock findEvents(String keywords, Long categoryId,
      boolean eventsStarted, int startIndex, int count)
      throws InstanceNotFoundException, StartIndexOrCountException {

    if (startIndex < 0 || count < 0) {
      throw new StartIndexOrCountException();
    }

    /*
     * Find count+1 events to determine if there exist more events above the
     * specified range.
     */
    List<EventInfo> events = eventInfoDao.findEvents(keywords, categoryId,
        eventsStarted, startIndex, count + 1);

    boolean existMoreEvents = events.size() == (count + 1);

    /*
     * Remove the last event from the returned list if there exist more events
     * above the specified range.
     */
    if (existMoreEvents) {
      events.remove(events.size() - 1);
    }

    /* Return EventInfoBlock. */
    return new EventInfoBlock(events, existMoreEvents);
  }

  /**
   * This method add a bet type.
   */
  public void addBetType(Long eventId, BetType type, List<TypeOption> options)
      throws InstanceNotFoundException, NoAssignedTypeOptionsException,
      DuplicatedResultTypeOptionsException {

    if (options == null || options.isEmpty()) {
      throw new NoAssignedTypeOptionsException();
    }

    EventInfo event = eventInfoDao.find(eventId);

    event.addBetType(type);
    betTypeDao.save(type);

    List<String> results = new ArrayList<>();

    for (TypeOption option : options) {
      if (results.contains(option.getResult())) {
        throw new DuplicatedResultTypeOptionsException();
      }
      results.add(option.getResult());
      type.addTypeOption(option);
      typeOptionDao.save(option);
    }
  }

  /**
   * This method put like winners the typeIptions of a betType.
   */
  public void pickWinners(List<Long> optionIds, Long betTypeId)
      throws InstanceNotFoundException, TypeNotMultipleException {

    // Puede lanzar exception = 1 CP
    BetType type = betTypeDao.find(betTypeId);

    // Obitene las TypeOptions del BetType
    Set<TypeOption> typeOptions = type.getTypeOptions();

    /*
     * First, if optionsIds is null or empty, pick all options to false and exit
     */

    // Si no se pasaron optionsId se ponen todas a false
    if (optionIds == null || optionIds.size() == 0) {
      for (TypeOption option : typeOptions) {
        option.setIsWinner(false);
        typeOptionDao.save(option);
      }

      type.setPickedWinners(true);
      betTypeDao.save(type);

      return;
    }

    /* If not, we need to check that TypeOptions exists */
    // Se comprueban que los ids de las options pasadas existen
    // Puede devolver exception
    for (Long option : optionIds) {
      typeOptionDao.find(option);
    }

    /*
     * If we want to pick more than one option as a winner but it's type doesn't
     * allow multiple options, throw an exception.
     */

    if (!type.getIsMultiple() && optionIds.size() > 1) {
      throw new TypeNotMultipleException(type.getTypeId());
    }

		/* Last, pick options as winners */
		for (TypeOption option : typeOptions) {
			if (optionIds.contains(option.getOptionId()))
				option.setIsWinner(true);
			else
				option.setIsWinner(false);

			typeOptionDao.save(option);
		}

    type.setPickedWinners(true);
    betTypeDao.save(type);
  }

  public List<CategoryInfo> findAllCategories() {
    return categoryInfoDao.findAllCategories();
  }

}
