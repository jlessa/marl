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


public class PublisherTest extends CamelBlueprintTestSupport {

    protected MockEndpoint resultEndpoint;


    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        camelContext.addComponent("activemq", activeMQComponent("vm://localhost?broker.persistent=false"));

        return camelContext;
    }

    // Load the blueprint file
    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/topic.xml";
    }

    // Setup the Mock Endpoint
    @Before
    public void setUp() throws Exception {
        super.setUp();
        resultEndpoint = (MockEndpoint) context.getEndpoint("mock:out");
    }

    // Tests if the route can pubish a message to a topic
    @Test
    public void testRouteWithTextMessage() throws Exception {
        String expectedBody = "This is a Request Message";

        resultEndpoint.setExpectedCount(1);
        resultEndpoint.message(0).body().contains(expectedBody);

        resultEndpoint.assertIsSatisfied();
    }


}