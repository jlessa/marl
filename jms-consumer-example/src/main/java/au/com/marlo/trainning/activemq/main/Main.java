package au.com.marlo.trainning.activemq.main;



import au.com.marlo.trainning.activemq.consumer.Requestor;
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
                connection.start();
                Requestor requestor = Requestor.newRequestor(connection, "requestQueue","replyQueue");
                if(args[1].equalsIgnoreCase("send")){
                    requestor.setSendMessage("This is a Request Message");
                    thread(requestor,false);
                }
                else if(args[1].equalsIgnoreCase("receive")){
                    requestor.receiveSync();
                }
            }
        }
    }

    public  static void thread(Runnable runnable, boolean daemon){
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }
}