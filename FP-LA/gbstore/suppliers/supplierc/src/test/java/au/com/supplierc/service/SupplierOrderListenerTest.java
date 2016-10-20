package au.com.supplierc.service;

import static org.junit.Assert.*;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.messaging.support.ErrorMessage;

import au.com.supplierc.messages.orderrequest.v1.SupplierCMessage;
import au.com.supplierc.messages.orderresponse.v1.SupplierCTranslatorMessage;
import au.com.supplierc.types.v1.ItemSupplierOrderListType;
import au.com.supplierc.types.v1.ItemSupplierOrderType;
import au.com.supplierc.types.v1.SupplierOrderRequestType;

public class SupplierOrderListenerTest {

/**Message that will be received by the queue
 * 
 */
String message;
	
	/**Create a message that is supposed to be received in queue
	 * @throws JAXBException
	 */
	@Before
	public void setup() throws JAXBException{
		SupplierCMessage spcmsg  = new SupplierCMessage();
		SupplierOrderRequestType request = new SupplierOrderRequestType();
		ItemSupplierOrderListType list = new ItemSupplierOrderListType();
		ItemSupplierOrderType order = new ItemSupplierOrderType();
		order.setProductId("1");
		order.setQuantity(BigInteger.ONE);
		list.getItemOrder().add(order);
		request.setItemSupplierOrderList(list);
		request.setSupplierOrderId(BigInteger.ONE);
		spcmsg.setSupplierOrderRequest(request);
		message = marshal(spcmsg);
	}
	
	/**Test the service's response message
	 * It must not be null
	 * @throws Exception 
	 * @throws ErrorMessage
	 */
	@Test
	public void testMessage() throws Exception{
		SupplierOrderListener service = new SupplierOrderListener();
		String respmsg = service.handleMessage(message);
		assertFalse(respmsg.isEmpty());
	}
	/**The orderNumber in service's response cant be empty
	 * @throws Exception 
	 * @throws ErrorMessage 
	 */
	@Test
	public void testOrderNumberInMessage() throws Exception{
		SupplierOrderListener service = new SupplierOrderListener();
		String respmsg = service.handleMessage(message);
		SupplierCTranslatorMessage response = unmarshal(respmsg);
		assertNotNull(response.getSupplierOrderResponse().getOrderNumber());
	}
	
	/**When is empty it cant be processed
	 * The service will send an answer saying the message is empty
	 * @throws Exception 
	 * @throws ErrorMessage
	 */
	@Test
	public void testErrorMessage() throws Exception{
		SupplierOrderListener service = new SupplierOrderListener();
		String respmsg = service.handleMessage("");
		assertTrue(respmsg.contains("Your message is empty"));
	}
	
	/** Unmarshal string to SupplierCTranslatorResponse object.
	 */
	private SupplierCTranslatorMessage unmarshal(String message) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(SupplierCTranslatorMessage.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		StringReader stringReader = new StringReader(message);
		return (SupplierCTranslatorMessage) unmarshaller.unmarshal(stringReader);
	}

	/**
	 * Marshal SupplierCMessage object to string.
	 */
	private String marshal(SupplierCMessage response) throws JAXBException {
		StringWriter writer = new StringWriter();
		JAXBContext jaxbContext = JAXBContext.newInstance(SupplierCMessage.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(response, writer);
		return writer.toString();
	}
}
