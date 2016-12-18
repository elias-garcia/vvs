package es.udc.pa.pa015.practicapa.model.eventService;

import es.udc.pa.pa015.practicapa.model.betservice.TypeNotMultipleException;
import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import java.util.Calendar;
import java.util.List;

/**
 * Event service interface.
 */
public interface EventService {

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
  EventInfo createEvent(String eventName, Calendar eventDate,
      Long categoryId) throws InstanceNotFoundException, EventDateException,
      NullEventNameException;

  /**
   * Find an event by a passed id.
   * @param eventId
   *          event id to search
   * @return the event found
   * @throws InstanceNotFoundException
   *          thrown out when the event id doesn't exist
   */
  EventInfo findEvent(Long eventId) throws InstanceNotFoundException;

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
  EventInfoBlock findEvents(String keywords, Long categoryId,
      boolean eventsStarted, int startIndex, int count)
      throws InstanceNotFoundException, StartIndexOrCountException;

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
  void addBetType(Long eventId, BetType type, List<TypeOption> options)
      throws InstanceNotFoundException, NoAssignedTypeOptionsException,
      DuplicatedResultTypeOptionsException;

  /**
   * This method pick the winners of a betType.
   * @param optionsIds
   *            List of optionsIds.
   * @param betTypeId
   *            typeId to search.
   * @throws InstanceNotFoundException
   *            thrown out when the betType doesn't exist
   * @throws TypeNotMultipleException
   *            thrown out when the betType is not multiple
   */
  void pickWinners(List<Long> optionsIds, Long betTypeId)
      throws InstanceNotFoundException, TypeNotMultipleException;

  /**
   * This method find all the categories.
   * @return list of categories
   */
  List<CategoryInfo> findAllCategories();
}
