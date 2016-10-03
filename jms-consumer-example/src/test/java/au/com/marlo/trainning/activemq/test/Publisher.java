package au.com.marlo.trainning.activemq.test;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Publisher {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(Publisher.class);

    private String clientId;
    private Connection connection;
    private Session session;
    private MessageProducer messageProducer;

    public void create(Connection connection, String clientId, String topicName) throws JMSException {
        this.connection = connection;
        this.clientId = clientId;

        connection.setClientID(clientId);

        // create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // create the Topic to which messages will be sent
        Topic topic = session.createTopic(topicName);

        // create a MessageProducer for sending messages
        messageProducer = session.createProducer(topic);
    }

    public void closeConnection() throws JMSException {
        connection.close();
    }

    public void send(String message) throws JMSException {

        // create a JMS TextMessage
        TextMessage textMessage = session.createTextMessage(message);

        // send the message to the topic destination
        messageProducer.send(textMessage);

        LOGGER.debug(clientId + ": sent message with text='{}'", message);
    }
}