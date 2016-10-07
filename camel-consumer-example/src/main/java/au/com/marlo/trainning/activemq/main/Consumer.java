package au.com.marlo.trainning.activemq.main;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class Consumer extends RouteBuilder{

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        PropertiesComponent propertiesComponent = new PropertiesComponent();
        propertiesComponent.setLocation("classpath:properties.properties");
        context.addComponent("properties",propertiesComponent);

        context.addRoutes(new Consumer());
        context.start();
        Thread.sleep(1000);
        context.stop();
    }

    @Override
    public void configure() throws Exception {
        from("properties:{{queueEndPoint}}")
                .to("stream:out");
    }
}