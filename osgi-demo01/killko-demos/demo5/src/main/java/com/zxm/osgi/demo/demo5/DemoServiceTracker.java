/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zxm.osgi.demo.demo5;

import com.zxm.osgi.demo.demo2.contract.ICalculation;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTracker;

/**
 *
 * @author han
 */
public class DemoServiceTracker extends ServiceTracker {

    private BundleContext context;

    public DemoServiceTracker(BundleContext ctx) {
        super(ctx, ICalculation.class.getName(), null);
        context=ctx;
    }

    @Override
    public Object addingService(ServiceReference reference) {
        if(reference!=null){
             ICalculation service = (ICalculation) context.getService(reference);
                if (service != null) {
                    System.out.println();
                    System.out.println("5+1=" + service.add(5, 1));
                    System.out.println("5-1=" + service.sub(5, 1));
                    System.out.println("5*3=" + service.mul(5, 3));
                }
        }
        return super.addingService(reference);
    }

    @Override
    public void removedService(ServiceReference reference, Object service) {
        System.out.println("["+ICalculation.class.getName()+"]服务注销"); 
        super.removedService(reference, service);
    }
}
