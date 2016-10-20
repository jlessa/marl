package com.goodbooze.ordermanager.route;

import org.apache.camel.builder.RouteBuilder;


/**
 * this route gets a message from soap channel adapter and transform this message in
 * a canonical-model's SaveStoreOrderMsg after the message transformation,
 * it is send to saveStoreOrderQueue where is consumed by db-translator component.
 */
public class CreateStoreOrderRoute extends RouteBuilder{
    

    /* (non-Javadoc)
     * @see org.apache.camel.builder.RouteBuilder#configure()
     */
    @Override
    public void configure() throws Exception {
        
        from("{{activemq.createStoreOrderQueue}}")
            .routeId("createStoreOrderRoute")
            .log("stating saveStoreOrderRoute")
            //transformation
            .to("xquery:xquery/save-storeorder-transformation.xq")
            .setHeader("type", constant("saveStoreOrder"))
            //send to db-translator queue
            .to("{{activemq.saveStoreOrderQueue}}")
            .log("createStoreOrderRoute completed");
    }

}
