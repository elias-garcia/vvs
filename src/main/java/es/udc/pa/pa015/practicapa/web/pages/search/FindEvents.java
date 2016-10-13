package es.udc.pa.pa015.practicapa.web.pages.search;

import java.util.List;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.eventService.EventService;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicyType;

@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class FindEvents {
	
	@Property
	private String keywords;
	
	@Property
	private Long categoryId;
	
	@Property
	private String categories;
	
	@Component
	private Form findEventsForm;
	
	@Inject
	private EventService eventService;
	
	@InjectPage
	private Events events;

	void onPrepareForRender() {
		List<CategoryInfo> tempCategories = eventService.findAllCategories();
		
		categories = "-1=--- TODAS LAS CATEGORÍAS ---";
		
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
	
	void onValidateFromFindEventsForm(){
		if (!findEventsForm.isValid()) {
			return;
		}
	}
	
	Object onSuccess() {
		events.setKeywords(keywords);
		events.setCategoryId(categoryId);		
		return events;
	}
}
