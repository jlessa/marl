package com.goodbooze.supplierctranslator.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;


/**
 * The Class SupCOrderTranslatorRoute translates a message into supplier c model, sends it, gets supplier response
 * and sends a message to br translator with the order number to be saved.
 */
public class SupCOrderTranslatorRoute extends RouteBuilder{

    /* (non-Javadoc)
     * @see org.apache.camel.builder.RouteBuilder#configure()
     */
    @Override
    public void configure() throws Exception {
        //namespaces used in xpath
        Namespaces ns = new Namespaces("tns","http://goodbooze.com/messages/supplierordermsg/v1.0" )
        .add("tns1", "http://goodbooze.com/types/typesdefinition/v1.0")
        .add("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        
        from("{{activemq.supcTranslatorQueue}}")
            .id("supplier c translator route")
            .log("stanting route")
            //set property with the supplier order id for future use
            .setProperty("orderId", ns.xpath("(//tns:supplierOrder/tns1:id)/text()",String.class))
            //transform message in supplier format
            .to("xquery:xquery/supplierc-neworder-transformation.xq")
            //send to supplier and wait for its answer
            .inOut("{{activemq.suppliercQueue}}")    
            //transform the received message into canonical model
            .to("xquery:xquery/save-ordernumber-msg-transformation.xq")
            //send to db-translator queue
            .setHeader("type", constant("saveOrderNumber"))
            .to("{{activemq.updateOrderNumberQueue}}")
            .log("suplier c tranlator route completed");
        
    }

}
