package au.com.gbstore.processor;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import au.com.gbstore.messages.legacysystem.storeorderresponse.v1.StoreOrderResponse;



/**
 * @author lteixeira
 *Sends a response message
 */
public class ResponseMessageProcessor implements Processor{
	
	/** @throws Exception
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 * 
	 * Creates a StoreOrderResponse object
	 * Marshal to xml format
	 * and send it back
	 */
	public void process(Exchange exchange) throws Exception {
		StringWriter writer = new StringWriter();
		StoreOrderResponse response = new StoreOrderResponse();
		response.setMessage("Your request has been received");
		JAXBContext jxb = JAXBContext.newInstance(StoreOrderResponse.class);
		Marshaller m = jxb.createMarshaller();
		m.marshal(response, writer);
		exchange.getOut().setBody(writer.toString());
	}

}
