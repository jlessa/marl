

package au.com.marlo.trainning.activemq.test;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.blueprint.CamelBlueprintTestSupport;
import org.junit.Before;
import org.junit.Test;

import static org.apache.activemq.camel.component.ActiveMQComponent.activeMQComponent;


public class RequestReplyTest extends CamelBlueprintTestSupport {

    protected MockEndpoint resultEndpoint;


    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        camelContext.addComponent("activemq", activeMQComponent("vm://localhost?broker.persistent=false"));

        return camelContext;
    }

    // Loads the Blueprint File
    @Override
    protected String getBlueprintDescriptor() {
        return "OSGI-INF/blueprint/reply.xml";
    }


    //Creates a route to request a message
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception{
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("timer://time?period=60000")
                        .setBody(simple("This is a Request Message " + "${date:now:HH:mm:ss.SSS MM/dd/yyyy}"))
                        .log("${body}")
                        .routeId("requestReply")
                        .inOut("activemq:queue:queueRequest")
                        .to("mock:out")
                        .log("${body}");
            }
        };
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        resultEndpoint = (MockEndpoint) context.getEndpoint("mock:out");
    }

    //Test if can reply a message follow the request reply pattern
    @Test
    public void testRouteWithTextMessage() throws Exception {
        String expectedBody = "This is a Response Message";

        resultEndpoint.setExpectedCount(1);
        resultEndpoint.message(0).body().contains(expectedBody);

        resultEndpoint.assertIsSatisfied();
    }

}
