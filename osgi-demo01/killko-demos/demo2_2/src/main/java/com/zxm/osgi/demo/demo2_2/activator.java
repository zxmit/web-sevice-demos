package com.zxm.osgi.demo.demo2_2;

import com.zxm.osgi.demo.demo2.contract.ICalculation;
import java.util.Dictionary;
import java.util.Hashtable;

import com.zxm.osgi.demo.demo2_2.Impl.Calculation;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 *
 * @author han
 */
public class activator implements BundleActivator {

    @Override
    public void start(BundleContext context) throws Exception {
        Dictionary<String, String> props = new Hashtable<String, String>();
        props.put("ServiceName", "Calculation");
        context.registerService(ICalculation.class.getName(), new Calculation(), props);
        System.out.println("Service registered!");
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        System.out.println("Stop demo2_2 bundle!");
    }
}
