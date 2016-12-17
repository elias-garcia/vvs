package es.udc.pa.pa015.practicapa.web.services;

import org.apache.tapestry5.internal.EmptyEventContext;
import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.MetaDataLocator;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.PageRenderRequestHandler;
import org.apache.tapestry5.services.PageRenderRequestParameters;

import java.io.IOException;

public class PageRenderAuthenticationFilter implements PageRenderRequestFilter {

  private ApplicationStateManager applicationStateManager;
  private ComponentSource componentSource;
  private MetaDataLocator locator;

  /**
   * Constructor of pageRenderAuthenticationFilter.
   * @param applicationStateManager
   *          ApplicationStateManager
   * @param componentSource
   *          ComponentSource
   * @param locator
   *          MetaDataLocator
   */
  public PageRenderAuthenticationFilter(
      ApplicationStateManager applicationStateManager,
      ComponentSource componentSource, MetaDataLocator locator) {

    this.applicationStateManager = applicationStateManager;
    this.componentSource = componentSource;
    this.locator = locator;

  }

  /**
   * This method handle.
   */
  public void handle(PageRenderRequestParameters parameters,
      PageRenderRequestHandler handler) throws IOException {

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
