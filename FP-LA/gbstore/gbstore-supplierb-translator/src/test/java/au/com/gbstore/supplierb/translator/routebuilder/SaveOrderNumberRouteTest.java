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

public class SaveOrderNumberRouteTest extends CamelTestSupport {

	private static final String INPUT_MESSAGE = "saveOrderNumberRoute/msg/input/supplierb-response.xml";

	@Produce(uri = "direct:from")
	protected ProducerTemplate startEndpoint;

	@EndpointInject(uri = "mock:to")
	protected MockEndpoint mock;

	protected CamelContext createCamelContext() throws Exception {
		CamelContext context = new DefaultCamelContext();
		PropertiesComponent properties = new PropertiesComponent();
		properties.setLocation("classpath:saveOrderNumberRoute/conf/au.com.gbstore.supplierb.translator.cfg");
		context.addComponent("properties", properties);
		return context;
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new SaveOrderNumberRoute();
	}
	
	/** Test message received in the route and verify the orderNumber
	 * @throws JAXBException
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void messageReceived() throws CamelExecutionException, IOException, JAXBException{
		startEndpoint.requestBody(fileToString(getClass(), INPUT_MESSAGE));
		String body = mock.getExchanges().get(0).getIn().getBody(String.class);
		SaveOrderNumber response = unmarshallerSaveOrderNumber(body);
		assertEquals("16", response.getSupplierOrderResponse().getOrderNumber());
	}


	/**Test final message received
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
}
