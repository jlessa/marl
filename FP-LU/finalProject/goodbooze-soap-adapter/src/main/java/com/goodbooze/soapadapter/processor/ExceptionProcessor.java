package com.goodbooze.soapadapter.processor;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import com.goodbooze.wsdl.createorder.v1.Error;
import com.goodbooze.wsdl.createorder.v1.ErrorMessage;


/**
 * The Class ExceptionProcessor sets a error response message.
 */
public class ExceptionProcessor implements Processor{

    /** {@inheritDoc} */
    public void process(Exchange exchange) throws Exception {
        Error error = new Error();
        error.setReason("an error occured while processing your request" );
        Exception ex = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
        
        StackTraceElement[] stackTrace = new StackTraceElement[]{new StackTraceElement("", "", "", 0)};
        
        ErrorMessage fault = new ErrorMessage(ex.getMessage(), error);
        fault.setStackTrace(stackTrace);
        
        exchange.getOut().setFault(true);
        exchange.getOut().setBody(fault);
    }

}
