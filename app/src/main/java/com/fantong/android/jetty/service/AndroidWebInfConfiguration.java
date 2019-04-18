package com.fantong.android.jetty.service;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;

import java.util.Iterator;
import java.util.List;

public class AndroidWebInfConfiguration extends WebInfConfiguration {
    private static final long serialVersionUID = 8235322314977241413L;

    public AndroidWebInfConfiguration() {
    }

    public void preConfigure(WebAppContext context) throws Exception {
        context.setClassLoader(AndroidWebInfConfiguration.class.getClassLoader());
        super.preConfigure(context);
        Log.debug("Setting classloader parent=" + this.getClass().getClassLoader() + " for context: " + context);
    }

    public void configure(WebAppContext context) throws Exception {
        if (context.isStarted()) {
            if (Log.isDebugEnabled()) {
                Log.debug("Cannot configure webapp " + context + " after it is started");
            }

        } else {
            List<Resource> resources = (List)context.getAttribute("org.eclipse.jetty.resources");
            if (resources != null) {
                Resource[] collection = new Resource[resources.size() + 1];
                int i = 0;
                int var10 = i + 1;
                collection[i] = context.getBaseResource();

                Resource resource;
                for(Iterator var8 = resources.iterator(); var8.hasNext(); collection[var10++] = resource) {
                    resource = (Resource)var8.next();
                }

                context.setBaseResource(new ResourceCollection(collection));
            }

        }
    }
}
