package au.com.marlo.trainning.activemq.producer.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;
import javax.naming.NamingException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Requestor implements Runnable {

    private Session session;
    private Connection connection;
    private Destination replyQueue;
    private MessageProducer requestProducer;
    private String receivedMessage,requestQueueName, replyQueueName, sendMessage;
    private MessageConsumer replyConsumer;

    private static final Logger LOGGER = LoggerFactory
            .getLogger(Requestor.class);

    public String getReceivedMessage() {
        return receivedMessage;
    }

    public void setSendMessage(String message){ sendMessage = message; }

    public static Requestor newRequestor(Connection connection, String requestQueueName,
                                         String replyQueueName)
            throws JMSException, NamingException {

        Requestor requestor = new Requestor();
        requestor.initialize(connection, requestQueueName, replyQueueName);
        return requestor;
    }

    protected void initialize(Connection connection, String requestQueueName,
                              String replyQueueName)
            throws NamingException, JMSException {

        this.connection = connection;
        this.requestQueueName = requestQueueName;
        this.replyQueueName = replyQueueName;

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination requestQueue = this.session.createQueue(requestQueueName);
        replyQueue = this.session.createQueue(replyQueueName);

        requestProducer = session.createProducer(requestQueue);
        replyConsumer = session.createConsumer(replyQueue);

        receivedMessage = "";
    }

    public void send(String message) throws JMSException {
        TextMessage requestMessage = session.createTextMessage();
        String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS MM/dd/yyyy").format(new Date());
        requestMessage.setText( message + " " + timeStamp);
        requestMessage.setJMSReplyTo(replyQueue);
        LOGGER.info("\n" + requestMessage.getText()+ "\n");
        requestProducer.send(requestMessage);
    }

    public String receiveSync() throws JMSException {
        Message msg = replyConsumer.receive();
        String replyMessageString = "";
        if (msg instanceof TextMessage) {
            TextMessage replyMessage = (TextMessage) msg;
            LOGGER.info("\nReceived Reply: " + replyMessage.getText()+ "\n");
            replyMessageString = replyMessage.getText();

        }
        return replyMessageString;
    }

    public void run() {
        try {
            send(sendMessage);
        }catch (Exception ex){
            System.err.println(ex.getMessage());
        }

    }
}