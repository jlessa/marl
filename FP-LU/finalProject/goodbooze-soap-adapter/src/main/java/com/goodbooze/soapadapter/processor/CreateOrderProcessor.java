package com.goodbooze.soapadapter.processor;



import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.cxf.message.MessageContentsList;

import com.goodbooze.messages.createordermsg.v1.CreateOrderMsg;


/**
 * The Class CreateOrderProcessor gets message from cxf service endpoint and transform it in a create order message.
 */
public class CreateOrderProcessor implements Processor{

    /** {@inheritDoc} */
    public void process(Exchange exchange) throws Exception {
        
        MessageContentsList list = exchange.getIn().getBody(MessageContentsList.class);
        CreateOrderMsg createOrderMsg = (CreateOrderMsg)list.get(0);
        createOrderMsg.setInsertDate(getXMLGregorianCalendarNow());
        exchange.getIn().setBody(createOrderMsg);
    }

    /**
     * Gets the XML gregorian calendar now.
     *
     * @return the XML gregorian calendar now
     * @throws DatatypeConfigurationException the datatype configuration exception
     */
    public XMLGregorianCalendar getXMLGregorianCalendarNow() 
            throws DatatypeConfigurationException
    {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        XMLGregorianCalendar now = 
            datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        return now;
    }
}
