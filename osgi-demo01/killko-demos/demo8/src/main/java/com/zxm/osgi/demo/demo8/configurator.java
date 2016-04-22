/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zxm.osgi.demo.demo8;

import java.util.Dictionary;
import java.util.Enumeration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

/**
 *
 * @author han
 */
public class configurator implements ManagedService {

    @Override
    public void updated(Dictionary dctnr) throws ConfigurationException {
        if (dctnr != null) {
            Enumeration enumeration = dctnr.keys();
            while (enumeration.hasMoreElements()) {
                String key = (String) enumeration.nextElement();
                System.out.println(key + " = " + dctnr.get(key));
            }
        }
    }
}
