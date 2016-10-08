package au.com.marlo.trainning.activemq.main;


import javax.jms.ConnectionFactory;


import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsMessage;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;

import java.text.SimpleDateFormat;
import java.util.Date;


public class RequestReply extends RouteBuilder{

    public static void main(String[] args) throws Exception {

        try {
            final CamelContext context = new DefaultCamelContext();

            RequestReply route = new RequestReply();

            PropertiesComponent propertiesComponent = new PropertiesComponent();
            propertiesComponent.setLocation("classpath:properties.properties");
            context.addComponent("properties",propertiesComponent);

            context.addRoutes(route);
            context.start();

            Thread.sleep(4000);

        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void configure() throws Exception {
        from("timer://time?period=60000")
                .setBody(simple("This is a Request Message " + "${date:now:HH:mm:ss.SSS MM/dd/yyyy}"))
                .log("${body}")
                .routeId("requestReply")
                .inOut("properties:{{inOutEndpoint}}")
                .to("properties:{{endPoint}}")
                .log("${body}");
    }
}