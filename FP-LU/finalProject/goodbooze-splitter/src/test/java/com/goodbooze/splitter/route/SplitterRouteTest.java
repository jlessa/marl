package com.goodbooze.splitter.route;

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

import com.goodbooze.messages.supplierordermsg.v1.SupplierOrderMsg;
import com.goodbooze.splitter.testutil.Util;


public class SplitterRouteTest extends CamelTestSupport {

    final static String INPUT_MESSAGE_URL = "SplitterRoute/msg/input/supplierOrderRequest.xml";

    @EndpointInject(uri = "mock:activemq:cbrQueue")
    protected MockEndpoint endEndpoint;

    @Produce(uri = "direct:activemq:splitterQueue")
    protected ProducerTemplate startEndpoint;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new SplitterRoute();
    }

    /**
     * override method to add properties to camel context
     */
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = new DefaultCamelContext();
        PropertiesComponent properties = new PropertiesComponent();
        properties
                .setLocation("classpath:SplitterRoute/conf/com.goodbooze.splitter.splitterRoute.cfg");
        context.addComponent("properties", properties);

        return context;
    }

    /**
     * checks if the message count received by endpoint is as expected
     */
    @Test
    public void checkMessageCount() throws Exception {

        endEndpoint.expectedMessageCount(2);

        String bodySend = Util.fileToString(getClass(), INPUT_MESSAGE_URL);
        startEndpoint.sendBody(bodySend);

        endEndpoint.assertIsSatisfied();
    }

    /**
     * checks the content of the received message in route final endpoint
     * @throws JAXBException 
     * @throws IOException 
     */
    @Test
    public void checkMessageContent() throws JAXBException, IOException {
        
        String bodySend = Util.fileToString(getClass(), INPUT_MESSAGE_URL);
        startEndpoint.sendBody(bodySend);

        // creating an unmarshaller
        JAXBContext jc = JAXBContext.newInstance("com.goodbooze.messages.supplierordermsg.v1");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        
        //getting first message body
        String resultBody = endEndpoint.getExchanges().get(0).getIn().getBody(String.class);
        assertNotNull(resultBody);
        SupplierOrderMsg response = (SupplierOrderMsg) unmarshaller.unmarshal(new StringReader(resultBody));
        assertNotNull(response);
        assertEquals(1, response.getSupplierOrder().getId());
        assertEquals(2, response.getSupplierOrder().getItemSupplierOrderList().getItemSupplier().size());
        
        long prod1Quantity = response.getSupplierOrder().getItemSupplierOrderList().getItemSupplier().get(0).getQuantity();
        String prod1Name = response.getSupplierOrder().getItemSupplierOrderList().getItemSupplier().get(0).getProduct().getName();
        long prod2Quantity = response.getSupplierOrder().getItemSupplierOrderList().getItemSupplier().get(1).getQuantity();
        String prod2Name = response.getSupplierOrder().getItemSupplierOrderList().getItemSupplier().get(1).getProduct().getName();
        
        assertEquals(4, prod1Quantity);
        assertEquals("Heineken", prod1Name);

        assertEquals(9, prod2Quantity);
        assertEquals("Stella Artois", prod2Name);
        
        resultBody = endEndpoint.getExchanges().get(1).getIn().getBody(String.class);

        response = (SupplierOrderMsg) unmarshaller.unmarshal(new StringReader(resultBody));
        long prod3Quantity = response.getSupplierOrder().getItemSupplierOrderList().getItemSupplier().get(0).getQuantity();
        String prod3Name = response.getSupplierOrder().getItemSupplierOrderList().getItemSupplier().get(0).getProduct().getName();

        assertEquals(2, prod3Quantity);
        assertEquals("Therezopolis", prod3Name);
    }
}
