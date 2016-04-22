package com.zxm.osgi.demo.demo2_1;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 *
 * @author han
 */
public class activator implements BundleActivator {

    @Override
    public void start(BundleContext context) throws Exception {
        System.out.println("Bundle demo2_1 started!");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("Stop demo2_1 bundle!");
    }
}
