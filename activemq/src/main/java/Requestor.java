import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.NamingException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Requestor implements Runnable {

    private Session session;
    private Destination replyQueue;
    private MessageProducer requestProducer;
    private MessageConsumer replyConsumer;

    public Requestor() {
        super();
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

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination requestQueue = this.session.createQueue(requestQueueName);
        replyQueue = this.session.createQueue(replyQueueName);

        requestProducer = session.createProducer(requestQueue);
        replyConsumer = session.createConsumer(replyQueue);
    }

    public void send() throws JMSException {
        TextMessage requestMessage = session.createTextMessage();
        String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS MM/dd/yyyy").format(new Date());
        requestMessage.setText("This is a Request Message " + timeStamp);
        requestMessage.setJMSReplyTo(replyQueue);
        System.out.print("\n" + requestMessage.getText()+ "\n");
        requestProducer.send(requestMessage);
    }

    public void receiveSync() throws JMSException {
        Message msg = replyConsumer.receive();
        if (msg instanceof TextMessage) {
            TextMessage replyMessage = (TextMessage) msg;
            System.out.print("\nReceived Reply: ");
            System.out.print(replyMessage.getText()+ "\n");
        }
    }

    public void run() {
        try{
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

            // Create a Connection
            Connection connection = connectionFactory.createConnection();
            connection.start();

            this.initialize(connection, "requestQueue", "replyQueue");

            this.send();

            Thread.sleep(3000);
            this.receiveSync();



        }catch (Exception ex){
            System.err.println(ex.getMessage());
        }

    }
}