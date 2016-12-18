package es.udc.pa.pa015.practicapa.web.services;

import org.apache.tapestry5.internal.EmptyEventContext;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.ComponentEventRequestHandler;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.MetaDataLocator;
import org.apache.tapestry5.services.PageRenderRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;

import java.io.IOException;

/**
 * Component Event Authentication Filter.
 */
public class ComponentEventAuthenticationFilter implements
    ComponentEventRequestFilter {

  /** application State Manager. */
  private ApplicationStateManager applicationStateManager;

  /** component Source. */
  private ComponentSource componentSource;

  /** MetaDataLocator. */
  private MetaDataLocator locator;

  /** page Render Request Handler. */
  private PageRenderRequestHandler pageRenderRequestHandler;

  /**
   * Constructor of componentEventAuthenticationFilter.
   * @param applicationStateManagerParam
   *          AplicationStateManager
   * @param componentSourceParam
   *          ComponentSource
   * @param locatorParam
   *          MetaDataLocator
   * @param pageRenderRequestHandlerParam
   *          PageRenderRequestHandler
   */
  public ComponentEventAuthenticationFilter(
      final ApplicationStateManager applicationStateManagerParam,
      final ComponentSource componentSourceParam,
      final MetaDataLocator locatorParam,
      final PageRenderRequestHandler pageRenderRequestHandlerParam) {

    this.applicationStateManager = applicationStateManagerParam;
    this.componentSource = componentSourceParam;
    this.locator = locatorParam;
    this.pageRenderRequestHandler = pageRenderRequestHandlerParam;

  }

  /**
   * This method handle.
   * @param parameters parameters
   * @param handler ComponentEventRequestHandler
   * @throws IOException ioException
   */
  public final void handle(final ComponentEventRequestParameters parameters,
      final ComponentEventRequestHandler handler) throws IOException {

    ComponentEventRequestParameters handlerParameters = parameters;
    String redirectPage = AuthenticationValidator.checkForPage(parameters
        .getActivePageName(), applicationStateManager, componentSource,
        locator);
    if (redirectPage == null) {
      String componentId = parameters.getNestedComponentId();
      if (componentId != null) {
        String mainComponentId = null;
        String eventId = null;
        if (componentId.indexOf(".") != -1) {
          mainComponentId = componentId.substring(0, componentId.lastIndexOf(
              "."));
          eventId = componentId.substring(componentId.lastIndexOf(".") + 1);
        } else {
          eventId = componentId;
        }

        redirectPage = AuthenticationValidator.checkForComponentEvent(parameters
            .getActivePageName(), mainComponentId, eventId, parameters
                .getEventType(), applicationStateManager, componentSource,
            locator);

      }
    }

    if (redirectPage != null) {
      pageRenderRequestHandler.handle(new PageRenderRequestParameters(
          redirectPage, new EmptyEventContext(), false));
    } else {
      handler.handle(handlerParameters);
    }
  }
}
