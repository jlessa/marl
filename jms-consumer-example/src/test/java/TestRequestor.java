/**
 * Created by jefferson on 02/10/16.
 */

import au.com.marlo.trainning.Replier;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.naming.NamingException;

public class TestRequestor {

    public  static void thread(Runnable runnable, boolean daemon){
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

    @Test
    public void  TestSend() throws JMSException, NamingException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        Connection connection = connectionFactory.createConnection();

        Requestor requestor = Requestor.newRequestor(connection, "requestQueue","replyQueue");

        thread(requestor,false);

        System.out.println("Message: "+ requestor.getReceivedMessage());
    }
}
