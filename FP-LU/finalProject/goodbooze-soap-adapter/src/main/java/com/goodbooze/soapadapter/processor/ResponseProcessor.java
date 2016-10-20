package com.goodbooze.soapadapter.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.cxf.message.MessageContentsList;

import com.goodbooze.wsdl.createorder.v1.CreateOrderResponse;


/**
 * The Class ResponseProcessor sets a successful response message.
 */
public class ResponseProcessor implements Processor{

    /** {@inheritDoc} */
    public void process(Exchange exchange) throws Exception {
        MessageContentsList msgContList = new MessageContentsList();
        CreateOrderResponse response = new CreateOrderResponse();
        response.setResponse("Request Received with sucess");
        msgContList.set(0, response);
        exchange.getOut().setBody(msgContList);
    }

}
