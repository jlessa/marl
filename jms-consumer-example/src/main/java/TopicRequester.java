import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.*;

import au.com.marlo.trainning.ConsumerMessageListener;

import org.apache.activemq.ActiveMQConnectionFactory;


public class TopicRequester implements Runnable {

    public void run() {
        Connection connection = null;
        try {
            // Producer
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    "tcp://localhost:61616");
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic("Topic");

            // Consumer1 subscribes to customerTopic
            MessageConsumer consumer1 = session.createConsumer(topic);
            consumer1.setMessageListener(new ConsumerMessageListener("Topic Consumer 1"));

            // Consumer2 subscribes to customerTopic
            MessageConsumer consumer2 = session.createConsumer(topic);
            consumer2.setMessageListener(new ConsumerMessageListener("Topic Consumer 2"));

            connection.start();

            String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS MM/dd/yyyy").format(new Date());
            String payload = "This is a Request Message" + timeStamp.toString();
            Message msg = session.createTextMessage(payload);
            MessageProducer producer = session.createProducer(topic);
            producer.send(msg);

            Thread.sleep(3000);
            session.close();
        }catch (Exception ex){
            System.err.println(ex.getMessage());
        }
        finally {
            try{
                if (connection != null) {
                    connection.close();
                }
            }catch (JMSException ex){
                System.err.println(ex.getMessage());
            }

        }
    }
}