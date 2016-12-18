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

/**
 * Event service implementation.
 */
@Service("eventService")
@Transactional
public class EventServiceImpl implements EventService {

  /** EventInfoDao. */
  @Autowired
  private EventInfoDao eventInfoDao;

  /** BetTypeDao. */
  @Autowired
  private BetTypeDao betTypeDao;

  /** TypeOptionDao. */
  @Autowired
  private TypeOptionDao typeOptionDao;

  /** CategoryInfoDao. */
  @Autowired
  private CategoryInfoDao categoryInfoDao;

  /**
   * This method create a new event.
   * @param eventName
   *            event name
   * @param eventDate
   *            event date
   * @param categoryId
   *            category associated
   * @return the created event
   * @throws InstanceNotFoundException
   *             thrown out when the category doesn't exist
   * @throws EventDateException
   *              thrown out when the date is previous to the current date
   * @throws NullEventNameException
   *              thrown out when the event name is null
   */
  public final EventInfo createEvent(final String eventName,
      final Calendar eventDate, final Long categoryId)
      throws InstanceNotFoundException, EventDateException,
      NullEventNameException {

    if (eventName == null) {
      throw new NullEventNameException();
    }

    CategoryInfo category = categoryInfoDao.find(categoryId);

    /*
     * If date is previous to the current date or is null, then throw an
     * exception
     */
    if (eventDate == null || eventDate.before(Calendar.getInstance())) {
      throw new EventDateException(eventName);
    }

    EventInfo newEvent = new EventInfo(eventName, eventDate, category);

    eventInfoDao.save(newEvent);

    return newEvent;
  }

  /**
   * Find an event by a passed id.
   * @param eventId
   *          event id to search
   * @return the event found
   * @throws InstanceNotFoundException
   *          thrown out when the event id doesn't exist
   */
  @Transactional(readOnly = true)
  public final EventInfo findEvent(final Long eventId)
                                   throws InstanceNotFoundException {
    EventInfo event = eventInfoDao.find(eventId);
    return event;
  }

  /**
   * Find all the events that contains the keywords, categoryId or have started.
   * @param keywords
   *              keyword to search
   * @param categoryId
   *              category to search
   * @param eventsStarted
   *              indicates if the events must started
   * @param startIndex
   *              number of first element of the list
   * @param count
   *              number of elements to list
   * @return EventInfoBlock
   * @throws InstanceNotFoundException
   *              thrown out when the category doesn't exist
   * @throws StartIndexOrCountException
   *              thrown out when startindex and count are not correct
   */
  @Transactional(readOnly = true)
  public final EventInfoBlock findEvents(final String keywords,
      final Long categoryId, final boolean eventsStarted,
      final int startIndex, final int count)
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
   * This method add a bet type to an event.
   * @param eventId
   *            Event for adding the betType
   * @param type
   *            BetType to add
   * @param options
   *            List of typeOptions
   * @throws InstanceNotFoundException
   *            thrown out when the event doesn't exist
   * @throws NoAssignedTypeOptionsException
   *            thrown out when there's not assigned typeOptions
   * @throws DuplicatedResultTypeOptionsException
   *            thrown out when the result is duplicated
   */
  public final void addBetType(final Long eventId, final BetType type,
      final List<TypeOption> options)
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
   * This method pick the winners of a betType.
   * @param optionIds
   *            List of optionsIds.
   * @param betTypeId
   *            typeId to search.
   * @throws InstanceNotFoundException
   *            thrown out when the betType doesn't exist
   * @throws TypeNotMultipleException
   *            thrown out when the betType is not multiple
   */
  public final void pickWinners(final List<Long> optionIds,
      final Long betTypeId)
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
      if (optionIds.contains(option.getOptionId())) {
        option.setIsWinner(true);
      } else {
        option.setIsWinner(false);
      }
      typeOptionDao.save(option);
    }

    type.setPickedWinners(true);
    betTypeDao.save(type);
  }

  /**
   * This method find all the categories.
   * @return list of categories
   */
  public final List<CategoryInfo> findAllCategories() {
    return categoryInfoDao.findAllCategories();
  }

}
