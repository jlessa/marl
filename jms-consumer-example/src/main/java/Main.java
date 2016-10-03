import au.com.marlo.trainning.Replier;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import javax.jms.*;


public class Main {

    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactoryRequestor = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connectionRequestor = connectionFactoryRequestor.createConnection();

        Requestor requestor = Requestor.newRequestor(connectionRequestor, "requestQueue","replyQueue");

        thread(requestor,false);


    }

    public  static void thread(Runnable runnable, boolean daemon){
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }
}