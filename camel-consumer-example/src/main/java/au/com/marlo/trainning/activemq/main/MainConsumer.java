package au.com.marlo.trainning.activemq.main;


import au.com.marlo.trainning.activemq.consumer.QueueConsumer;
import au.com.marlo.trainning.activemq.consumer.Replier;
import au.com.marlo.trainning.activemq.consumer.Subscriber;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;


public class MainConsumer {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "admin",
                ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
        context.addComponent("test-jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("test-jms:queue:test-queue")
                        .to("stream:out");
            }
        });

        context.start();

        Thread.sleep(1000);
        context.stop();

    }


}