package au.com.marlo.trainning.activemq.main;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.main.Main;


public class Producer extends RouteBuilder{

    private Main main;

    public void boot() throws Exception {
        this.main = new Main();
        this.main.addRouteBuilder(new Producer());
        PropertiesComponent propertiesComponent = new PropertiesComponent();
        propertiesComponent.setLocation("classpath:properties.properties");
        this.main.bind("properties", propertiesComponent);
        this.main.run();
    }

    public static void main(String[] args) throws Exception {
        try {
            Producer producer = new Producer();
            producer.boot();
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