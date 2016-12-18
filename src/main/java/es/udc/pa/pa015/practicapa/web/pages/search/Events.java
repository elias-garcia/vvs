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

/**
 * Class of events page.
 */
@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class Events {

  /** Event_per_page. */
  private static final int EVENTS_PER_PAGE = 10;

  /** userSession. */
  @Property
  @SessionState(create = false)
  private UserSession userSession;

  /** keyWords. */
  private String keywords;

  /** CategoryId. */
  private Long categoryId;

  /** Number of first element in the list. */
  private int startIndex = 0;

  /** eventInfo. */
  private EventInfo eventInfo;

  /** eventInfoBlock. */
  private EventInfoBlock eventInfoBlock;

  /** eventService. */
  @Inject
  private EventService eventService;

  /** Locale. */
  @Inject
  private Locale locale;

  /**
   * Get keywords.
   * @return keywords
   */
  public final String getKeywords() {
    return keywords;
  }

  /**
   * Set keywords.
   * @param keywordsParam keywords
   */
  public final void setKeywords(final String keywordsParam) {
    this.keywords = keywordsParam;
  }

  /**
   * Get category id.
   * @return category id
   */
  public final Long getCategoryId() {
    return categoryId;
  }

  /**
   * Set categoryId.
   * @param categoryIdParam categoryId
   */
  public final void setCategoryId(final Long categoryIdParam) {
    this.categoryId = categoryIdParam;
  }

  /**
   * Get events.
   * @return list of eventInfo
   */
  public final List<EventInfo> getEvents() {
    return eventInfoBlock.getEvents();
  }

  /**
   * Get event info.
   * @return eventInfo
   */
  public final EventInfo getEventInfo() {
    return eventInfo;
  }

  /**
   * Set event info.
   * @param eventParam evenInfo
   */
  public final void setEventInfo(final EventInfo eventParam) {
    this.eventInfo = eventParam;
  }

  /**
   * Get dateFormat.
   * @return dateFormat
   */
  public final DateFormat getDateFormat() {
    return DateFormat.getDateInstance(DateFormat.SHORT, locale);
  }

  /**
   * Get timeFormat.
   * @return timeFormat
   */
  public final DateFormat getTimeFormat() {
    return DateFormat.getTimeInstance(DateFormat.SHORT, locale);
  }

  /**
   * This method get the previous link context.
   * @return Object
   */
  public final Object[] getPreviousLinkContext() {

    if (startIndex - EVENTS_PER_PAGE >= 0) {
      return new Object[] {keywords, categoryId, startIndex
          - EVENTS_PER_PAGE };
    } else {
      return null;
    }
  }

  /**
   * This method get the next link.
   * @return Object
   */
  public final Object[] getNextLinkContext() {

    if (eventInfoBlock.getExistMoreEvents()) {
      return new Object[] {keywords, categoryId, startIndex
          + EVENTS_PER_PAGE };
    } else {
      return null;
    }
  }

  /**
   * onPassivate.
   * @return Object
   */
  final Object[] onPassivate() {
    return new Object[] {keywords, categoryId, startIndex };
  }

  /**
   * onActivate.
   * @param keywordsParam keywords
   * @param categoryIdParam category id
   * @param startIndexParam first element of the list
   * @throws InstanceNotFoundException
   *                  thrown out when the category doesn't exist
   * @throws StartIndexOrCountException
   *                  thrown out when startindex or count are not corrects
   */
  final void onActivate(final String keywordsParam, final Long categoryIdParam,
              final int startIndexParam) throws InstanceNotFoundException,
              StartIndexOrCountException {
    this.keywords = keywordsParam;
    this.categoryId = categoryIdParam;
    this.startIndex = startIndexParam;

    if (userSession == null || !userSession.getAdmin()) {
      eventInfoBlock = eventService.findEvents(keywords, categoryId, false,
          startIndex, EVENTS_PER_PAGE);
    } else {
      eventInfoBlock = eventService.findEvents(keywords, categoryId, true,
          startIndex, EVENTS_PER_PAGE);
    }
  }
}
