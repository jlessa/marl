package au.com.marlo.trainning;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Created by jefferson on 30/09/16.
 */
public class ConsumerMessageListener implements MessageListener{
    private String consumer;

    public ConsumerMessageListener(String consumer){
        this.consumer = consumer;
    }

    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try{
            System.out.print("\n" + consumer + " received: " + textMessage.getText()+ "\n");
        }
        catch (Exception ex){
            System.err.println(ex.getMessage());
        }
    }
}
