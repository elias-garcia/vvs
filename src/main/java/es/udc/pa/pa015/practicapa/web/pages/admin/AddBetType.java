package es.udc.pa.pa015.practicapa.web.pages.admin;

import es.udc.pa.pa015.practicapa.model.bettype.BetType;
import es.udc.pa.pa015.practicapa.model.
                eventService.DuplicatedResultTypeOptionsException;
import es.udc.pa.pa015.practicapa.model.eventService.EventService;
import es.udc.pa.pa015.practicapa.model.
                eventService.NoAssignedTypeOptionsException;
import es.udc.pa.pa015.practicapa.model.typeoption.TypeOption;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Class for addByeType page.
 */
@AuthenticationPolicy(AuthenticationPolicyType.ADMIN_USER)
public class AddBetType {

  /** event id. */
  @Property
  private Long eventId;

  /**List of options. */
  @Persist
  @Property
  private ArrayList<TypeOption> options;

  /** TypeOption. */
  @Property
  private TypeOption option;

  /** Indicates if it is a show zone. */
  @Property
  private boolean showZone = false;

  /** betTypeQuestion. */
  @Property
  private String betTypeQuestion;

  /** multipleType. */
  @Property
  private String multipleType;

  /** bet odd. */
  @Property
  private Double odd;

  /** yesNoSelectModel. */
  @Property
  private String yesNoSelectModel;

  /** typeOptionNames. */
  @Property
  private String typeOptionNames;

  /** typeOptionOdds. */
  @Property
  private String typeOptionOdds;
  /*
   * @Component(id="typeOptionNames") private TextArea typeOptionNamesTextArea;
   * @Component(id="typeOptionOdds") private TextArea typeOptionOddsTextArea;
   */
  /** addBetTypeForm. */
  @Component
  private Form addBetTypeForm;

  /** zone injected. */
  @InjectComponent
  private Zone zone;

  /** Locale. */
  @Inject
  private Locale locale;

  /** Messages. */
  @Inject
  private Messages messages;

  /** Request. */
  @Inject
  private Request request;

  /** BetTypeAddedZone. */
  @InjectComponent
  private Zone betTypeAddedZone;

  /** FromZone. */
  @InjectComponent
  private Zone formZone;

  /** AjazResponseRenderer. */
  @Inject
  private AjaxResponseRenderer ajaxResponseRenderer;

  /** Even service. */
  @Inject
  private EventService eventService;

  /**
   * on prepare for render.
   */
  final void onPrepareForRender() {
    this.yesNoSelectModel = ("false" + "=" + messages.get("select-no") + ","
        + "true" + "=" + messages.get("select-yes"));
  }

  /**
   * method to validate the add bet type form.
   * @throws NoAssignedTypeOptionsException
   *          Thrown out when there's not typeOptions assigned
   * @throws DuplicatedResultTypeOptionsException
   *          Thrown out when the results are duplicated
   */
  final void onValidateFromAddBetTypeForm()
      throws NoAssignedTypeOptionsException,
      DuplicatedResultTypeOptionsException {
    if (!addBetTypeForm.isValid()) {
      return;
    }

    try {
      BetType newBetType = new BetType(betTypeQuestion, Boolean.parseBoolean(
          multipleType), eventService.findEvent(eventId));
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

  /**
   * Method when the result is success.
   */
  final void onSuccess() {
    if (request.isXHR()) {
      showZone = true;
      ajaxResponseRenderer.addRender(formZone).addRender(betTypeAddedZone);
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
   * Method when it refresh.
   * @return the page
   */
  final Object onRefresh() {
    options = new ArrayList<TypeOption>();
    return request.isXHR() ? formZone.getBody() : null;
  }

  /**
   * Get unique zone id.
   * @return unique zone id
   */
  public final String getUniqueZoneId() {
    return "zone_" + options.indexOf(option);
  }

  /**
   * Get id.
   * @return id
   */
  public final int getId() {
    return options.indexOf(option);
  }

  /**
   * onActivate.
   * @param eventIdParam
   *        event id passed
   */
  final void onActivate(final Long eventIdParam) {
    this.eventId = eventIdParam;
    if (options == null) {
      options = new ArrayList<TypeOption>();
    }
  }

  /**
   * onPassivate.
   * @return eventId
   */
  final Long onPassivate() {
    options = new ArrayList<TypeOption>();
    return eventId;
  }

  /**
   * onAddRow.
   * @return typeOption
   */
  final Object onAddRow() {
    TypeOption newOpt = new TypeOption();
    options.add(newOpt);
    return newOpt;
  }

  /**
   * onRemoveRow.
   * @param newOpt
   *        TypeOption
   */
  final void onRemoveRow(final TypeOption newOpt) {
    options.set(options.indexOf(newOpt), null);
  }

  /**
   * onZoneUpdate.
   * @param index int
   * @return bode of zone
   */
  final Object onZoneUpdate(final int index) {
    option = options.get(index);
    return zone.getBody();
  }

  /**
   * This method get the encoder.
   * @return ValueEncoder
   */
  public final ValueEncoder<TypeOption> getEncoder() {
    return new ValueEncoder<TypeOption>() {

      public String toClient(final TypeOption optionParam) {
        return String.valueOf(options.indexOf(optionParam));
      }

      public TypeOption toValue(final String clientValue) {
        return options.get(Integer.parseInt(clientValue));
      }

    };
  }
}
