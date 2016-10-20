package com.goodbooze.splitter.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;




/**
 * The Class SplitterRoute splits original income message according to it supplier information.
 */
public class SplitterRoute extends RouteBuilder{

    /* (non-Javadoc)
     * @see org.apache.camel.builder.RouteBuilder#configure()
     */
    @Override
    public void configure() throws Exception {
        
        Namespaces ns = new Namespaces("tns","http://goodbooze.com/messages/supplierorderrequestmsg/v1.0" )
                    .add("tns1", "http://goodbooze.com/types/typesdefinition/v1.0")
                    .add("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        
        from("{{activemq.splitterQueue}}")
            .routeId("splitterRoute")
            .log("Consuming from {{activemq.splitterQueue}}")
            .split(ns.xpath("//tns1:supplierOrder"))
            .to("xquery:xquery/supplier-order-transformation.xq")
            .to("{{activemq.cbrQueue}}")
            .log("message sent to cbrQueue");
        
    }

}
