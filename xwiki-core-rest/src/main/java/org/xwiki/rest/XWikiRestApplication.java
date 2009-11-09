package org.xwiki.rest;

import java.util.Map;
import java.util.logging.Level;

import org.restlet.Context;
import org.restlet.Restlet;
import org.restlet.Router;
import org.restlet.ext.wadl.WadlApplication;
import org.xwiki.component.manager.ComponentLifecycleException;
import org.xwiki.component.manager.ComponentManager;
import org.xwiki.component.phase.Composable;
import org.xwiki.rest.resources.BrowserAuthenticationResource;

/**
 * The rest application, this is the entry-point. This application is able to discover resource components
 * 
 * @version $Id$
 */
public class XWikiRestApplication extends WadlApplication implements Composable
{
    /* Injected by the component manager. */
    private ComponentManager componentManager;

    /* Injected by the component manager. This map contains all the components that have XWikiResource role. */
    private Map<String, XWikiResource> classNameToResourceMap;

    public XWikiRestApplication()
    {
        super();
    }

    /**
     * Constructor.
     * 
     * @param parentContext The parent context.
     */
    public XWikiRestApplication(Context parentContext)
    {
        super(parentContext);
    }

    /**
     * Creates a router and attaches the url patterns.
     * 
     * @return router The newly created router.
     */
    @Override
    public Restlet createRoot()
    {
        getTunnelService().setEnabled(true);
        getTunnelService().setExtensionsTunnel(true);

        Router router = new XWikiRouter(componentManager, getContext());

        /* Attach an empty resource in order to allow plain browser to introduce authentication credentials. */
        router.attach(BrowserAuthenticationResource.URI_PATTERN, BrowserAuthenticationResource.class);

        /* Register all the resource components */
        for (String className : classNameToResourceMap.keySet()) {
            XWikiResource resource = classNameToResourceMap.get(className);
            String uriPattern = resource.getUriPattern();

            if (uriPattern != null) {
                getLogger().log(
                    Level.INFO,
                    String.format("Attaching resource %s to URI pattern '%s'", resource.getClass().getName(),
                        uriPattern));

                router.attach(uriPattern, resource.getClass());
            } else {
                getLogger().log(
                    Level.WARNING,
                    String.format("Resource %s is not cofigured with any URI pattern to be attached to.", resource
                        .getClass().getName(), resource));
            }

            /*
             * Release the previously looked up resource because we just needed it to retrieve the uriPatterns and the
             * resource Class
             */
            try {
                componentManager.release(resource);
            } catch (ComponentLifecycleException e) {
                e.printStackTrace();
            }
        }

        /*
         * Add a filter before the main router for setting and cleaning up the XWiki context. The contract is that if a
         * request reaches one of the Restlet components, then the Restlet context attributes will contains properly
         * initialized XWiki platform objects.
         */
        XWikiInitializationAndCleanupFilter initializationAndCleanupFilter =
            new XWikiInitializationAndCleanupFilter(componentManager, getContext());
        XWikiAuthentication guard = new XWikiAuthentication(getContext());
        initializationAndCleanupFilter.setNext(guard);
        guard.setNext(router);

        /* The status service will handle cleanup when a resource raises an exception. */
        setStatusService(new XWikiStatusService(this));

        return initializationAndCleanupFilter;
    }

    public void compose(ComponentManager componentManager)
    {
        this.componentManager = componentManager;
    }

    public ComponentManager getComponentManager()
    {
        return componentManager;
    }

}
