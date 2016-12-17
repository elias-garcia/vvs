package es.udc.pa.pa015.practicapa.web.pages.search;

import es.udc.pa.pa015.practicapa.model.eventService.EventInfoBlock;
import es.udc.pa.pa015.practicapa.model.eventService.EventService;
import es.udc.pa.pa015.practicapa.model.eventService.StartIndexOrCountException;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa015.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.text.DateFormat;
import java.util.List;
import java.util.Locale;

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class Events {

  private static final int EVENTS_PER_PAGE = 10;

  @Property
  @SessionState(create = false)
  private UserSession userSession;

  private String keywords;

  private Long categoryId;

  private int startIndex = 0;

  private EventInfo eventInfo;

  private EventInfoBlock eventInfoBlock;

  @Inject
  private EventService eventService;

  @Inject
  private Locale locale;

  public String getKeywords() {
    return keywords;
  }

  public void setKeywords(String keywords) {
    this.keywords = keywords;
  }

  public Long getCategoryId() {
    return categoryId;
  }

  public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
  }

  public List<EventInfo> getEvents() {
    return eventInfoBlock.getEvents();
  }

  public EventInfo getEventInfo() {
    return eventInfo;
  }

  public void setEventInfo(EventInfo event) {
    this.eventInfo = event;
  }

  public DateFormat getDateFormat() {
    return DateFormat.getDateInstance(DateFormat.SHORT, locale);
  }

  public DateFormat getTimeFormat() {
    return DateFormat.getTimeInstance(DateFormat.SHORT, locale);
  }

  /**
   * This method get the previous link context.
   * @return Object
   */
  public Object[] getPreviousLinkContext() {

    if (startIndex - EVENTS_PER_PAGE >= 0) {
      return new Object[] { keywords, categoryId, startIndex
          - EVENTS_PER_PAGE };
    } else {
      return null;
    }
  }

  /**
   * This method get the next link.
   * @return Object
   */
  public Object[] getNextLinkContext() {

    if (eventInfoBlock.getExistMoreEvents()) {
      return new Object[] { keywords, categoryId, startIndex
          + EVENTS_PER_PAGE };
    } else {
      return null;
    }
  }

  Object[] onPassivate() {
    return new Object[] { keywords, categoryId, startIndex };
  }

  void onActivate(String keywords, Long categoryId, int startIndex)
      throws InstanceNotFoundException, StartIndexOrCountException {
    this.keywords = keywords;
    this.categoryId = categoryId;
    this.startIndex = startIndex;

    if (userSession == null || !userSession.getAdmin()) {
      eventInfoBlock = eventService.findEvents(keywords, categoryId, false,
          startIndex, EVENTS_PER_PAGE);
    } else {
      eventInfoBlock = eventService.findEvents(keywords, categoryId, true,
          startIndex, EVENTS_PER_PAGE);
    }
  }
}
