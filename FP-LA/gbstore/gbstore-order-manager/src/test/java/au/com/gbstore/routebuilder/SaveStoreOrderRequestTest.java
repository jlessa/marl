package au.com.gbstore.routebuilder;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import au.com.gbstore.messages.dbtranslator.savestoreorderreply.v1.SaveStoreOrderReply;
import au.com.gbstore.messages.dbtranslator.savestoreorderrequest.v1.SaveStoreOrderRequest;


/**
 * Test the route to save the entry message
 */
public class SaveStoreOrderRequestTest extends CamelTestSupport {

	private static final String INPUT_MESSAGE = "saveStoreOrderRoute/msg/input/soap-message.xml";
	private static final String RESPONSE_MESSAGE = "saveStoreOrderRoute/msg/mock/db-save-order-response.xml";

	//mocks the endpoint from Soap Adapter Channel
	@Produce(uri = "direct:from")
	protected ProducerTemplate startEndpoint;

	//Endpoint sends message to DB
	@EndpointInject(uri = "mock:to")
	protected MockEndpoint mock;
	
	//receives the message from DB
	@Produce(uri = "direct:db")
	protected ProducerTemplate dbResponse;
		
	//sends to Enrichment endpoint
	@EndpointInject(uri = "mock:result")
	protected MockEndpoint result;
	
	/**
	 * override method to add properties to camel context
	 */
	protected CamelContext createCamelContext() throws Exception {
		CamelContext context = new DefaultCamelContext();
		PropertiesComponent properties = new PropertiesComponent();
		properties.setLocation("classpath:saveStoreOrderRoute/conf/au.com.gbstore.ordermanager.cfg");
		context.addComponent("properties", properties);
		return context;
	}

	/**
	 * Call soap adapter' route builder
	 */
	@Override
	protected RouteBuilder createRouteBuilder() {
		return new SaveStoreOrderRoute();
	}
	
	/**
	 * Checks the entry message
	 * before it is sent to DB
	 * 
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws JAXBException
	 */
	@Test
	public void messageSentDB() throws CamelExecutionException, IOException, JAXBException{
		startEndpoint.requestBody(fileToString(getClass(), INPUT_MESSAGE));
		StringReader reader = new StringReader(mock.getExchanges().get(0).getIn().getBody(String.class));
		JAXBContext jxb = JAXBContext.newInstance(SaveStoreOrderRequest.class);
		Unmarshaller u = jxb.createUnmarshaller();
		SaveStoreOrderRequest request = (SaveStoreOrderRequest) u.unmarshal(reader);
		
		assertFalse(request.getEntry().getStoreOrderList().getStoreOrder().isEmpty());
	}
	
	/**
	 * Receive a response from DB
	 * about the entry saved
	 * 
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws JAXBException
	 */
	@Test
	public void messageReceivedDB() throws CamelExecutionException, IOException, JAXBException{
		Object body = dbResponse.requestBody(fileToString(getClass(), RESPONSE_MESSAGE));
		StringReader reader = new StringReader(body.toString());
		JAXBContext jxb = JAXBContext.newInstance(SaveStoreOrderReply.class);
		Unmarshaller u = jxb.createUnmarshaller();
		SaveStoreOrderReply response = (SaveStoreOrderReply) u.unmarshal(reader);
		
		assertFalse(response.getEntry().getStoreOrderList().getStoreOrder().isEmpty());
	}
	
	/**
	 * Tests how many messages
	 * the route sent to DB
	 * 
	 * @throws JAXBException
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void testMiddleMessage() throws JAXBException, CamelExecutionException, IOException, InterruptedException {
		startEndpoint.sendBody(fileToString(getClass(), INPUT_MESSAGE));
		mock.setExpectedMessageCount(1);
		mock.assertIsSatisfied();
	}

	/**Test how many messages OM received from db
	 * 
	 * @throws JAXBException
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void testFinalMessage() throws JAXBException, CamelExecutionException, IOException, InterruptedException {
		dbResponse.sendBody(fileToString(getClass(), RESPONSE_MESSAGE));
		result.setExpectedMessageCount(1);
		result.assertIsSatisfied();
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

}
