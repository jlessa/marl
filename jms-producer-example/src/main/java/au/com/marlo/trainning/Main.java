package au.com.marlo.trainning;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

/**
 * Hello world!
 */
public class Main {

    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactoryReplier = new ActiveMQConnectionFactory("tcp://localhost:61616");
        Connection connectionReplier = connectionFactoryReplier.createConnection();

        Replier replier = Replier.newReplier(connectionReplier,"requestQueue");

        thread(replier,false);
    }

    public  static void thread(Runnable runnable, boolean daemon){
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }
}