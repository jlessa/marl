package au.com.gbstore.supplierb.translator.routebuilder;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import au.com.gbstore.messages.dbtranslator.saveordernumber.v1.SaveOrderNumber;
import au.com.gbstore.messages.splitter.splittermessage.v1.SplitterOrderToCbr;

/**
 * this class test the whole route. from the message received from Splitter till
 * the response received from SupplierA
 * 
 * @author lteixeira
 *
 */
public class SupplierBTranslatorRouteTest extends CamelTestSupport {

	private static final String INPUT_MESSAGE = "supplierTranslatorRoute/msg/input/message-to-supplierb.xml";
	private static final String RESPONSE_MESSAGE = "supplierTranslatorRoute/msg/input/supplierb-response.xml";

	@Produce(uri = "direct:from")
	protected ProducerTemplate startEndpoint;

	@EndpointInject(uri = "mock:to")
	protected MockEndpoint mock;

	protected CamelContext createCamelContext() throws Exception {
		CamelContext context = new DefaultCamelContext();
		PropertiesComponent properties = new PropertiesComponent();
		properties.setLocation("classpath:supplierTranslatorRoute/conf/au.com.gbstore.supplierb.translator.cfg");
		context.addComponent("properties", properties);
		return context;
	}

	/** Add Routes and create a new route that simulates the route between SupplierATranslator's routes. 
	 * @see org.apache.camel.test.junit4.CamelTestSupport#createRouteBuilders()
	 */
	@Override
    	protected RouteBuilder[] createRouteBuilders() throws Exception {
    		return new RouteBuilder[]{
    				new SupplierOrderRequestRoute(),
    				new SaveOrderNumberRoute(),
    				new RouteBuilder() {
    		    		
    		    		@Override
    		    		public void configure() throws Exception {
    		    			from("{{spb.suporder.to}}")
    		    			.process(new Processor() {
    		    				public void process(Exchange exchange) throws Exception {
    		    					exchange.getIn().setBody(fileToString(getClass(), RESPONSE_MESSAGE));
    		    				}
    		    			});
    		    		}
    		    	}
    		};
    	}

	/**	Verify the type of received message is right and its needed elements(orderNumber and supplierOrderId)
	 *  Test presence of orderNumber
	 *  Verify the supplierOrderId is the same as the one in input message
	 * 
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws JAXBException
	 */
	@Test
	public void testFinalMessageElements() throws CamelExecutionException, IOException, JAXBException {
		String message = fileToString(getClass(), INPUT_MESSAGE);
		startEndpoint.requestBody(message);
		String body = mock.getExchanges().get(0).getIn().getBody(String.class);
		
		SplitterOrderToCbr order = unmarshallerSplitterMessage(message);
		SaveOrderNumber response = unmarshallerSaveOrderNumber(body);
		
		assertNotNull(response.getSupplierOrderResponse().getOrderNumber());
		assertEquals(order.getSupplierOrder().getSupplierOrderId(),
				response.getSupplierOrderResponse().getSupplierOrderId());
	}

	/**Test the final message.
	 * 
	 * @throws JAXBException
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void testFinalMessage() throws JAXBException, CamelExecutionException, IOException, InterruptedException {
		startEndpoint.sendBody(fileToString(getClass(), INPUT_MESSAGE));
		mock.setExpectedMessageCount(1);
		mock.assertIsSatisfied();
	}

	/**
	 * convert a file into a string
	 * 
	 * @param loader
	 * @param fileLocation
	 * @return
	 * @throws IOException
	 */
	public static String fileToString(Class<?> loader, String fileLocation) throws IOException {
		return IOUtils.toString(loader.getClassLoader().getResource(fileLocation), Charset.defaultCharset());
	}

	/**Unmarshall SaveOrderNumber message
	 * @param body
	 * @return
	 * @throws JAXBException
	 */
	public SaveOrderNumber unmarshallerSaveOrderNumber(String body) throws JAXBException {
		StringReader reader = new StringReader(body);
		JAXBContext jxb = JAXBContext.newInstance(SaveOrderNumber.class);
		Unmarshaller u = jxb.createUnmarshaller();
		return (SaveOrderNumber) u.unmarshal(reader);
	}
	
	/**Unmarshall Splitter message
	 * @param message
	 * @return
	 * @throws JAXBException
	 */
	public SplitterOrderToCbr unmarshallerSplitterMessage(String message) throws JAXBException{
		StringReader reader = new StringReader(message);
		JAXBContext jxb = JAXBContext.newInstance(SplitterOrderToCbr.class);
		Unmarshaller u = jxb.createUnmarshaller();
		SplitterOrderToCbr order = (SplitterOrderToCbr) u.unmarshal(reader);
		return order;
	}

}
