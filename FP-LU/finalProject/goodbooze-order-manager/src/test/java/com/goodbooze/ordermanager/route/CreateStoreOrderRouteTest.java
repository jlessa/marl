package com.goodbooze.ordermanager.route;

import java.io.IOException;

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

import com.goodbooze.ordermanager.testutil.Util;


/**
 * 
 * @author Luiz Pessanha
 *
 */
public class CreateStoreOrderRouteTest extends CamelTestSupport{
    
    final static String INPUT_MESSAGE_URL = "CreateStoreOrderRoute/msg/input/CreateOrderMsg.xml";
    final static String EXPECTED_MESSAGE_URL = "CreateStoreOrderRoute/msg/output/saveStoreOrderMsg.xml";
    
    @EndpointInject(uri = "mock:storeOrderEndRoute")
    protected MockEndpoint endEndpoint;

    @Produce(uri = "direct:startRoute")
    protected ProducerTemplate startEndpoint;
    
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new CreateStoreOrderRoute();
    }

    /**
     * override method to add properties to camel context
     */
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = new DefaultCamelContext();
        PropertiesComponent properties = new PropertiesComponent();
        properties
                .setLocation("classpath:CreateStoreOrderRoute/conf/com.goodbooze.ordermanager.createStoreOrderRouter.cfg");
        context.addComponent("properties", properties);

        return context;
    }
    
    /**
     * checks if the content in the final message will be as expected after the
     * transformation, according to the canonical model SaveStoreOrderMsg.
     * Using an expected message for comparison.
     */
    @Test
    public void checkTransformationContent() throws Exception {

        // getting content from file to test final transformation
        String resultBody = Util.fileToString(getClass(), EXPECTED_MESSAGE_URL);

        endEndpoint.expectedBodiesReceived(resultBody);

        String bodySend = Util.fileToString(getClass(), INPUT_MESSAGE_URL);
        startEndpoint.sendBody(bodySend);

        endEndpoint.assertIsSatisfied();
    }
    
    /**
     * checks if header type with value "saveStoreOrder" was corrected added to the message
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void checkHeaderTest() throws IOException, InterruptedException{
        endEndpoint.expectedHeaderReceived("type", "saveStoreOrder");
        
        String bodySend = Util.fileToString(getClass(), INPUT_MESSAGE_URL);
        startEndpoint.sendBody(bodySend);

        endEndpoint.assertIsSatisfied();
    }
        
}
