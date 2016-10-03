/**
 * Created by jefferson on 02/10/16.
 */


import au.com.marlo.trainning.activemq.test.Replier;

import au.com.marlo.trainning.activemq.consumer.Requestor;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Assert;
import org.junit.Test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.naming.NamingException;

public class RequestorTest {

    public  static void thread(Runnable runnable, boolean daemon){
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

    @Test
    public void  TestSend() throws JMSException, NamingException, InterruptedException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Requestor requestor = Requestor.newRequestor(connection, "requestQueue","replyQueue");
        Replier replier = Replier.newReplier(connection,"requestQueue");

        thread(requestor,false);
        Thread.sleep(3000);
        thread(replier,false);
        Thread.sleep(3000);

        Assert.assertNotNull(requestor.getReceivedMessage());
    }
}
