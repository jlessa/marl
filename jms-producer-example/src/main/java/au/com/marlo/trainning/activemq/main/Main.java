package au.com.marlo.trainning.activemq.main;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;

import au.com.marlo.trainning.activemq.producer.Publisher;
import au.com.marlo.trainning.activemq.producer.Replier;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws Exception {
        if(args.length > 0){
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            Connection connection = connectionFactory.createConnection();
            if(args[0].equalsIgnoreCase("topic")){
                Publisher publisher = new Publisher();
                publisher.create(connection,"publisher-topic","topic");
                String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS MM/dd/yyyy").format(new Date());
                publisher.send("This is a Topic Message " + timeStamp);
            }else if(args[0].equalsIgnoreCase("queue")){
                connection.start();
                Replier replier = Replier.newReplier(connection,"requestQueue");
                thread(replier,false);
            }
        }
    }

    public  static void thread(Runnable runnable, boolean daemon){
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }
}