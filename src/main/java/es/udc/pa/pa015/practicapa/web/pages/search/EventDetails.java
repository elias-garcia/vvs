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

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class EventDetails {

  @Property
  @SessionState(create = false)
  private UserSession userSession;

  @Property
  private Long eventId;

  @Property
  private EventInfo event;

  @Property
  private boolean eventHasStarted;

  @Property
  private BetType betType;

  @Property
  private TypeOption typeOption;

  @Inject
  private EventService eventService;

  @InjectPage
  private PickWinners pickWinners;

  void onActivate(Long eventId) {
    this.eventId = eventId;

    try {
      event = eventService.findEvent(eventId);

      Calendar now = Calendar.getInstance();
      if (event.getEventDate().before(now)) {
        eventHasStarted = true;
      } else {
        eventHasStarted = false;
      }
    } catch (InstanceNotFoundException e) {
    }
  }

  Long onPassivate() {
    return eventId;
  }

}
