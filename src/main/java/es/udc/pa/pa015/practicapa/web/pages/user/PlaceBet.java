package es.udc.pa.pa015.practicapa.web.pages.user;

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

import es.udc.pa.pa015.practicapa.model.betservice.BetService;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa015.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.NON_ADMIN_AUTHENTICATED_USERS)
public class PlaceBet {

	@Property
	@SessionState(create = false)
	private UserSession userSession;

	@Property
	private Long eventId;

	@Property
	private boolean showZone = false;

	@Property
	private Long typeOptionId;

	@Property
	private Long betAmount;

	@Inject
	BetService betService;

	@Component
	private Form placeBetForm;

	@Component(id = "betAmount")
	private TextField betAmountTextField;

	@Inject
	private Request request;

	@InjectComponent
	private Zone betPlacedZone;

	@InjectComponent
	private Zone formZone;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	Object onPassivate() {
		return new Object[] { eventId, typeOptionId };
	}

	void onActivate(Long eventId, Long typeOptionId) {
		this.eventId = eventId;
		this.typeOptionId = typeOptionId;
	}

	void onValidateFromPlaceBetForm() {
		if (!placeBetForm.isValid()) {
			return;
		}

		try {
			betService.createBet(userSession.getUserProfileId(), typeOptionId, betAmount);
		} catch (InstanceNotFoundException e) {
		}
	}

	void onSuccess() {
		if (request.isXHR()) {
			showZone = true;
			ajaxResponseRenderer.addRender(formZone).addRender(betPlacedZone);
		}
	}

	void onFailure() {
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(formZone);
		}
	}

}