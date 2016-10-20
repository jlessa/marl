package com.goodbooze.soapadapter.route;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JaxbDataFormat;

import com.goodbooze.messages.createordermsg.v1.CreateOrderMsg;
import com.goodbooze.soapadapter.processor.CreateOrderProcessor;
import com.goodbooze.soapadapter.processor.ExceptionProcessor;
import com.goodbooze.soapadapter.processor.ResponseProcessor;


/**
 * The Class AdapterRoute exposes an soap service, handles income message and sends to order manager queue.
 */
public class AdapterRoute extends RouteBuilder{

    /* (non-Javadoc)
     * @see org.apache.camel.builder.RouteBuilder#configure()
     */
    @Override
    public void configure() throws Exception {
        JaxbDataFormat createOrderMsgDf = new JaxbDataFormat();
        createOrderMsgDf.setContextPath(CreateOrderMsg.class.getPackage().getName());
        
            
          from("{{cxf.endpoint}}")
          .id("adapterRoute")
          .onException(Exception.class)
              .log(LoggingLevel.ERROR, "An error has occurred while message processing")
              .handled(true)
              .process(new ExceptionProcessor())
           .end()
          .log("consumed from endpoint {{cxf.endpoint}}")
          .process(new CreateOrderProcessor())
          .marshal(createOrderMsgDf)
          .inOnly("{{activemq.orderManagerQueue}}")
          .process(new ResponseProcessor());
    }

}
