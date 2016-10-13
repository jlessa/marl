package au.com.marlo.trainning.osgi.producer.test;

import org.apache.camel.CamelContext;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Test;
import static org.apache.activemq.camel.component.ActiveMQComponent.activeMQComponent;


//Test class to check if the Producer is sending message to a Queue
public class ProducerTest extends CamelBlueprintTestSupport {

    // Create the Camel Context
    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        camelContext.addComponent("activemq", activeMQComponent("vm://localhost?broker.persistent=false"));

        return camelContext;
    }

    // Load the blueprint file
    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/queueProducer.xml";
    }


    //Test if the queue received an specific message
    @Test
    public void testRoute() throws Exception {
        MockEndpoint resultEndpoint = getMockEndpoint("mock:out");
        String expectedMessage = "This is a Request Message";
        resultEndpoint.expectedMessageCount(1);
        resultEndpoint.message(0).body().contains(expectedMessage);
        resultEndpoint.assertIsSatisfied();
    }

}