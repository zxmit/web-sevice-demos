/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zxm.osgi.demo.demo3;

import com.zxm.osgi.demo.demo2.contract.ICalculation;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

/**
 *
 * @author han
 */
public class activator implements BundleActivator{

    @Override
    public void start(BundleContext context) throws Exception {
         ServiceReference[] refs = context.getServiceReferences(
            ICalculation.class.getName(), "(ServiceName=Calculation)");
         if(refs!=null && refs.length>0){
             ICalculation service=(ICalculation)context.getService(refs[0]);
             System.out.println();
             System.out.println("1+1="+service.add(1, 1));
             System.out.println("2-1="+service.sub(2, 1));
             System.out.println("2*3="+service.mul(2, 3));
         }else{
             System.out.println("没有找到需要的服务！");
         }
    }

    @Override
    public void stop(BundleContext context) throws Exception {

    }

}
