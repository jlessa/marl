package au.com.marlo.trainning.activemq.producer.test;

import static org.apache.activemq.camel.component.ActiveMQComponent.activeMQComponent;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;

import au.com.marlo.trainning.activemq.main.Subscriber;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsEndpoint;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jms.connection.JmsTransactionManager;

/*
** Class to test if the Subscriber send and receive an specific messages to all subscribers
*/
public class SubscriberTest extends CamelTestSupport {

    protected MockEndpoint resultEndpoint;
    private String topicPath = "activemq:topic:test-topic";

    protected void sendExchange(final Object expectedBody) {
        template.sendBodyAndHeader(topicPath, expectedBody, "ReplyTest", "header");
    }

    //create a camel context to set the queue embeded
    protected CamelContext createCamelContext() throws Exception {
        CamelContext camelContext = super.createCamelContext();
        camelContext.addComponent("activemq", activeMQComponent("vm://localhost?broker.persistent=false"));

        PropertiesComponent propertiesComponent = new PropertiesComponent();
        propertiesComponent.setLocation("classpath:testproperties.properties");
        camelContext.addComponent("properties",propertiesComponent);


        return camelContext;
    }

    protected RouteBuilder createRouteBuilder() throws Exception {
        return new Subscriber();
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