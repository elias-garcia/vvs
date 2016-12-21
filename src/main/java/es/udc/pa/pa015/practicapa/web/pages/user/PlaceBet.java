package es.udc.pa.pa015.practicapa.web.pages.user;

import es.udc.pa.pa015.practicapa.model.betservice.BetService;
import es.udc.pa.pa015.practicapa.model.betservice.NegativeAmountException;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa015.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

/**
 * Class of the placeBet page.
 */
@AuthenticationPolicy(AuthenticationPolicyType.NON_ADMIN_AUTHENTICATED_USERS)
public class PlaceBet {

  /**
   * userSession.
   */
  @Property
  @SessionState(create = false)
  private UserSession userSession;

  /**
   * eventId.
   */
  @Property
  private Long eventId;

  /**
   * Indicates if it is showZone.
   */
  @Property
  private boolean showZone = false;

  /**
   * typeOptionId.
   */
  @Property
  private Long typeOptionId;

  /**
   * betAmount.
   */
  @Property
  private Long betAmount;

  /**
   * betService.
   */
  @Inject
  private BetService betService;

  /**
   * placeBetForm.
   */
  @Component
  private Form placeBetForm;

  /**
   * betAmount TextField.
   */
  @Component(id = "betAmount")
  private TextField betAmountTextField;

  /**
   * Request.
   */
  @Inject
  private Request request;

  /**
   * betPlacedZone.
   */
  @InjectComponent
  private Zone betPlacedZone;

  /**
   * formZone.
   */
  @InjectComponent
  private Zone formZone;

  /**
   * ajaxResponseRenderer.
   */
  @Inject
  private AjaxResponseRenderer ajaxResponseRenderer;

  /**
   * onPassivate.
   * @return Object
   */
  final Object onPassivate() {
    return new Object[] {eventId, typeOptionId };
  }

  /**
   * onActivate.
   * @param eventIdParam eventId
   * @param typeOptionIdParam optionId
   */
  final void onActivate(final Long eventIdParam,
                                        final Long typeOptionIdParam) {
    this.eventId = eventIdParam;
    this.typeOptionId = typeOptionIdParam;
  }

  /**
   * Method to validate the placeBetForm.
   */
  final void onValidateFromPlaceBetForm() {
    if (!placeBetForm.isValid()) {
      return;
    }

    try {
      betService.createBet(userSession.getUserProfileId(), typeOptionId,
          betAmount);
    } catch (InstanceNotFoundException e) {
    } catch (NegativeAmountException e) {
    }
  }

  /**
   * Method when the result is success.
   */
  final void onSuccess() {
    if (request.isXHR()) {
      showZone = true;
      ajaxResponseRenderer.addRender(formZone).addRender(betPlacedZone);
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

}
