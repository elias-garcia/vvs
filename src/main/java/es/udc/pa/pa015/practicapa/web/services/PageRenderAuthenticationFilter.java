package es.udc.pa.pa015.practicapa.web.services;

import org.apache.tapestry5.internal.EmptyEventContext;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.MetaDataLocator;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.PageRenderRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;

import java.io.IOException;

/**
 * Page Render Authentication Filter.
 */
public class PageRenderAuthenticationFilter implements PageRenderRequestFilter {

  /** application State Manager. */
  private ApplicationStateManager applicationStateManager;

  /** component Source. */
  private ComponentSource componentSource;

  /** Meta Data Locator. */
  private MetaDataLocator locator;

  /**
   * Constructor of pageRenderAuthenticationFilter.
   * @param applicationStateManagerParam
   *          ApplicationStateManager
   * @param componentSourceParam
   *          ComponentSource
   * @param locatorParam
   *          MetaDataLocator
   */
  public PageRenderAuthenticationFilter(
      final ApplicationStateManager applicationStateManagerParam,
      final ComponentSource componentSourceParam,
      final MetaDataLocator locatorParam) {

    this.applicationStateManager = applicationStateManagerParam;
    this.componentSource = componentSourceParam;
    this.locator = locatorParam;

  }

  /**
   * This method handle.
   * @param parameters parameters
   * @param handler PageRenderRequestHandler
   * @throws IOException ioException
   */
  public final void handle(final PageRenderRequestParameters parameters,
      final PageRenderRequestHandler handler) throws IOException {

    PageRenderRequestParameters handlerParameters = parameters;
    String redirectPage = AuthenticationValidator.checkForPage(parameters
        .getLogicalPageName(), applicationStateManager, componentSource,
        locator);
    if (redirectPage != null) {
      handlerParameters = new PageRenderRequestParameters(redirectPage,
          new EmptyEventContext(), false);
    }

    handler.handle(handlerParameters);

  }

}
