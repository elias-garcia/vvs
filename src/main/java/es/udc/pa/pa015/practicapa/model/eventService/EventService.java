package es.udc.pa.pa015.practicapa.model.eventService;

import es.udc.pa.pa015.practicapa.model.betservice.TypeNotMultipleException;
import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import java.util.Calendar;
import java.util.List;

public interface EventService {

  public EventInfo createEvent(String eventName, Calendar eventDate,
      Long categoryId) throws InstanceNotFoundException, EventDateException,
      NullEventNameException;

  public EventInfo findEvent(Long eventId) throws InstanceNotFoundException;

  public EventInfoBlock findEvents(String keywords, Long categoryId,
      boolean eventsStarted, int startIndex, int count)
      throws InstanceNotFoundException, StartIndexOrCountException;

  public void addBetType(Long eventId, BetType type, List<TypeOption> options)
      throws InstanceNotFoundException, NoAssignedTypeOptionsException,
      DuplicatedResultTypeOptionsException;

  public void pickWinners(List<Long> optionsIds, Long betTypeId)
      throws InstanceNotFoundException, TypeNotMultipleException;

  public List<CategoryInfo> findAllCategories();
}
