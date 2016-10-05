package au.com.marlo.trainning.activemq.main;


import javax.jms.ConnectionFactory;

import au.com.marlo.trainning.activemq.producer.QueueProducer;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsMessage;
import org.apache.camel.impl.DefaultCamelContext;

import java.text.SimpleDateFormat;
import java.util.Date;


public class Main {

    public static void main(String[] args) throws Exception {

        try {
            final CamelContext context = new DefaultCamelContext();
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "admin",
                    ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
            context.addComponent("test-jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));


            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {

                    from("timer://time?period=60000")
                            .setBody(simple("This is a Request Message " + "${date:now:HH:mm:ss.SSS MM/dd/yyyy}"))
                            //.setExchangePattern(ExchangePattern.InOut)
                            .log("${body}")
                            .inOut("test-jms:queue:queueRequest")
                            //.to("stream:out")
                            //.to("test-jms:queue:queueRequest")
                            .log("${body}")
                            .end();

                }
            });

            context.start();

            Thread.sleep(90000);
            //context.stop();
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    public  static void thread(Runnable runnable, boolean daemon){
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }
}