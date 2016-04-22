/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zxm.osgi.demo.demo4;

import com.zxm.osgi.demo.demo2.contract.ICalculation;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;

/**
 *
 * @author han
 */
public class DemoServiceListener implements ServiceListener {

    private BundleContext context;

    public DemoServiceListener(BundleContext cntxt) {
        context = cntxt;
    }

    @Override
    public void serviceChanged(ServiceEvent event) {

        if (event.getType() == ServiceEvent.REGISTERED) {
            ServiceReference ref = event.getServiceReference();
            if (ref != null) {
                ICalculation service = (ICalculation) context.getService(ref);
                if (service != null) {
                    System.out.println();
                    System.out.println("4+1=" + service.add(4, 1));
                    System.out.println("4-1=" + service.sub(4, 1));
                    System.out.println("4*3=" + service.mul(4, 3));
                }
            }
        } else if (event.getType() == ServiceEvent.UNREGISTERING) {
           System.out.println("["+ICalculation.class.getName()+"]服务注销");
        }
    }
}
