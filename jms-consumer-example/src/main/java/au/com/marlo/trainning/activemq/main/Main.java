package au.com.marlo.trainning.activemq.main;



import au.com.marlo.trainning.activemq.consumer.QueueConsumer;
import au.com.marlo.trainning.activemq.consumer.Replier;
import au.com.marlo.trainning.activemq.consumer.Subscriber;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;


public class Main {

    public static void main(String[] args) throws Exception {

        if(args.length > 0){
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            Connection connection = connectionFactory.createConnection();
            if(args[0].equalsIgnoreCase("topic")){
                Subscriber subscriber = new Subscriber();
                subscriber.create(connection,"singleSubscriber","topic","subscribe");
                subscriber.getGreeting(3000);
            }else if(args[0].equalsIgnoreCase("queue")){
                QueueConsumer queueConsumer = new QueueConsumer();
                connection.start();
                queueConsumer.create(connection,"queue");
                queueConsumer.getMessage(2000);
            }else if(args[0].equalsIgnoreCase("reply")){
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