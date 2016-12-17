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

@AuthenticationPolicy(AuthenticationPolicyType.ADMIN_USER)
public class AddEvent {

  @Property
  private String eventName;

  @Property
  private boolean showZone = false;

  @Property
  private String startDate;

  @Property
  private String startTime;

  @Property
  private Long categoryId;

  @Property
  private String categories;

  @Component
  private Form addEventForm;

  @Component(id = "startDate")
  private TextField startDateTextField;

  @Component(id = "startTime")
  private TextField startTimeTextField;

  @Component(id = "categoryId")
  private Select categoryIdSelect;

  @Inject
  private Messages messages;

  @Inject
  private Request request;

  @InjectComponent
  private Zone eventAddedZone;

  @InjectComponent
  private Zone formZone;

  @Inject
  private AjaxResponseRenderer ajaxResponseRenderer;

  @Inject
  private Locale locale;

  @Inject
  private EventService eventService;

  @Inject
  private JavaScriptSupport javaScriptSupport;

  public void afterRender() {
    javaScriptSupport.require("reset-form");
  }

  void onPrepareForRender() {
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

  void onValidateFromAddEventForm() throws NullEventNameException {
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

  void onSuccess() throws EventDateException, InstanceNotFoundException {
    if (request.isXHR()) {
      showZone = true;
      ajaxResponseRenderer.addRender(formZone).addRender(eventAddedZone);
    }
  }

  void onFailure() {
    if (request.isXHR()) {
      ajaxResponseRenderer.addRender(formZone);
    }
  }

}
