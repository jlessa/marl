package au.com.gbstore.processor;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.cxf.binding.soap.SoapFault;

import au.com.gbstore.messages.legacysystem.errormessage.v1.Error;
/**
 * @author lteixeira
 *Sends a error message
 */
public class ErrorMessageProcessor implements Processor {

	/**
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 * Creates an soap fault message
	 * marshal it
	 * and send it back
	 * @throws Exception
	 */
	public void process(Exchange exchange) throws Exception {
		Exception exception = (Exception) exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Exception.class);
		SoapFault fault = new SoapFault(exception.getMessage(), SoapFault.FAULT_CODE_SERVER);
		
		Error error = new Error();
		error.setFaultString(fault.getMessage());
		error.setFaultCode("1030");
		
		StringWriter writer = new StringWriter();
		JAXBContext jxb = JAXBContext.newInstance(Error.class);
		Marshaller marshaller = jxb.createMarshaller();
		marshaller.marshal(error, writer);
		
		exchange.getOut().setFault(true);
		exchange.getOut().setBody(writer.toString());
	}

}
