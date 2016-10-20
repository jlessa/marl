package com.goodbooze.ordermanager.route;

import org.apache.camel.builder.RouteBuilder;



/**
 * The Class SupplierOrderRequestRoute consumes from a queue that has an enriched saveSupplierOrderMsg
 *  with its ids and encapsulates this message in the canonical format that is expected by splitter component.
 */
public class SupplierOrderRequestRoute extends RouteBuilder{


    /* (non-Javadoc)
     * @see org.apache.camel.builder.RouteBuilder#configure()
     */
    @Override
    public void configure() throws Exception {
        from("{{activemq.createSupplierOrderRequestQueue}}")
            .routeId("createSupplierOrderRequestRoute")
            .log("Consuming from supplierOrderQueue")
            //transformation
            .to("xquery:xquery/create-supplier-order-request-transformation.xq")
            //send to splitter queue
            .to("{{activemq.splitterQueue}}")
            .log("createSupplierOrderRequestRoute completed");
        
    }

}
