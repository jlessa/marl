import au.com.marlo.trainning.connectionFactory.LocalConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.NamingException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Requestor implements Runnable {

    private Session session;
    private Connection connection;
    private Destination replyQueue;
    private MessageProducer requestProducer;
    private String receivedMessage,requestQueueName, replyQueueName;
    private MessageConsumer replyConsumer;

    public String getReceivedMessage() {
        return receivedMessage;
    }


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
        System.out.print("\n" + requestMessage.getText()+ "\n");
        requestProducer.send(requestMessage);
    }

    public String receiveSync() throws JMSException {
        Message msg = replyConsumer.receive();
        String replyMessageString = "";
        if (msg instanceof TextMessage) {
            TextMessage replyMessage = (TextMessage) msg;
            System.out.print("\nReceived Reply: ");
            System.out.print(replyMessage.getText()+ "\n");
            replyMessageString = replyMessage.getText();

        }
        return replyMessageString;
    }

    public void run() {
        try{

            connection.start();

            //this.send("This is a Request Message");

            receivedMessage = this.receiveSync();



        }catch (Exception ex){
            System.err.println(ex.getMessage());
        }

    }
}