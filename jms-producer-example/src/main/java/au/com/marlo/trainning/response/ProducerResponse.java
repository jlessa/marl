package au.com.marlo.trainning.response;

/**
 * Created by jefferson on 02/10/16.
 */
public class ProducerResponse {
    private String requestMessage;
    private String replyMessage;

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String replyMessage) {
        this.replyMessage = replyMessage;
    }
}
