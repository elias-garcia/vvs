package es.udc.pa.pa015.practicapa.web.pages.search;

import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.eventService.EventService;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.web.pages.admin.PickWinners;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa015.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.Calendar;

/**
 * Class of eventDetails page.
 */
@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class EventDetails {

  /** userSession. */
  @Property
  @SessionState(create = false)
  private UserSession userSession;

  /** eventId. */
  @Property
  private Long eventId;

  /** eventInfo. */
  @Property
  private EventInfo event;

  /** Indicates if the event has started. */
  @Property
  private boolean eventHasStarted;

  /** betType. */
  @Property
  private BetType betType;

  /** typeOption. */
  @Property
  private TypeOption typeOption;

  /** eventService. */
  @Inject
  private EventService eventService;

  /** pickWinners. */
  @InjectPage
  private PickWinners pickWinners;

  /**
   * onActivate.
   * @param eventIdParam eventId
   */
  final void onActivate(final Long eventIdParam) {
    this.eventId = eventIdParam;

    try {
      event = eventService.findEvent(eventIdParam);

      Calendar now = Calendar.getInstance();
      if (event.getEventDate().before(now)) {
        eventHasStarted = true;
      } else {
        eventHasStarted = false;
      }
    } catch (InstanceNotFoundException e) {
    }
  }

  /**
   * onPassivate.
   * @return eventId
   */
  final Long onPassivate() {
    return eventId;
  }

}
