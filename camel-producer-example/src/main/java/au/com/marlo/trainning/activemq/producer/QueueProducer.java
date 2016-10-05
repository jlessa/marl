package au.com.marlo.trainning.activemq.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QueueProducer {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(QueueProducer.class);


    private Connection connection;
    private Session session;
    private MessageProducer messageProducer;
    private String queueName;

    public void create(Connection connection, String queueName) throws JMSException {
        this.connection = connection;
        this.queueName = queueName;

        // create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // create the Topic to which messages will be sent
        Destination queue = session.createQueue(queueName);

        // create a MessageProducer for sending messages
        messageProducer = session.createProducer(queue);
    }

    public void closeConnection() throws JMSException {
        connection.close();
    }

    public void send(String message) throws JMSException {

        String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS MM/dd/yyyy").format(new Date());

        // create a JMS TextMessage
        TextMessage textMessage = session.createTextMessage(message + " " + timeStamp);

        // send the message to the topic destination
        messageProducer.send(textMessage);

        LOGGER.info("Send message: " +  textMessage.getText());
    }
}