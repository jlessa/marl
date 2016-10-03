package au.com.marlo.trainning.activemq.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;


public class Replier implements MessageListener, Runnable {

    private Session session;
    private Connection connection;
    private String requestQueue;
    private String responseMessage;
    private String receivedMessage;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(Replier.class);

    public String getResponseMessage() {
        return responseMessage;
    }

    public String getReceivedMessage() {
        return receivedMessage;
    }

    private Replier() {
        super();
    }

    public static Replier newReplier(Connection connection, String requestQueueName)
            throws JMSException, NamingException {

        Replier replier = new Replier();
        replier.initialize(connection, requestQueueName);
        return replier;
    }

    protected void initialize(Connection connection, String requestQueueName)
            throws NamingException, JMSException {

        this.connection = connection;
        this.requestQueue = requestQueueName;
        this.receivedMessage = "";
        this.responseMessage = "";

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination requestQueue = this.session.createQueue(requestQueueName);

        MessageConsumer requestConsumer = session.createConsumer(requestQueue);
        MessageListener listener = this;
        requestConsumer.setMessageListener(listener);

    }

    //Implemented method to reply a request
    public void onMessage(Message message) {

        try {
            if ((message instanceof TextMessage) && (message.getJMSReplyTo() != null)) {
                TextMessage requestMessage = (TextMessage) message;

                this.receivedMessage = requestMessage.getText();

                LOGGER.info("\nReceived Request: " + requestMessage.getText());

                Destination replyDestination = message.getJMSReplyTo();
                MessageProducer replyProducer = session.createProducer(replyDestination);

                TextMessage replyMessage = session.createTextMessage();
                String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS MM/dd/yyyy").format(new Date());

                replyMessage.setText(" This is a Response Message " + timeStamp);

                this.responseMessage = replyMessage.getText();

                replyMessage.setJMSCorrelationID(requestMessage.getJMSMessageID());
                replyProducer.send(replyMessage);

                LOGGER.info("\nResponse: " + replyMessage.getText() + "\n");

            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    public void run(){
        try {
            initialize(connection, this.requestQueue);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}