package com.ponder.Demo.demo1;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 *
 * @author han
 */
public class activator implements BundleActivator{

    @Override
    public void start(BundleContext context) throws Exception {
       System.out.println("Hello world!");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
       System.out.println("Stop bundle!");
    }

}
