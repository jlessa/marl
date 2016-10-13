package au.com.marlo.trainning.osgi.producer.test;

import org.apache.camel.*;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Before;
import org.junit.Test;
import static org.apache.activemq.camel.component.ActiveMQComponent.activeMQComponent;

/*
** Class to test the Request Reply Pattern
*/
public class RequestTest extends CamelBlueprintTestSupport {


    protected MockEndpoint resultEndpoint;
    protected MockEndpoint replyEndpoint;

    //Create a camel context to set the embedded activemq
    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        camelContext.addComponent("activemq", activeMQComponent("vm://localhost?broker.persistent=false"));

        return camelContext;
    }

    // Loads the Blueprint File
    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/queueRequest.xml";
    }


    protected Processor createProcessor() throws Exception {
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                final String message = "This is a Response Message";
                exchange.getIn().setBody(message);
            }
        };
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        resultEndpoint = context.getEndpoint("mock:out", MockEndpoint.class);
        replyEndpoint = context.getEndpoint("mock:reply", MockEndpoint.class);
    }


    //Test if the route is requesting and receiving a reply
    @Test
    public void testJmsRouteWithTextMessage() throws Exception {
        String expectedMessage = "This is a Response Message";

        replyEndpoint.whenAnyExchangeReceived(createProcessor());
        resultEndpoint.setExpectedCount(1);
        resultEndpoint.message(0).body().isEqualTo(expectedMessage);

        resultEndpoint.assertIsSatisfied();
    }
}
