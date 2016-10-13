package au.com.marlo.trainning.osgi.consumer.test;

import org.apache.camel.CamelContext;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Test;

import static org.apache.activemq.camel.component.ActiveMQComponent.activeMQComponent;


public class ConsumerTest extends CamelBlueprintTestSupport {

    private String queuePath = "activemq:queue:test-queue";

    // Create the Camel Context
    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        camelContext.addComponent("activemq", activeMQComponent("vm://localhost?broker.persistent=false"));

        return camelContext;
    }

    // Load the blueprint file
    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/queueConsumer.xml";
    }

    // Tests if the queue can consume a message
    @Test
    public void testRoute() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:out");
        String expectedMessage = "Test Message";
        // send a message
        template.sendBody(queuePath, "Test Message");

        // set mock expectations
        mock.expectedMessageCount(1);
        mock.message(0).body().isEqualTo(expectedMessage);

        // assert mocks
        assertMockEndpointsSatisfied();

    }


}