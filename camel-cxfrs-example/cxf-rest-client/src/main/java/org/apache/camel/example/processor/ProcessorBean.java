package org.apache.camel.example.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by zxm on 2016/4/19.
 */
public class ProcessorBean implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        System.out.println("====================================================");
        System.out.println(exchange);
        System.out.println("====================================================");
    }
}
