package au.com.gbstore.bean;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.camel.Body;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;

import au.com.gbstore.messages.ordermanager.ordermanagermessage.v1.SplitterSupplierMessage;
import au.com.gbstore.types.ordermanager.v1.SupplierOrderType;

/**Build a message to send to CBR.
 * 
 * @author lteixeira
 *
 */
public class SplitterBean {

	/**Splits the message.
	 * and sets the suplierId as header
	 * return a list of messages
	 * @param body - the original message
	 * @return list of messages
	 * @throws JAXBException
	 */
	public List<Message> splitMessage(@Body String body) throws JAXBException{
		List<Message> supOrders = new ArrayList<Message>();
		
		SplitterSupplierMessage splmsg = unmarshaller(body);
		
		for(SupplierOrderType spOrder : splmsg.getSupplierOrder()){
			DefaultMessage message = new DefaultMessage();
			message.setHeader("supplierId", spOrder.getSupplier().getSupplierId());
			message.setBody(marshaller(spOrder));
			supOrders.add(message);
		}
		return supOrders;
	}
	
	/** Marshal.
	 * @param supOrder
	 * @return String
	 * @throws JAXBException
	 */
	public String marshaller(SupplierOrderType supOrder) throws JAXBException{
		SplitterSupplierMessage message = new SplitterSupplierMessage();
		message.getSupplierOrder().add(supOrder);
		
		StringWriter writer = new StringWriter();
		
		JAXBContext jxb = JAXBContext.newInstance(SplitterSupplierMessage.class);
		Marshaller m = jxb.createMarshaller();
		m.marshal(message, writer);
		return writer.toString();
	}
	
	
	/**Unmarshal.
	 * @param body
	 * @return SppliterSupplierMessage
	 * @throws JAXBException
	 */
	public SplitterSupplierMessage unmarshaller(String body) throws JAXBException{
		StringReader reader = new StringReader(body);
		JAXBContext jxb = JAXBContext.newInstance(SplitterSupplierMessage.class);
		Unmarshaller u = jxb.createUnmarshaller();
		return (SplitterSupplierMessage) u.unmarshal(reader);
	}
	
}
