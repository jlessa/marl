package com.goodbooze.cbr.route;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import com.goodbooze.cbr.testutil.Util;
import com.goodbooze.messages.supplierordermsg.v1.SupplierOrderMsg;


public class CbrRouteTest extends CamelTestSupport{
    final static String SUPA_INPUT_MESSAGE_URL = "CbrRoute/msg/input/supAorderMessage.xml";
    final static String SUPB_INPUT_MESSAGE_URL = "CbrRoute/msg/input/supBorderMessage.xml";
    final static String SUPC_INPUT_MESSAGE_URL = "CbrRoute/msg/input/supCorderMessage.xml";
    
    
    @EndpointInject(uri = "mock:activemq:supATranslator")
    protected MockEndpoint supATranslatorEndpoint;
    
    @EndpointInject(uri = "mock:activemq:supBTranslator")
    protected MockEndpoint supBTranslatorEndpoint;
    
    @EndpointInject(uri = "mock:activemq:supCTranslator")
    protected MockEndpoint supCTranslatorEndpoint;

    @Produce(uri = "direct:activemq:cbrQueue")
    protected ProducerTemplate startEndpoint;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new CbrRoute();
    }

    /**
     * override method to add properties to camel context
     */
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = new DefaultCamelContext();
        PropertiesComponent properties = new PropertiesComponent();
        properties.setLocation("classpath:CbrRoute/conf/com.goodbooze.cbr.cbrRoute.cfg");
        context.addComponent("properties", properties);

        return context;
    }
    
    /**
     * checks if the supplier from the message is correct;
     * sending 3 supplier order messages to cbr inner queue, one for each supplier 
     * and verifying if the correct message is directed to the supplier a translator queue 
     */
    @Test
    public void testForSupAMessages() throws IOException, JAXBException{
        
        String bodySendA = Util.fileToString(getClass(), SUPA_INPUT_MESSAGE_URL);
        String bodySendB = Util.fileToString(getClass(), SUPB_INPUT_MESSAGE_URL);
        String bodySendC = Util.fileToString(getClass(), SUPC_INPUT_MESSAGE_URL);
        startEndpoint.sendBody(bodySendA);
        startEndpoint.sendBody(bodySendB);
        startEndpoint.sendBody(bodySendC);


        // creating an unmarshaller
        JAXBContext jc = JAXBContext.newInstance("com.goodbooze.messages.supplierordermsg.v1");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        
        //getting first message body
        String resultBody = supATranslatorEndpoint.getExchanges().get(0).getIn().getBody(String.class);
        SupplierOrderMsg response = (SupplierOrderMsg) unmarshaller.unmarshal(new StringReader(resultBody));
        int supId = response.getSupplierOrder().getSupplier().getId();
        
        assertEquals(1,supId);
    }
    
    /**
     * checks if the supplier from the message is correct;
     * sending 3 supplier order messages to cbr inner queue, one for each supplier 
     * and verifying if the correct message is directed to the supplier b translator queue 
     */
    @Test
    public void testForSupBMessages() throws Exception{
        
        String bodySendA = Util.fileToString(getClass(), SUPA_INPUT_MESSAGE_URL);
        String bodySendB = Util.fileToString(getClass(), SUPB_INPUT_MESSAGE_URL);
        String bodySendC = Util.fileToString(getClass(), SUPC_INPUT_MESSAGE_URL);
        startEndpoint.sendBody(bodySendA);
        startEndpoint.sendBody(bodySendB);
        startEndpoint.sendBody(bodySendC);

        // creating an unmarshaller
        JAXBContext jc = JAXBContext.newInstance("com.goodbooze.messages.supplierordermsg.v1");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        
        assertEquals(1, supBTranslatorEndpoint.getExchanges().size());
        
        //getting first message body
        String resultBody = supBTranslatorEndpoint.getExchanges().get(0).getIn().getBody(String.class);
        SupplierOrderMsg response = (SupplierOrderMsg) unmarshaller.unmarshal(new StringReader(resultBody));
        int supId = response.getSupplierOrder().getSupplier().getId();
        
        assertEquals(2,supId);
    }
    
    /**
     * checks if the supplier from the message is correct;
     * sending 3 supplier order messages to cbr inner queue, one for each supplier 
     * and verifying if the correct message is directed to the supplier c translator queue 
     */
    @Test
    public void testForSupCMessages() throws Exception{
        
        String bodySendA = Util.fileToString(getClass(), SUPA_INPUT_MESSAGE_URL);
        String bodySendB = Util.fileToString(getClass(), SUPB_INPUT_MESSAGE_URL);
        String bodySendC = Util.fileToString(getClass(), SUPC_INPUT_MESSAGE_URL);
        startEndpoint.sendBody(bodySendA);
        startEndpoint.sendBody(bodySendB);
        startEndpoint.sendBody(bodySendC);


        // creating an unmarshaller
        JAXBContext jc = JAXBContext.newInstance("com.goodbooze.messages.supplierordermsg.v1");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        
        //getting first message body
        String resultBody = supCTranslatorEndpoint.getExchanges().get(0).getIn().getBody(String.class);
        SupplierOrderMsg response = (SupplierOrderMsg) unmarshaller.unmarshal(new StringReader(resultBody));
        int supId = response.getSupplierOrder().getSupplier().getId();
        
        assertEquals(3,supId);
    }
}
