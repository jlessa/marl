package au.com.marlo.trainning;
import javax.jms.Connection;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

/**
 * Hello world!
 */
public class Main {

    public static void main(String[] args) throws Exception {
    	BrokerService broker = new BrokerService();
        broker.setPersistent(false);
        broker.setUseJmx(false);
        broker.addConnector("tcp://localhost:6166");
        broker.start();
        
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:6166");

        // Create a Connection
        Connection connection = connectionFactory.createConnection();
        connection.start();

        Replier replier = Replier.newReplier(connection,"requestQueue");
        
        Thread.sleep(300);
        
        //connection.close();
        //broker.stop();
        
    }

}