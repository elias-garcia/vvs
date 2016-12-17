package es.udc.pa.pa015.practicapa.web.pages.admin;

import es.udc.pa.pa015.practicapa.model.betservice.TypeNotMultipleException;
import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.eventService.EventService;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Checklist;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.internal.services.StringValueEncoder;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AuthenticationPolicy(AuthenticationPolicyType.ADMIN_USER)
public class PickWinners {

  @Property
  private final StringValueEncoder 
                  stringValueEncoder = new StringValueEncoder();

  @Property
  private Long eventId;

  @Property
  private Long betTypeId;

  @Property
  private String typeOptionsModel;

  @Property
  private List<String> pickedWinners;

  @Property
  private boolean showZone = false;

  @Component
  private Form pickWinnersForm;

  @Component
  private Checklist winnersChecklist;

  @Inject
  private Messages messages;

  @Inject
  private Request request;

  @InjectComponent
  private Zone pickedWinnersZone;

  @InjectComponent
  private Zone formZone;

  @Inject
  private AjaxResponseRenderer ajaxResponseRenderer;

  @Inject
  EventService eventService;

  private Set<TypeOption> typeOptions;

  void onValidateFromPickWinnersForm() {
    List<Long> optionsIds = new ArrayList<Long>();

    for (TypeOption tempOption : typeOptions) {
      if (pickedWinners.contains(tempOption.getResult())) {
        optionsIds.add(tempOption.getOptionId());
      }
    }

    try {
      eventService.pickWinners(optionsIds, betTypeId);
    } catch (TypeNotMultipleException ex) {

      pickWinnersForm.recordError(winnersChecklist, messages.get(
          "error-typeNotMultiple"));

    } catch (InstanceNotFoundException ex) {
    }
  }

  void onSuccess() {
    if (request.isXHR()) {
      showZone = true;
      ajaxResponseRenderer.addRender(formZone).addRender(pickedWinnersZone);
    }
  }

  void onFailure() {
    if (request.isXHR()) {
      ajaxResponseRenderer.addRender(formZone);
    }
  }

  Object onPassivate() {
    return new Object[] { eventId, betTypeId };
  }

  void onActivate(Long eventId, Long betTypeId) {
    this.eventId = eventId;
    this.betTypeId = betTypeId;

    try {
      EventInfo event = eventService.findEvent(eventId);
      Set<BetType> betTypes = event.getBetTypes();

      for (BetType type : betTypes) {
        if (type.getTypeId() == betTypeId) {
          typeOptions = type.getTypeOptions();

          for (TypeOption option : typeOptions) {
            if (typeOptionsModel == null) {
              typeOptionsModel = option.getResult();
            } else {
              typeOptionsModel += (", " + option.getResult());
            }
          }

          break;
        }
      }

    } catch (InstanceNotFoundException ex) {
    }
  }

}
