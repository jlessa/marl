package au.com.marlo.trainning.activemq.main;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;

import au.com.marlo.trainning.activemq.producer.Publisher;
import au.com.marlo.trainning.activemq.producer.QueueProducer;
import au.com.marlo.trainning.activemq.producer.Requestor;
import org.apache.activemq.ActiveMQConnectionFactory;


public class Main {

    public static void main(String[] args) throws Exception {
        if(args.length > 0){
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            Connection connection = connectionFactory.createConnection();
            if(args[0].equalsIgnoreCase("topic")){
                Publisher publisher = new Publisher();
                publisher.create(connection,"publisher-topic","topic");
                publisher.send("This is a Topic Message ");
            }else if(args[0].equalsIgnoreCase("queue")){
                connection.start();
                QueueProducer queueProducer = new QueueProducer();
                queueProducer.create(connection ,"queue");
                queueProducer.send("This is a Request Message");
            }else if(args[0].equalsIgnoreCase("request")){
                connection.start();
                Requestor requestor = Requestor.newRequestor(connection, "requestQueue","replyQueue");
                requestor.setSendMessage("This is a Request Message");
                thread(requestor,false);
            }
        }
    }

    public  static void thread(Runnable runnable, boolean daemon){
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }
}