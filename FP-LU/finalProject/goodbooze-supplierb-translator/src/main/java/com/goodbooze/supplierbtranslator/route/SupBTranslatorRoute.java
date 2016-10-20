package com.goodbooze.supplierbtranslator.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;


/**
 * The Class SupBTranslatorRoute translates a message into supplier b model, send it to supplier b, gets it answer
 * and send a message to db translator with the order number to be saved in db.
 */
public class SupBTranslatorRoute extends RouteBuilder {

    /* (non-Javadoc)
     * @see org.apache.camel.builder.RouteBuilder#configure()
     */
    @Override
    public void configure() throws Exception {

        // namespaces used in xpath
        Namespaces ns = new Namespaces("tns",
                "http://goodbooze.com/messages/supplierordermsg/v1.0").add(
                "tns1", "http://goodbooze.com/types/typesdefinition/v1.0").add(
                "xsi", "http://www.w3.org/2001/XMLSchema-instance");

        from("{{activemq.supBTranslatorQueue}}")
                .streamCaching()
                .log("supplier b translator queue")
                // set property with the supplier order id for future use
                .setProperty(
                        "supplierOrderId",
                        ns.xpath("//tns:supplierOrder/tns1:id/text()",
                                String.class))
                // transform message in supplier format
                .to("xquery:xquery/supplierb-neworder-transformation.xq")
                // send to supplier and wait for its answer
                .log("${body}").removeHeader("operationName")
                .removeHeader("operationNamespace")
                .inOut("{{supplierb.service}}")
                // transform the received message into canonical model
                .to("xquery:xquery/save-ordernumber-msg-transformation.xq")
                // send to db-translator queue
                .setHeader("type", constant("saveOrderNumber"))
                .to("{{activemq.updateOrderNumberQueue}}")

                .log("supplier b translator route completed");

    }

}
