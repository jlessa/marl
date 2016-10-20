package com.goodbooze.dbtranslator.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JaxbDataFormat;

import com.goodbooze.dbtranslator.processor.SaveOrderNumberProcessor;
import com.goodbooze.dbtranslator.processor.SaveStoreOrderProcessor;
import com.goodbooze.dbtranslator.processor.SaveSupplierOrderProcessor;
import com.goodbooze.messages.dbtranslator.saveordernumbermsg.v1.UpdateOrderNumber;
import com.goodbooze.messages.dbtranslator.savestoreordermsg.v1.SaveStoreOrder;
import com.goodbooze.messages.dbtranslator.savesupplierorder.v1.SaveSupplierOrder;
import com.goodbooze.messages.dbtranslator.supplierresponsemsg.v1.SupplierResponse;
import com.goodbooze.messages.dbtranslator.suppliersrequestmsg.v1.SupplierRequest;



/**
 * The Class Route is responsible to route and handle income messages for db translator.
 */
public class Route extends RouteBuilder {

    /* (non-Javadoc)
     * @see org.apache.camel.builder.RouteBuilder#configure()
     */
    @Override
    public void configure() throws Exception {
    
        JaxbDataFormat saveStoreOrderDf = new JaxbDataFormat();
        saveStoreOrderDf.setContextPath(SaveStoreOrder.class.getPackage().getName());
        
        JaxbDataFormat saveSupplierOrderDf = new JaxbDataFormat();
        saveSupplierOrderDf.setContextPath(SaveSupplierOrder.class.getPackage().getName());
        
        JaxbDataFormat supplierRequestDf = new JaxbDataFormat();
        supplierRequestDf.setContextPath(SupplierRequest.class.getPackage().getName());
        
        JaxbDataFormat supplierResposeDf = new JaxbDataFormat();
        supplierResposeDf.setContextPath(SupplierResponse.class.getPackage().getName());
        
        JaxbDataFormat updateOrderNumberDf = new JaxbDataFormat();
        updateOrderNumberDf.setContextPath(UpdateOrderNumber.class.getPackage().getName());
        
        from("{{activemq.uri}}").log("starting db translator route")
            .choice()
                .when(header("type").isEqualTo("saveStoreOrder"))
                    .log("Message type is saveStoreOrder")
                    .unmarshal(saveStoreOrderDf)
                    .to("{{activemq.saveStoreOrdersQueue}}")
                    
                .when(header("type").isEqualTo("saveSupplierOrder"))
                    .log("Message type is saveSupplierOrder")
                    .unmarshal(saveSupplierOrderDf)
                    .to("{{activemq.saveSupplierOrderQueue}}")

                    
                .when(header("type").isEqualTo("saveOrderNumber"))
                    .log("Message type is saveOrderNumber")
                    .unmarshal(updateOrderNumberDf)
                    .to("{{activemq.saveOrderNumberQueue}}");
                
        
        
        from("{{activemq.saveStoreOrdersQueue}}")
            .log("Consuming from saveStoreOrderQueue")
            .process(new SaveStoreOrderProcessor())
            .log("Store Orders were Saved")
            .marshal(saveStoreOrderDf)          
            .to("{{activemq.storeOrderQueue}}")
            .log("message was sent to storeOrderQueue");
        
        from("{{activemq.saveSupplierOrderQueue}}")
            .log("Consuming from saveSupplierOrderQueue")
            .log("${body}")
            .process(new SaveSupplierOrderProcessor())
            .log("Supplier Orders were Saved")
            .marshal(saveSupplierOrderDf)
            .to("{{activemq.supplierOrderQueue}}")
            .log("message was sent to supplierOrderQueue");
        
        from("{{activemq.saveOrderNumberQueue}}")
            .log("Consuming from saveOrderNumberQueue")
            .process(new SaveOrderNumberProcessor())
            .log("Order Number was Saved");
    }

}
