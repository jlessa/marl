package au.com.marlo.trainning.activemq.main;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;


public class MainPublisher {

    public static void main(String[] args) throws Exception {

        try {
            final CamelContext context = new DefaultCamelContext();
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("admin", "admin",
                    ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
            context.addComponent("test-jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

            context.addRoutes(new RouteBuilder() {
                @Override
                public void configure() throws Exception {
                    from("test-jms:topic:test-topic")
                            .routeId("topic1")
                            .to("stream:out");
                    from("test-jms:topic:test-topic")
                            .routeId("topic2")
                            .to("stream:out");
                    from("test-jms:topic:test-topic")
                            .routeId("topic3")
                            .to("stream:out");
                }
            });


            context.start();

            Thread.sleep(4000);
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