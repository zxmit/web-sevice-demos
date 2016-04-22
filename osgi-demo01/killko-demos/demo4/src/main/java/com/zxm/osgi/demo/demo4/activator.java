/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zxm.osgi.demo.demo4;

import com.zxm.osgi.demo.demo2.contract.ICalculation;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 *
 * @author han
 */
public class activator implements BundleActivator{

    @Override
    public void start(BundleContext context) throws Exception {
        synchronized (this){
            context.addServiceListener(new DemoServiceListener(context),"(&(objectClass=" + ICalculation.class.getName() + ")(ServiceName=Calculation))");
        }
    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }

}
