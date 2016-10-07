package au.com.marlo.trainning.activemq.main;


import javax.jms.ConnectionFactory;

import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsMessage;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;


public class Producer extends RouteBuilder{

    public static void main(String[] args) throws Exception {

        try {

            final CamelContext context = new DefaultCamelContext();
            PropertiesComponent propertiesComponent = new PropertiesComponent();
            propertiesComponent.setLocation("classpath:properties.properties");
            context.addComponent("properties",propertiesComponent);

            context.addRoutes(new Producer());

            context.start();
            Thread.sleep(1000);
            context.stop();

        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }

    @Override
    public void configure() throws Exception {
        from("timer://time?period=60000")
                .setBody(simple("This is a Request Message " + "${date:now:HH:mm:ss.SSS MM/dd/yyyy}"))
                .setExchangePattern(ExchangePattern.InOnly)
                .log("${body}")
                .to("properties:{{queueEndPoint}}");
    }
}