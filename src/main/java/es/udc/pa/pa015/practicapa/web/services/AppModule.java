package es.udc.pa.pa015.practicapa.web.services;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.services.ComponentEventRequestFilter;
import org.apache.tapestry5.services.PageRenderRequestFilter;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;

/**
 * This module is automatically included as part of the Tapestry IoC Registry,
 * it's a good place to configure and extend Tapestry, or to place your own
 * service definitions.
 */
public class AppModule {

  /**
   * This method bind filters.
   * @param binder
   *          service binder
   */
  public static final void bind(final ServiceBinder binder) {

    /* Bind filters. */
    binder.bind(SessionFilter.class);
    binder.bind(PageRenderAuthenticationFilter.class);
    binder.bind(ComponentEventAuthenticationFilter.class);

  }

  /**
   * This method contribute application defaults.
   * @param configuration
   *          the mapped configuration
   */
  public static final void contributeApplicationDefaults(
      final MappedConfiguration<String, Object> configuration) {

    configuration.add(SymbolConstants.SUPPORTED_LOCALES, "en,es,gl");
    configuration.add(SymbolConstants.JAVASCRIPT_INFRASTRUCTURE_PROVIDER,
        "jquery");

  }

  /**
   * Contribute our {@link ComponentClassTransformWorker2} to transformation
   * pipeline to add our code to loaded classes.
   * @param configuration
   *          component class transformer configuration
   */
  public static final void contributeComponentClassTransformWorker(
    final OrderedConfiguration<ComponentClassTransformWorker2> configuration) {

    configuration.add("AuthenticationPolicy", new AuthenticationPolicyWorker());

  }

  /**
   * This method contribute request handler.
   * @param configuration
   *          ordered configuration
   * @param sessionFilter
   *          session filter
   */
  public static final void contributeRequestHandler(
      final OrderedConfiguration<RequestFilter> configuration,
      final SessionFilter sessionFilter) {

    /* Add filters to the RequestHandler service. */
    configuration.add("SessionFilter", sessionFilter, "after:*");

  }

  /**
   * Contributes "PageRenderAuthenticationFilter" filter which checks for access
   * rights of requests.
   * @param configuration
   *          ordered configuration
   * @param pageRenderAuthenticationFilter
   *          PageRenderRequestFilter
   */
  public final void contributePageRenderRequestHandler(
      final OrderedConfiguration<PageRenderRequestFilter> configuration,
      final PageRenderRequestFilter pageRenderAuthenticationFilter) {

    /*
     * Add filters to the filters pipeline of the PageRender command of the
     * MasterDispatcher service.
     */
    configuration.add("PageRenderAuthenticationFilter",
        pageRenderAuthenticationFilter, "before:*");

  }

  /**
   * Contribute "PageRenderAuthenticationFilter" filter to determine if the
   * event can be processed and the user has enough rights to do so.
   * @param configuration
   *          ordered configuration
   * @param componentEventAuthenticationFilter
   *          ComponentEventRequestFilter
   */
  public final void contributeComponentEventRequestHandler(
      final OrderedConfiguration<ComponentEventRequestFilter> configuration,
      final ComponentEventRequestFilter componentEventAuthenticationFilter) {

    /*
     * Add filters to the filters pipeline of the ComponentEvent command of the
     * MasterDispatcher service.
     */
    configuration.add("ComponentEventAuthenticationFilter",
        componentEventAuthenticationFilter, "before:*");

  }

}
