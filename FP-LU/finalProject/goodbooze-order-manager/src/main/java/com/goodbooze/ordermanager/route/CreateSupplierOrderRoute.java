package com.goodbooze.ordermanager.route;

import org.apache.camel.builder.RouteBuilder;


/**
 * The Class CreateSupplierOrderRoute this route intends to create supplier orders and send it to db-translator 
 * component.
 * this route consumes from the createSupplierOrderQueue, that has an enriched saveStoreOrderMsg
 * from db-translator, with suppliers order ids and products suppliers.
 * from that message is created one supplier order for each product supplier as necessary.
 */
public class CreateSupplierOrderRoute extends RouteBuilder{
    

    /* (non-Javadoc)
     * @see org.apache.camel.builder.RouteBuilder#configure()
     */
    @Override
    public void configure() throws Exception {
        from("{{activemq.createSupplierOrderQueue}}")
            .routeId("createSupplierOrderRoute")
            .log("Consuming from createSupplierOrderQueue")
            //makes all the necessary transformation to create the supplier's order
            .to("xquery:xquery/save-supplier-order-transformation.xq")
            //set this header for bd-translator knows the type of this message
            .setHeader("type", constant("saveSupplierOrder"))
            //send to db-translator
            .to("{{activemq.saveSupplierOrderQueue}}")
            .log("createSupplierOrderRoute completed");
    }

}
