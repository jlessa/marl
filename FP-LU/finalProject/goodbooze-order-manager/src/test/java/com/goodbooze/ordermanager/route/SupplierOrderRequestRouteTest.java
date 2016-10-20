package com.goodbooze.ordermanager.route;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
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

import com.goodbooze.messages.supplierorderrequestmsg.v1.SupplierOrderRequestMsg;
import com.goodbooze.ordermanager.testutil.Util;

public class SupplierOrderRequestRouteTest extends CamelTestSupport{
    
    final static String INPUT_MESSAGE_URL = "SupplierOrderRequestRoute/msg/input/saveSupplierOrderMsg.xml";
    
    @EndpointInject(uri = "mock:activemq:splitterQueue")
    protected MockEndpoint endEndpoint;

    @Produce(uri = "direct:startRoute")
    protected ProducerTemplate startEndpoint;
    
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new SupplierOrderRequestRoute();
    }

    /**
     * override method to add properties to camel context
     */
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = new DefaultCamelContext();
        PropertiesComponent properties = new PropertiesComponent();
        properties
                .setLocation("classpath:SupplierOrderRequestRoute/conf/com.goodbooze.ordermanager.SupplierOrderRequestRoute.cfg");
        context.addComponent("properties", properties);

        return context;
    }
    
    /**
     * checks if the products and its quantities are corrected in the message
     * 
     */
    @Test
    public void checkTransformationContent() throws Exception {
        String bodySend = Util.fileToString(getClass(), INPUT_MESSAGE_URL);
        startEndpoint.sendBody(bodySend);

        // creating an unmarshaller
        JAXBContext jc = JAXBContext
                .newInstance("com.goodbooze.messages.supplierorderrequestmsg.v1");
        Unmarshaller unmarshaller = jc.createUnmarshaller();

        String resultBody = endEndpoint.getExchanges().get(0).getIn()
                .getBody(String.class);

        SupplierOrderRequestMsg response = (SupplierOrderRequestMsg) unmarshaller.unmarshal(new StringReader(resultBody));

        long prod1Quantity = response.getSupplierOrderList().getSupplierOrder().get(0).getItemSupplierOrderList().getItemSupplier().get(0).getQuantity();
        String prod1Name = response.getSupplierOrderList().getSupplierOrder().get(0).getItemSupplierOrderList().getItemSupplier().get(0).getProduct().getName();
        long prod2Quantity = response.getSupplierOrderList().getSupplierOrder().get(0).getItemSupplierOrderList().getItemSupplier().get(1).getQuantity();
        String prod2Name = response.getSupplierOrderList().getSupplierOrder().get(0).getItemSupplierOrderList().getItemSupplier().get(1).getProduct().getName();
        long prod3Quantity = response.getSupplierOrderList().getSupplierOrder().get(1).getItemSupplierOrderList().getItemSupplier().get(0).getQuantity();
        String prod3Name = response.getSupplierOrderList().getSupplierOrder().get(1).getItemSupplierOrderList().getItemSupplier().get(0).getProduct().getName();

        assertEquals(4, prod1Quantity);
        assertEquals("Heineken", prod1Name);

        assertEquals(9, prod2Quantity);
        assertEquals("Stella Artois", prod2Name);

        assertEquals(2, prod3Quantity);
        assertEquals("Therezopolis", prod3Name);
    }
    
}
