package au.com.marlo.trainning.osgi.producer.test;

import static org.apache.activemq.camel.component.ActiveMQComponent.activeMQComponent;
import org.apache.camel.*;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Before;
import org.junit.Test;

/*
** Class to test if the Subscriber send and receive an specific messages to all subscribers
*/
public class SubscriberTest extends CamelBlueprintTestSupport {

    protected MockEndpoint resultEndpoint;
    private String topicPath = "activemq:topic:test-topic";

    protected void sendExchange(final Object expectedBody) {
        template.sendBodyAndHeader(topicPath, expectedBody, "ReplyTest", "header");
    }

    //create a camel context to set the queue embeded
    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        camelContext.addComponent("activemq", activeMQComponent("vm://localhost?broker.persistent=false"));

        return camelContext;
    }

    // Load the blueprint file
    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/topicSubscriber.xml";
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        resultEndpoint = (MockEndpoint) context.getEndpoint("mock:out");
    }


    //Test if all subscribers are receiving an specific message from a publisher
    @Test
    public void testJmsRouteWithTextMessage() throws Exception {
        String expectedBody = "Test Message";
        sendExchange(expectedBody);

        resultEndpoint.setExpectedCount(3);
        resultEndpoint.message(0).header("ReplyTest").isEqualTo("header");
        resultEndpoint.message(0).body().isEqualTo(expectedBody);

        resultEndpoint.assertIsSatisfied();
    }


}