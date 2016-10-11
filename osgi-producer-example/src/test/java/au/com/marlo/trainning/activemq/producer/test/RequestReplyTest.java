package au.com.marlo.trainning.activemq.producer.test;

import au.com.marlo.trainning.activemq.main.Producer;
import au.com.marlo.trainning.activemq.main.RequestReply;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Before;
import org.junit.Test;
import javax.jms.ConnectionFactory;


import java.text.SimpleDateFormat;
import java.util.Date;

import static org.apache.activemq.camel.component.ActiveMQComponent.activeMQComponent;
import static org.apache.camel.component.jms.JmsComponent.jmsComponentAutoAcknowledge;

/*
** Class to test the Request Reply Pattern
*/
public class RequestReplyTest extends CamelTestSupport{


    protected MockEndpoint resultEndpoint;
    protected MockEndpoint replyEndpoint;

    //Create a camel context to set the embedded activemq
    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        camelContext.addComponent("activemq", activeMQComponent("vm://localhost?broker.persistent=false"));

        PropertiesComponent propertiesComponent = new PropertiesComponent();
        propertiesComponent.setLocation("classpath:testproperties.properties");
        camelContext.addComponent("properties",propertiesComponent);

        return camelContext;
    }

    //Create Route
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RequestReply();
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
