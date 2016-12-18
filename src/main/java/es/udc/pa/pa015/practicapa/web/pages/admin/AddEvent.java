package es.udc.pa.pa015.practicapa.web.pages.admin;

import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.eventService.EventDateException;
import es.udc.pa.pa015.practicapa.model.eventService.EventService;
import es.udc.pa.pa015.practicapa.model.eventService.NullEventNameException;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.corelib.components.Zone;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ajax.AjaxResponseRenderer;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Class for add event page.
 */
@AuthenticationPolicy(AuthenticationPolicyType.ADMIN_USER)
public class AddEvent {

  /** Event name. */
  @Property
  private String eventName;

  /** Indicates if its is a show zone. */
  @Property
  private boolean showZone = false;

  /** Start date. */
  @Property
  private String startDate;

  /** Start time. */
  @Property
  private String startTime;

  /** Category id. */
  @Property
  private Long categoryId;

  /** Categories. */
  @Property
  private String categories;

  /** Add event form. */
  @Component
  private Form addEventForm;

  /** startDateTextField. */
  @Component(id = "startDate")
  private TextField startDateTextField;

  /** startTimeTextField. */
  @Component(id = "startTime")
  private TextField startTimeTextField;

  /** categoryIdSelect. */
  @Component(id = "categoryId")
  private Select categoryIdSelect;

  /** Messages. */
  @Inject
  private Messages messages;

  /** Request. */
  @Inject
  private Request request;

  /** eventAddedZone. */
  @InjectComponent
  private Zone eventAddedZone;

  /** formZone. */
  @InjectComponent
  private Zone formZone;

  /** ajaxResponseRenderer. */
  @Inject
  private AjaxResponseRenderer ajaxResponseRenderer;

  /** Locale. */
  @Inject
  private Locale locale;

  /** eventService. */
  @Inject
  private EventService eventService;

  /** javaScripSupport. */
  @Inject
  private JavaScriptSupport javaScriptSupport;

  /**
   * After renderer.
   */
  public final void afterRender() {
    javaScriptSupport.require("reset-form");
  }

  /**
   * onPrepareForRender.
   */
  final void onPrepareForRender() {
    List<CategoryInfo> tempCategories = eventService.findAllCategories();

    for (CategoryInfo temp : tempCategories) {
      if (categories == null) {
        categories = (temp.getCategoryId().toString() + "=" + temp
            .getCategoryName());
      } else {
        categories += (", " + temp.getCategoryId().toString() + "=" + temp
            .getCategoryName());
      }
    }
  }

  /**
   * Method to validate the add event form.
   * @throws NullEventNameException
   *                      thrown out when the even name is null
   */
  final void onValidateFromAddEventForm() throws NullEventNameException {
    if (!addEventForm.isValid()) {
      return;
    }

    try {
      // Parse startDate to Calendar
      Calendar date = Calendar.getInstance();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm", locale);
      date.setTime(sdf.parse(startDate + startTime));

      eventService.createEvent(eventName, date, categoryId);
    } catch (EventDateException ex) {
      addEventForm.recordError(startDateTextField, messages.format(
          "error-dateHasPassed", startDate));
      addEventForm.recordError(startTimeTextField, messages.format(
          "error-timeHasPassed", startTime));
    } catch (InstanceNotFoundException ex) {
      addEventForm.recordError(categoryIdSelect, messages.format(
          "error-categoryDoesNotExist", categoryId));
    } catch (ParseException ex) {
      addEventForm.recordError(startDateTextField, messages.format(
          "error-badParse", startDate + " " + startTime));
    }
  }

  /**
   * Method when the result is success.
   * @throws EventDateException
   *            Thrown out when the date isn't correct
   * @throws InstanceNotFoundException
   *            Thrown out when the event doesn't exist
   */
  final void onSuccess() throws EventDateException, InstanceNotFoundException {
    if (request.isXHR()) {
      showZone = true;
      ajaxResponseRenderer.addRender(formZone).addRender(eventAddedZone);
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
