package au.com.marlo.trainning.activemq.main;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.main.Main;


public class Subscriber extends RouteBuilder{

    private Main main;

    public void boot() throws Exception {
        this.main = new Main();
        this.main.addRouteBuilder(new Subscriber());
        PropertiesComponent propertiesComponent = new PropertiesComponent();
        propertiesComponent.setLocation("classpath:properties.properties");
        this.main.bind("properties", propertiesComponent);
        this.main.run();
    }


    public static void main(String[] args) throws Exception {

        try {
            Subscriber subscriber = new Subscriber();
            subscriber.boot();

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