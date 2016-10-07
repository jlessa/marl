package au.com.marlo.trainning.activemq.test;



import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.model.ProcessorDefinition;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Before;
import org.junit.Test;

import static org.apache.activemq.camel.component.ActiveMQComponent.activeMQComponent;


public class SubscriberTest extends CamelBlueprintTestSupport {

    private String queuePath = "activemq:topic:test-topic";

    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        camelContext.addComponent("activemq", activeMQComponent("vm://localhost?broker.persistent=false"));

        return camelContext;
    }

    // override this method, and return the location of our Blueprint XML file to be used for testing
    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/topic.xml";
    }

    // here we have regular JUnit @Test method
    @Test
    public void testRoute() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:out");
        String expectedMessage = "Test Message";
        // set mock expectations
        mock.expectedMessageCount(1);

        // send a message
        template.sendBody(queuePath, "Test Message");


        // assert mocks
        //getMockEndpoint("mock:out").setExpectedCount(1);
        assertMockEndpointsSatisfied();

    }

}