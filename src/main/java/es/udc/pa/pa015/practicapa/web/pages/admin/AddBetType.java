package es.udc.pa.pa015.practicapa.web.pages.admin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;

import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.eventService.EventService;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

@AuthenticationPolicy(AuthenticationPolicyType.ADMIN_USER)
public class AddBetType {
	@Property
	private Long eventId;
	@Persist
	@Property
	private ArrayList<TypeOption> options;
	@Property
	private TypeOption option;
	@Property
	private boolean showZone = false;

	@Property
	private String betTypeQuestion;

	@Property
	private String multipleType;

	@Property
	private Double odd;
	@Property
	private String yesNoSelectModel;

	@Property
	private String typeOptionNames;

	@Property
	private String typeOptionOdds;
	/*
	 * @Component(id="typeOptionNames") private TextArea
	 * typeOptionNamesTextArea;
	 * 
	 * @Component(id="typeOptionOdds") private TextArea typeOptionOddsTextArea;
	 */
	@Component
	private Form addBetTypeForm;

	@InjectComponent
	private Zone zone;

	@Inject
	private Locale locale;

	@Inject
	private Messages messages;

	@Inject
	private Request request;

	@InjectComponent
	private Zone betTypeAddedZone;

	@InjectComponent
	private Zone formZone;

	@Inject
	private AjaxResponseRenderer ajaxResponseRenderer;

	@Inject
	private EventService eventService;

	void onPrepareForRender() {
		this.yesNoSelectModel = ("false" + "=" + messages.get("select-no") + "," + "true" + "="
				+ messages.get("select-yes"));
	}

	void onValidateFromAddBetTypeForm() {
		if (!addBetTypeForm.isValid()) {
			return;
		}

		try {
			BetType newBetType = new BetType(betTypeQuestion, Boolean.parseBoolean(multipleType),
					eventService.findEvent(eventId));
			List<TypeOption> newOptions = new ArrayList<TypeOption>();
			for (TypeOption opt : options) {
				if (opt != null) {
					opt.setOdd(opt.getOdd() / 100);
					opt.setOdd(Double.valueOf(opt.getOdd()));
					newOptions.add(opt);
				}
			}

			eventService.addBetType(eventId, newBetType, newOptions);

		} catch (InstanceNotFoundException ex) {
		}
	}

	void onSuccess() {
		if (request.isXHR()) {
			showZone = true;
			ajaxResponseRenderer.addRender(formZone).addRender(betTypeAddedZone);
		}
	}

	void onFailure() {
		if (request.isXHR()) {
			ajaxResponseRenderer.addRender(formZone);
		}
	}

	Object onRefresh() {
		options = new ArrayList<TypeOption>();
		return request.isXHR() ? formZone.getBody() : null;
	}

	public String getUniqueZoneId() {
		return "zone_" + options.indexOf(option);
	}

	public int getId() {
		return options.indexOf(option);
	}

	void onActivate(Long eventId) {
		this.eventId = eventId;
		if (options == null) {
			options = new ArrayList<TypeOption>();
		}
	}

	Long onPassivate() {
		options = new ArrayList<TypeOption>();
		return eventId;
	}

	Object onAddRow() {
		TypeOption newOpt = new TypeOption();
		options.add(newOpt);
		return newOpt;
	}

	void onRemoveRow(TypeOption newOpt) {
		options.set(options.indexOf(newOpt), null);
	}

	Object onZoneUpdate(int index) {
		option = options.get(index);
		return zone.getBody();
	}

	public ValueEncoder<TypeOption> getEncoder() {
		return new ValueEncoder<TypeOption>() {

			public String toClient(TypeOption option) {
				return String.valueOf(options.indexOf(option));
			}

			public TypeOption toValue(String clientValue) {
				return options.get(Integer.parseInt(clientValue));
			}

		};
	}
}