package au.com.marlo.trainning.activemq.producer.test;

import au.com.marlo.trainning.activemq.producer.Requestor;
import au.com.marlo.trainning.activemq.producer.test.util.Replier;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Assert;
import org.junit.Test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.naming.NamingException;


/*
** Class to test if the Requestor send and receive an specific message
*/
public class RequestorTest {

    public  static void thread(Runnable runnable, boolean daemon){
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }


    /*
     ** Test if the Requestor send and receive an specific message
     */
    @Test
    public void  TestSend() throws JMSException, NamingException, InterruptedException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Requestor requestor = Requestor.newRequestor(connection, "requestQueue","replyQueue");
        Replier replier = Replier.newReplier(connection,"requestQueue");

        requestor.setSendMessage("Test Message");

        thread(requestor,false);
        Thread.sleep(3000);
        thread(replier,false);
        Thread.sleep(3000);

        Assert.assertNotNull(requestor.getReceivedMessage());
    }
}
