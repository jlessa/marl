package au.com.marlo.trainning.activemq.main;

import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import java.text.SimpleDateFormat;
import java.util.Date;


public class RequestReply extends RouteBuilder{

    public static void main(String[] args) throws Exception {
        final CamelContext context = new DefaultCamelContext();
        context.addRoutes(new RequestReply());
        context.start();
        Thread.sleep(6000);
        context.stop();
    }

    @Override
    public void configure() throws Exception {
        from("activemq:queue:queueRequest")
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
}