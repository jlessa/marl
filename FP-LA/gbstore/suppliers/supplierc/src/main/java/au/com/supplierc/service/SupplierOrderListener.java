package au.com.supplierc.service;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.supplier.persistence.dao.OrderNumberGenerator;
import au.com.supplierc.messages.errormessage.v1.SupplierCErrorMessage;
import au.com.supplierc.messages.orderrequest.v1.SupplierCMessage;
import au.com.supplierc.messages.orderresponse.v1.SupplierCTranslatorMessage;
import au.com.supplierc.types.v1.ErrorMessageType;
import au.com.supplierc.types.v1.SupplierOrderResponseType;

public class SupplierOrderListener {

	private Log log = LogFactory.getLog(getClass());

	/** its a method that listens to the queue.
	 * get the message from SupplierCTranslator
	 * get the OrderNumber and generate a response
	 * and send it back to the queue
	 * @param message
	 * @return
	 * @throws Exception
	 */
	public String handleMessage(String message) throws Exception {
		if(message.isEmpty()){
			SupplierCErrorMessage errormsg = new SupplierCErrorMessage();
			ErrorMessageType msg = new ErrorMessageType();
			msg.setFaultCode("1230");
			msg.setFaultString("Your message is empty");
			log.info("SupplierC: Message cant be processed - FaultCode: " + 
					msg.getFaultCode() + " FaultString: " + msg.getFaultString());
			errormsg.setErrorMessage(msg);
			return marshalError(errormsg);
		}
		log.info("SupplierC received this message: " + message.toString());
		
		String response = getResponse(message);
		
		log.info("SupplierC will send this message to SupplierCTranslator: " + response);
		return response;
	}

	/**This method will build the response with SupplierOrderId and a orderNumber.
	 * 
	 * @param req
	 * @return
	 * @throws Exception
	 */
	private String getResponse(String message) throws Exception {
		SupplierCMessage request = unmarshal(message);
		SupplierOrderResponseType sor = new SupplierOrderResponseType();
		sor.setSupplierOrderId(request.getSupplierOrderRequest().getSupplierOrderId());
		sor.setOrderNumber(generateOrderNumber());
		log.info("SupC: Getting a supplier order response: id = " + sor.getSupplierOrderId() + " OrderNumber = "
				+ sor.getOrderNumber());
		SupplierCTranslatorMessage spctm = new SupplierCTranslatorMessage();
		spctm.setSupplierOrderResponse(sor);
		return marshal(spctm);
	}
	
	/**Generate a orderNumber calling persistence-module
	 * @return
	 * @throws Exception
	 */
	private BigInteger generateOrderNumber() throws Exception {
		OrderNumberGenerator generator = new OrderNumberGenerator();
		return BigInteger.valueOf(generator.generateNewOrderNumber());
	}

	/** Unmarshal string to SupplieOrderRequest object.
	 */
	private SupplierCMessage unmarshal(String message) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(SupplierCMessage.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		StringReader stringReader = new StringReader(message);
		return (SupplierCMessage) unmarshaller.unmarshal(stringReader);
	}

	/**
	 * Marshal SupplierOrderResponse object to string.
	 */
	private String marshal(SupplierCTranslatorMessage response) throws JAXBException {
		StringWriter writer = new StringWriter();
		JAXBContext jaxbContext = JAXBContext.newInstance(SupplierCTranslatorMessage.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(response, writer);
		return writer.toString();
	}
	
	/**
	 * Marshal SupplierOrderResponse object to string.
	 */
	private String marshalError(SupplierCErrorMessage errormsg) throws JAXBException {
		StringWriter writer = new StringWriter();
		JAXBContext jaxbContext = JAXBContext.newInstance(SupplierCErrorMessage.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		marshaller.marshal(errormsg, writer);
		return writer.toString();
	}

}
