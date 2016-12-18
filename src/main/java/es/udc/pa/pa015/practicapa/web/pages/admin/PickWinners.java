package es.udc.pa.pa015.practicapa.web.pages.admin;

import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.eventService.EventService;
import es.udc.pa.pa015.practicapa.model.eventService.TypeNotMultipleException;
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

/**
 * Class of pickwinners page.
 */
@AuthenticationPolicy(AuthenticationPolicyType.ADMIN_USER)
public class PickWinners {

  /** stringValueEncoder. */
  @Property
  private final
            StringValueEncoder stringValueEncoder = new StringValueEncoder();

  /** eventId. */
  @Property
  private Long eventId;

  /** betTypeId. */
  @Property
  private Long betTypeId;

  /** typeOptionModel. */
  @Property
  private String typeOptionsModel;

  /** pickedWinners. */
  @Property
  private List<String> pickedWinners;

  /** indicates if it is a showzone. */
  @Property
  private boolean showZone = false;

  /** pickWinnersForm. */
  @Component
  private Form pickWinnersForm;

  /** winnersChecklist. */
  @Component
  private Checklist winnersChecklist;

  /** Messages. */
  @Inject
  private Messages messages;

  /** Request. */
  @Inject
  private Request request;

  /** pickedWinnersZone. */
  @InjectComponent
  private Zone pickedWinnersZone;

  /** formZone. */
  @InjectComponent
  private Zone formZone;

  /** AjaxResponseRenderer. */
  @Inject
  private AjaxResponseRenderer ajaxResponseRenderer;

  /** eventService. */
  @Inject
  private EventService eventService;

  /** typeOptions. */
  private Set<TypeOption> typeOptions;

  /**
   * Method to validate the pickWinnersForm.
   */
  final void onValidateFromPickWinnersForm() {
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

  /**
   * Method when the result is success.
   */
  final void onSuccess() {
    if (request.isXHR()) {
      showZone = true;
      ajaxResponseRenderer.addRender(formZone).addRender(pickedWinnersZone);
    }
  }

  /**
   * Method when the result is failure.
   */
  final void onFailure() {
    if (request.isXHR()) {
      ajaxResponseRenderer.addRender(formZone);
    }
  }

  /**
   * onPassivate.
   * @return Object
   */
  final Object onPassivate() {
    return new Object[] {eventId, betTypeId };
  }

  /**
   * onActivate.
   * @param eventIdParam eventId
   * @param betTypeIdParam typeId
   */
  final void onActivate(final Long eventIdParam, final Long betTypeIdParam) {
    this.eventId = eventIdParam;
    this.betTypeId = betTypeIdParam;

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
