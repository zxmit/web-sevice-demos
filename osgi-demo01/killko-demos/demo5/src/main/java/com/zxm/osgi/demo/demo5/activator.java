/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zxm.osgi.demo.demo5;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 *
 * @author han
 */
public class activator implements BundleActivator{
   DemoServiceTracker tracker;

    @Override
    public void start(BundleContext context) throws Exception {
        tracker=new DemoServiceTracker(context);
        tracker.open();
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        if(tracker != null){
            tracker.close();
        }
    }

}
