package au.com.marlo.trainning.activemq.test;



import au.com.marlo.trainning.activemq.consumer.QueueConsumer;
import au.com.marlo.trainning.activemq.test.util.QueueProducer;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import static org.junit.Assert.*;


//Test class to check if the QueueConsumer class can get a message from a queue
public class QueueConsumerTest {

    //Method creates a connection to an embedded queue
    private Connection createConnection() throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost?broker.persistent=false");
        return connectionFactory.createConnection();
    }

    //Method to test if getMessage method on the QueueConsumer class gets a message from an specific queue
    @Test
    public void testSendQueueProducer() throws JMSException, InterruptedException {
        String message = "Test Message to Queue";
        Connection connectionProducer = createConnection();

        QueueProducer queueProducer = new QueueProducer();
        connectionProducer.start();
        queueProducer.create(connectionProducer,"queue");
        queueProducer.send(message);

        Thread.sleep(3000);

        Connection connectionConsumer = createConnection();
        connectionConsumer.start();
        QueueConsumer queueConsumer = new QueueConsumer();
        queueConsumer.create(connectionConsumer,"queue");
        assertTrue(queueConsumer.getMessage(2000).contains(message));

    }

}