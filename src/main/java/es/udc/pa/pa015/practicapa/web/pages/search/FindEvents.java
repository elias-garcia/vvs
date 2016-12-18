package es.udc.pa.pa015.practicapa.web.pages.search;

import es.udc.pa.pa015.practicapa.model.categoryinfo.CategoryInfo;
import es.udc.pa.pa015.practicapa.model.eventService.EventService;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicyType;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;

import java.util.List;

/**
 * Class of the findEvents page.
 */
@AuthenticationPolicy(AuthenticationPolicyType.ALL_USERS)
public class FindEvents {

  /** keyWords. */
  @Property
  private String keywords;

  /** category id. */
  @Property
  private Long categoryId;

  /** categories. */
  @Property
  private String categories;

  /** findEventsForm. */
  @Component
  private Form findEventsForm;

  /** eventService. */
  @Inject
  private EventService eventService;

  /** events. */
  @InjectPage
  private Events events;

  /**
   * on Prepare for Render.
   */
  final void onPrepareForRender() {
    List<CategoryInfo> tempCategories = eventService.findAllCategories();

    categories = "-1=--- TODAS LAS CATEGORÍAS ---";

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
   * Method for validate findEventsForm.
   */
  final void onValidateFromFindEventsForm() {
    if (!findEventsForm.isValid()) {
      return;
    }
  }

  /**
   * Method when the result is success.
   * @return events
   */
  final Object onSuccess() {
    events.setKeywords(keywords);
    events.setCategoryId(categoryId);
    return events;
  }
}
