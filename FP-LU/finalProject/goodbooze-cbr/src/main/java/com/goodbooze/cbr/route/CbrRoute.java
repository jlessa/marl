package com.goodbooze.cbr.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;


/**
 * The Class CbrRoute is responsible for handle income messages in cbr component by determining to what
 * supplier translator it should be sent.
 */
public class CbrRoute extends RouteBuilder{

    /* (non-Javadoc)
     * @see org.apache.camel.builder.RouteBuilder#configure()
     */
    @Override
    public void configure() throws Exception {
        
        Namespaces ns = new Namespaces("tns","http://goodbooze.com/messages/supplierordermsg/v1.0" )
        .add("tns1", "http://goodbooze.com/types/typesdefinition/v1.0")
        .add("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        
        from("{{activemq.cbrQueue}}")
        .id("CbrRoute")
        .log("starting cbrQueueRoute")
        .choice()
            .when(ns.xpath("//tns1:supplier[tns1:id = 1]"))
                .to("{{activemq.supATranslator}}")
                .log("message sent to supATranslator")
                
            .when(ns.xpath("//tns1:supplier[tns1:id = 2]"))
                .to("{{activemq.supBTranslator}}")
                .log("message sent to supBTranslator")
                
            .when(ns.xpath("//tns1:supplier[tns1:id = 3]"))
                .to("{{activemq.supCTranslator}}")
                .log("message sent to supCTranslator");
        
    }

}
