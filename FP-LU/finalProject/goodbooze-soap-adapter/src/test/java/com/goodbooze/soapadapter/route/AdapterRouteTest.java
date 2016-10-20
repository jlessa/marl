package com.goodbooze.soapadapter.route;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.cxf.message.MessageContentsList;
import org.junit.Test;

import com.goodbooze.messages.createordermsg.v1.CreateOrderMsg;
import com.goodbooze.soapadapter.testutil.Util;
import com.goodbooze.wsdl.createorder.v1.CreateOrderResponse;
import com.goodbooze.wsdl.createorder.v1.ErrorMessage;

public class AdapterRouteTest extends CamelTestSupport {
    final static String INPUT_MESSAGE_URL = "AdapterRoute/msg/input/soapRequest.xml";

    @EndpointInject(uri = "mock:activemq:orderManagerQueue")
    protected MockEndpoint activemqEndpoint;

    @Produce(uri = "direct:createOrderServiceEndpoint")
    protected ProducerTemplate startEndpoint;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new AdapterRoute();
    }

    /**
     * override method to add properties to camel context
     */
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = new DefaultCamelContext();
        PropertiesComponent properties = new PropertiesComponent();
        properties
                .setLocation("classpath:AdapterRoute/conf/com.goodbooze.soapadapter.AdapterRoute.cfg");
        context.addComponent("properties", properties);

        return context;
    }

    /**
     * this test checks if received message in endpoint
     * {{activemq.orderManagerQueue}} have the expected values of store,
     * products and quantity
     * 
     * @throws IOException
     * @throws JAXBException
     */
    @Test
    public void testRouteMessage() throws IOException, JAXBException {
        MessageContentsList msgListIn = new MessageContentsList();
        CreateOrderMsg createOrderMsg = Util.getCreateOrderMsg();
        msgListIn.set(0, createOrderMsg);
        startEndpoint.sendBody(msgListIn);

        // creating an unmarshaller
        JAXBContext jc = JAXBContext
                .newInstance("com.goodbooze.messages.createordermsg.v1");
        Unmarshaller unmarshaller = jc.createUnmarshaller();

        String resultBody = activemqEndpoint.getExchanges().get(0).getIn()
                .getBody(String.class);

        CreateOrderMsg receivedCreateOrderMsg = (CreateOrderMsg) unmarshaller
                .unmarshal(new StringReader(resultBody));
        
        assertNotNull(receivedCreateOrderMsg.getInsertDate());

        int storeId = receivedCreateOrderMsg.getCreateOrderList()
                .getOrderList().getOrder().get(0).getStore().getId();
        String storeName = receivedCreateOrderMsg.getCreateOrderList()
                .getOrderList().getOrder().get(0).getStore().getName();
        assertEquals(1, storeId);
        assertEquals("Store A", storeName);

        int itemListSize = receivedCreateOrderMsg.getCreateOrderList()
                .getOrderList().getOrder().get(0).getItemList()
                .getItemStoreOrder().size();
        assertEquals(1, itemListSize);

        int productId = receivedCreateOrderMsg.getCreateOrderList()
                .getOrderList().getOrder().get(0).getItemList()
                .getItemStoreOrder().get(0).getProduct().getInternalId();
        assertEquals(1, productId);

        long productQuantity = receivedCreateOrderMsg.getCreateOrderList()
                .getOrderList().getOrder().get(0).getItemList()
                .getItemStoreOrder().get(0).getQuantity();
        assertEquals(10, productQuantity);
    }

    /**
     * checks if the returned message contains the corrected response message
     */
    @Test
    public void testResonseMessage() {
        MessageContentsList msgListIn = new MessageContentsList();
        CreateOrderMsg createOrderMsg = Util.getCreateOrderMsg();
        msgListIn.set(0, createOrderMsg);
        MessageContentsList msgContList = (MessageContentsList) startEndpoint
                .requestBody(msgListIn);
        CreateOrderResponse response = (CreateOrderResponse) msgContList.get(0);

        assertEquals("Request Received with sucess", response.getResponse());
    }

    /**
     * checks if when an exception occurs it is correctly handled and a fault
     * message is returned
     */
    @Test
    public void testFaultMessage() {
        MessageContentsList msgListIn = new MessageContentsList();
        CreateOrderMsg createOrderMsg = Util.getCreateOrderMsg();
        msgListIn.set(0, createOrderMsg);

        activemqEndpoint.whenAnyExchangeReceived(new Processor() {

            public void process(Exchange exchange) throws Exception {
                throw new Exception("test exception");

            }
        });

        ErrorMessage response = (ErrorMessage) startEndpoint.requestBody(msgListIn);
        String reason = response.getFaultInfo().getReason();
        assertEquals("an error occured while processing your request", reason);
        

    }

}
