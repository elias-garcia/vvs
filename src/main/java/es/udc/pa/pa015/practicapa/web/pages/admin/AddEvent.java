package es.udc.pa.pa015.practicapa.web.pages.admin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa015.practicapa.model.eventService.EventService;
import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.eventService.EventDateException;
import es.udc.pa.pa015.practicapa.model.eventinfo.EventInfo;
import es.udc.pa.pa015.practicapa.web.util.UserSession;
import es.udc.pojo.modelutil.exceptions.InstanceNotFoundException;

public class AddEvent {
	
	@Property
    @SessionState(create=false)
    private UserSession userSession;
	
	@Property
	private String eventName;
	
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
	
	@Component(id="startDate")
	private TextField startDateTextField; 
	
	@Component(id="startTime")
	private TextField startTimeTextField; 
	
	@Component(id="categoryId")
	private Select categoryIdSelect; 
	
	@Inject
	private Messages messages; 
	
	@Inject
	private Locale locale; 
	
	@Inject
	private EventService eventService;
	
	@InjectPage
	private EventAdded eventAdded;
	
	void onPrepareForRender() {
		List<CategoryInfo> tempCategories = eventService.findAllCategories();
		
		for (CategoryInfo temp : tempCategories) {
			if (categories == null) {
				categories = (temp.getCategoryId().toString() +
						"=" + temp.getCategoryName());
			} else {
				categories += (", " + temp.getCategoryId().toString()
						+ "=" + temp.getCategoryName());
			}
		}
	}
	
	void onValidateFromAddEventForm() {
		if (!addEventForm.isValid()) {
			return;
		}
		
		try {
			// Parse startDate to Calendar
			Calendar date = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm", locale);
			date.setTime(sdf.parse(startDate + startTime));
			
			EventInfo createdEvent = eventService.createEvent(eventName, date, categoryId);
			eventAdded.setEventId(createdEvent.getEventId());
		} catch (EventDateException ex) {
			addEventForm.recordError(startDateTextField,
					messages.format("error-dateHasPassed", startDate));
			addEventForm.recordError(startDateTextField,
					messages.format("error-timeHasPassed", startTime));
		} catch (InstanceNotFoundException ex) {
			addEventForm.recordError(categoryIdSelect,
					messages.format("error-categoryDoesNotExist", categoryId));
		} catch (ParseException e) {
			addEventForm.recordError(startDateTextField,
					messages.format("error-badParse", startDate + " " + startTime));
		}		
	}
	
	Object onSuccess() throws EventDateException, InstanceNotFoundException {
		return eventAdded;
	}

}
