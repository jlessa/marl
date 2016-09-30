import au.com.marlo.trainning.Replier;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * Hello world!
 */
public class Main {

    public static void main(String[] args) throws Exception {

        thread(new Requestor(),false);
        thread(new Replier(),false);

        Thread.sleep(3000);

        thread(new TopicRequester(),false);

    }

    public  static void thread(Runnable runnable, boolean daemon){
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }
}