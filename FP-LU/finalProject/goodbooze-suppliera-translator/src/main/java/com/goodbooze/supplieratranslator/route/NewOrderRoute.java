package com.goodbooze.supplieratranslator.route;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.xml.Namespaces;
import org.apache.camel.model.dataformat.XmlJsonDataFormat;
/**
 * Translate new order message to supplier model send to it, wait the response and send to db-translator.
 * @author Luiz Pessanha
 *
 */
public class NewOrderRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        
        //declare xmljson datatype and set its options
        XmlJsonDataFormat xmlJsonFormat = new XmlJsonDataFormat();
        xmlJsonFormat.setSkipNamespaces(true);
        xmlJsonFormat.setRemoveNamespacePrefixes(true);
        
        //namespaces used in xpath
        Namespaces ns = new Namespaces("tns","http://goodbooze.com/messages/supplierordermsg/v1.0" )
        .add("tns1", "http://goodbooze.com/types/typesdefinition/v1.0")
        .add("xsi", "http://www.w3.org/2001/XMLSchema-instance");
        
        from("{{activemq.supplieraTranslatorQueue}}")
            .log("consuming from {{activemq.supplieraTranslatorQueue}}")
            //save the order id in message property to use in xpath
            .setProperty("supplierOrderId", ns.xpath("//tns:supplierOrder/tns1:id/text()", String.class))
            //get only the items list in order to transform in json
            .to("xquery:xquery/transform-to-item-list.xq")
            //xml to json
            .marshal(xmlJsonFormat)
            //set  http method
            .setHeader(Exchange.HTTP_METHOD, constant("POST"))
            //send to supplier service
            .inOut("{{suppliera.service.url}}")
            //json to xml
            .unmarshal().xmljson()
            //transform into canonical model save order number message
            .to("xquery:xquery/save-ordernumber-message-transformation.xq")
            
            //db-translator queue
            .setHeader("type", constant("saveOrderNumber"))
            .to("{{activemq.updateOrderNumberQueue}}")
            .log("message sent to {{activemq.updateOrderNumberQueue}}");
    }

}
