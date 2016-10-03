package au.com.marlo.trainning.activemq.producer.test;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Subscriber {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(Subscriber.class);

    private static final String NO_GREETING = "no greeting";

    private String clientId;
    private String subscritionName;
    private Connection connection;
    private Session session;
    private MessageConsumer messageConsumer;

    public void create(Connection connection, String clientId, String topicName, String subscritionName) throws JMSException {
        this.clientId = clientId;
        this.connection = connection;
        this.subscritionName = subscritionName;

        connection.setClientID(clientId);

        // create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // create the Topic from which messages will be received
        Topic topic = session.createTopic(topicName);

        // create a MessageConsumer for receiving messages
        messageConsumer = session.createDurableSubscriber(topic,subscritionName);

        // start the connection in order to receive messages
        connection.start();
    }

    public void removeSubscriber() throws JMSException {
        messageConsumer.close();
        session.unsubscribe(subscritionName);

    }

    public void closeConnection() throws JMSException {
        connection.close();
    }

    public String getGreeting(int timeout) throws JMSException {

        String greeting = NO_GREETING;

        // read a message from the topic destination
        Message message = messageConsumer.receive(timeout);

        // check if a message was received
        if (message != null) {
            // cast the message to the correct type
            TextMessage textMessage = (TextMessage) message;

            // retrieve the message content
            String text = textMessage.getText();
            LOGGER.debug(clientId + ": received message with text='{}'", text);

            // create greeting
            greeting = text;
        } else {
            LOGGER.debug(clientId + ": no message received");
        }

        LOGGER.info("greeting={}", greeting);
        return greeting;
    }
}