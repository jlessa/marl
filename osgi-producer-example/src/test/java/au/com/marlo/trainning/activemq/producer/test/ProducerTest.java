package au.com.marlo.trainning.activemq.producer.test;

import au.com.marlo.trainning.activemq.main.Producer;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;


import static org.apache.activemq.camel.component.ActiveMQComponent.activeMQComponent;



//Test class to check if the Producer is sending message to a Queue
public class ProducerTest extends CamelTestSupport {

    protected MockEndpoint resultEndpoint;

    //create a camel context to set the queue embeded
    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        camelContext.addComponent("activemq", activeMQComponent("vm://localhost?broker.persistent=false"));

        PropertiesComponent propertiesComponent = new PropertiesComponent();
        propertiesComponent.setLocation("classpath:testproperties.properties");
        camelContext.addComponent("properties",propertiesComponent);

        return camelContext;
    }

    //test the created Route
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new Producer();
    }

    //set up the mockEndpoint
    @Before
    public void setUp() throws Exception {
        super.setUp();
        resultEndpoint = (MockEndpoint) context.getEndpoint("mock:out");
    }

    //Test if the queue received an specific message
    @Test
    public void testJmsRouteWithTextMessage() throws Exception {
        String expectedMessage = "This is a Request Message";
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.message(0).body().contains(expectedMessage);
        resultEndpoint.assertIsSatisfied();
    }

}