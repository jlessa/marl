package au.com.marlo.trainning.activemq.main;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;


public class MainSubscriber {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "admin",
                ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
        context.addComponent("test-jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        ProducerTemplate template = context.createProducerTemplate();
        context.start();

        template.sendBody("test-jms:topic:test-topic", "Test Message");

        Thread.sleep(1000);
        context.stop();


    }


}