package es.udc.pa.pa015.practicapa.web.components;

import es.udc.pa.pa015.practicapa.web.pages.Index;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicy;
import es.udc.pa.pa015.practicapa.web.services.AuthenticationPolicyType;
import es.udc.pa.pa015.practicapa.web.util.CookiesManager;
import es.udc.pa.pa015.practicapa.web.util.UserSession;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SessionState;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Cookies;

/**
 * Layout class.
 */
@Import(library = { "tapestry5/bootstrap/js/collapse.js",
    "tapestry5/bootstrap/js/dropdown.js" },
    stylesheet = "tapestry5/bootstrap/css/bootstrap-theme.css")
public class Layout {

  /** Title. */
  @Property
  @Parameter(required = true, defaultPrefix = "message")
  private String title;

  /** showTitleInBody. */
  @Parameter(defaultPrefix = "literal")
  private Boolean showTitleInBody;

  /** userSession. */
  @Property
  @SessionState(create = false)
  private UserSession userSession;

  /** cookies. */
  @Inject
  private Cookies cookies;

  /**
   * This method gets if the show title is in body.
   * @return boolean
   */
  public final boolean getShowTitleInBody() {
    if (showTitleInBody == null) {
      return true;
    } else {
      return showTitleInBody;
    }
  }

  /**
   * This method is for the action of logout.
   * @return Index class
   */
  @AuthenticationPolicy(AuthenticationPolicyType.AUTHENTICATED_USERS)
  final Object onActionFromLogout() {
    userSession = null;
    CookiesManager.removeCookies(cookies);
    return Index.class;
  }

}
