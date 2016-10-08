package au.com.marlo.trainning.activemq.main;


import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;


public class Subscriber extends RouteBuilder{

    public static void main(String[] args) throws Exception {

        try {
            final CamelContext context = new DefaultCamelContext();

            PropertiesComponent propertiesComponent = new PropertiesComponent();
            propertiesComponent.setLocation("classpath:properties.properties");
            context.addComponent("properties",propertiesComponent);

            context.addRoutes(new Subscriber());

            context.start();

            Thread.sleep(4000);

        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void configure() throws Exception {
        from("properties:{{topicEndPoint}}")
            .routeId("topic1")
            .to("properties:{{endPoint}}");

        from("properties:{{topicEndPoint}}")
                .routeId("topic2")
                .to("properties:{{endPoint}}");

        from("properties:{{topicEndPoint}}")
                .routeId("topic3")
                .to("properties:{{endPoint}}");

    }
}