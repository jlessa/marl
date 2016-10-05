package au.com.marlo.trainning.activemq.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class QueueConsumer {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(QueueConsumer.class);

    private static final String NO_MESSAGE = "no message";

    private Connection connection;
    private Session session;
    private MessageConsumer messageConsumer;
    private String queueName;

    public void create(Connection connection, String queueName) throws JMSException {

        this.connection = connection;
        this.queueName = queueName;

        // create a Session
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        // create the Queue
        Destination queue = session.createQueue(queueName);

        // create a MessageConsumer for receiving messages
        messageConsumer = session.createConsumer(queue);

        // start the connection in order to receive messages
        connection.start();
    }


    public void closeConnection() throws JMSException {
        connection.close();
    }

    public String getMessage (int timeout) throws JMSException {

        String messageString = NO_MESSAGE;

        // read a message from the topic destination
        Message message = messageConsumer.receive(timeout);

        // check if a message was received
        if (message != null) {
            // cast the message to the correct type
            TextMessage textMessage = (TextMessage) message;

            // retrieve the message content
            String text = textMessage.getText();
            LOGGER.info("Request: " + text);

            messageString = text;
        } else {
            LOGGER.info(NO_MESSAGE);
        }

        return messageString;
    }
}