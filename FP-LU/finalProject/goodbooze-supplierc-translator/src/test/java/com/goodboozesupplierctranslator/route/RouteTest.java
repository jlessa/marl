package com.goodboozesupplierctranslator.route;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.goodbooze.messages.dbtranslator.saveordernumbermsg.v1.UpdateOrderNumber;
import com.goodbooze.supplierctranslator.route.SupCOrderTranslatorRoute;

public class RouteTest extends CamelTestSupport {

    
    private static final String INPUT_ORDER_MESSAGE = "SupCOrderTranslatorRoute/msg/input/supCorderMessage.xml";

    @EndpointInject(uri = "mock:supplierQueue")
    protected MockEndpoint endEndpoint;

    @EndpointInject(uri = "mock:activemq:updateOrderNumberQueue")
    protected MockEndpoint mockUpdateOrderNumberQueue;



    @Produce(uri = "direct:activemq:supcTranslatorQueue")
    protected ProducerTemplate startEndpoint;

    /**
     * creates an auxiliary route to mock supplier c service
     */
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {

            @Override
            public void configure() throws Exception {
                from("direct:supplierc")
                        .setBody(
                                simple("<?xml version=\"1.0\" encoding=\"UTF-8\"?> "
                                        + "<tns:response xmlns:tns=\"http://supplier.com/supplierc/messages/response/v1.0\" "
                                        + "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" "
                                        + "xsi:schemaLocation=\"http://supplier.com/supplierc/messages/response/v1.0 NewOrderResponse.xsd \">"
                                        + " <tns:orderNumber>1</tns:orderNumber>"
                                        + "</tns:response>"));
            }
        };
    }

    /**
     * override method to add properties to camel context
     */
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = new DefaultCamelContext();
        PropertiesComponent properties = new PropertiesComponent();
        properties
                .setLocation("classpath:SupCOrderTranslatorRoute/conf/com.goodbooze.supplierc.translator.cfg");
        context.addComponent("properties", properties);
        context.addRoutes(new SupCOrderTranslatorRoute());
        return context;
    }

    /**
     * checks route logic by send an initial message and asserting the final message at route end endpoint
     * @throws JAXBException
     * @throws CamelExecutionException
     * @throws IOException
     */
    @Test
    public void testFinalMessage() throws JAXBException, CamelExecutionException, IOException {
        startEndpoint.sendBody(fileToString(getClass(), INPUT_ORDER_MESSAGE));

        // creating an unmarshaller
        JAXBContext jc = JAXBContext.newInstance("com.goodbooze.messages.dbtranslator.saveordernumbermsg.v1");
        Unmarshaller unmarshaller = jc.createUnmarshaller();
 
        String resultBody = mockUpdateOrderNumberQueue.getExchanges().get(0).getIn().getBody(String.class);
        UpdateOrderNumber response = (UpdateOrderNumber) unmarshaller.unmarshal(new StringReader(resultBody));
                
        assertEquals("1",response.getOrderNumber());
        
        String prop =  (String) mockUpdateOrderNumberQueue.getExchanges().get(0).getProperty("orderId");
        
        
        
        assertEquals("1", prop);
    }
    
    /**
     * convert a file into a string
     * @param loader
     * @param fileLocation
     * @return
     * @throws IOException
     */
    public static String fileToString(Class<?> loader, String fileLocation)
            throws IOException {
        return IOUtils.toString(
                loader.getClassLoader().getResource(fileLocation),
                Charset.defaultCharset());
    }
}
