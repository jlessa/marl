package au.com.marlo.trainning;
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

import org.apache.activemq.ActiveMQConnectionFactory;

public class Replier implements MessageListener, Runnable {

    private Session session;    

    public Replier() {
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

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination requestQueue = this.session.createQueue(requestQueueName);

        MessageConsumer requestConsumer = session.createConsumer(requestQueue);
        MessageListener listener = this;
        requestConsumer.setMessageListener(listener);

    }

    public void onMessage(Message message) {
        try {
            if ((message instanceof TextMessage) && (message.getJMSReplyTo() != null)) {
                TextMessage requestMessage = (TextMessage) message;

                System.out.print("\nReceived Request: ");
                System.out.print(requestMessage.getText());

                String contents = requestMessage.getText();
                Destination replyDestination = message.getJMSReplyTo();
                MessageProducer replyProducer = session.createProducer(replyDestination);

                TextMessage replyMessage = session.createTextMessage();
                String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS MM/dd/yyyy").format(new Date());
                replyMessage.setText(" This is a Response Message " + timeStamp);
                replyMessage.setJMSCorrelationID(requestMessage.getJMSMessageID());
                replyProducer.send(replyMessage);

                System.out.print("\nResponse: ");
                System.out.print(replyMessage.getText() + "\n");
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

	public void run(){
		// TODO Auto-generated method stub
		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("vm://localhost");

        // Create a Connection
        Connection connection;
		try {
			connection = connectionFactory.createConnection();
			connection.start();
	        initialize(connection, "requestQueue");
		} catch (Exception e) {	
			e.printStackTrace();
		}
        
		
	}
}