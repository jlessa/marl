package au.com.gbstore.supplierb.translator.routebuilder;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBException;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
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

public class SupplierOrderRequestRouteTest extends CamelTestSupport {

	private static final String INPUT_MESSAGE = "supOrderReqRoute/msg/input/message-to-supplierb.xml";
	private static final String RESPONSE_MESSAGE = "supOrderReqRoute/msg/input/supplierb-response.xml";

	@Produce(uri = "direct:from")
	protected ProducerTemplate startEndpoint;

	@EndpointInject(uri = "mock:to")
	protected MockEndpoint mock;

	@EndpointInject(uri = "mock:toSupB")
	protected MockEndpoint toSupB;
	
	protected CamelContext createCamelContext() throws Exception {
		CamelContext context = new DefaultCamelContext();
		PropertiesComponent properties = new PropertiesComponent();
		properties.setLocation("classpath:supOrderReqRoute/conf/au.com.gbstore.supplierb.translator.cfg");
		context.addComponent("properties", properties);
		return context;
	}

	@Override
	protected RouteBuilder[] createRouteBuilders() throws Exception {
		return new RouteBuilder[] { new SupplierOrderRequestRoute(), new RouteBuilder() {
			@Override
			public void configure() throws Exception {
				from("{{spb.suporder.to}}").to(ExchangePattern.InOnly, "mock:toSupB").process(new Processor() {
					public void process(Exchange exchange) throws Exception {
						exchange.getIn().setBody(fileToString(getClass(), RESPONSE_MESSAGE));
					}
				});
			}
		} };
	}

	/**
	 * Verify the message to SupplierB has the right root's name
	 * 
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws JAXBException
	 * @throws InterruptedException 
	 */
	@Test
	public void messageFormat() throws CamelExecutionException, IOException, JAXBException, InterruptedException {
		startEndpoint.requestBody(fileToString(getClass(), INPUT_MESSAGE));
		String body = toSupB.getExchanges().get(0).getIn().getBody(String.class);
		assertTrue(body.contains("supplierBMessage"));
	}

	/**
	 * Test final message received
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
}
