package au.com.marlo.trainning.activemq.main;



import au.com.marlo.trainning.activemq.consumer.QueueConsumer;
import au.com.marlo.trainning.activemq.consumer.Replier;
import au.com.marlo.trainning.activemq.consumer.Subscriber;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Main {

    public static void main(String[] args) throws Exception {

        final CamelContext context = new DefaultCamelContext();

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "admin",
                ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
        context.addComponent("test-jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("test-jms:queue:queueRequest")
                        .log("${body}")
                        .process(new Processor() {
                            @Override
                            public void process(Exchange exchange) throws Exception {
                                String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS MM/dd/yyyy").format(new Date());
                                final String message = "This is a Response Message " + timeStamp;
                                exchange.getIn().setBody(message);
                            }
                        })
                ;

            }
        });
        context.start();


        Thread.sleep(60000);
        //context.stop();

    }


}