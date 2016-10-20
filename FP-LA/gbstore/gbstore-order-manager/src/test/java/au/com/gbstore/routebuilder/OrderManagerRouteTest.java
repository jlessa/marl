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

import au.com.gbstore.messages.ordermanager.ordermanagermessage.v1.SplitterSupplierMessage;


/**
 * Test all routes in OrderManager Component
 *
 */
public class OrderManagerRouteTest extends CamelTestSupport{
	
	private static final String INPUT_MESSAGE = "orderManagerRoute/msg/input/soap-message.xml";
	private static final String RESPONSE_MESSAGE = "orderManagerRoute/msg/input/db-saved-entry.xml";
	private static final String SUP_SAVED_MESSAGE = "orderManagerRoute/msg/input/supplier-order.xml";
	
	 @Produce(uri = "direct:from")
	 protected ProducerTemplate startEndpoint;
	 
	 
    @EndpointInject(uri = "mock:to")
    protected MockEndpoint result;
	
	protected CamelContext createCamelContext() throws Exception {
		CamelContext context = new DefaultCamelContext();
		PropertiesComponent properties = new PropertiesComponent();
		properties.setLocation("classpath:orderManagerRoute/conf/au.com.gbstore.ordermanager.cfg");
		context.addComponent("properties", properties);
		context.addRoutes(new SaveStoreOrderRoute());
		context.addRoutes(new EnrichmentRequestRoute());
		context.addRoutes(new SplitterMessagingRoute());
		return context;
	}

		/**
		 * Call soap adapter' route builder
		 */
	@Override
	protected RouteBuilder createRouteBuilder() {
		return new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				//simulating DB response after saving entry
				from("direct:a")
				.process(new Processor() {
					
					public void process(Exchange exchange) throws Exception {
						String newBody = fileToString(getClass(), RESPONSE_MESSAGE).toString();
						exchange.getIn().setBody(newBody);
					}
				})
				.to("direct:b");
				
				//simulating DB response after enrichment
				from("direct:c")
				.to("xquery:dbprocess/enrichmentResponse.xq")
				.inOnly("direct:d");
				
				from("direct:g")
				.process(new Processor(){
					public void process(Exchange exchange) throws IOException{
						String newBody = fileToString(getClass(), SUP_SAVED_MESSAGE).toString();
						exchange.getIn().setBody(newBody);
					}
				});
			}
		};
	}
	
	/** Test all routes in Order Manager Component.
	 * 
	 * Send a message received from Soap Adapter
	 * and gets a message to send to Splitter
	 * 
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws JAXBException
	 */
	@Test
	public void orderManagerRouteTest() throws CamelExecutionException, IOException, JAXBException{
		startEndpoint.requestBody(fileToString(getClass(), INPUT_MESSAGE)).toString();
    	String body = result.getExchanges().get(0).getIn().getBody(String.class);

    	StringReader reader = new StringReader(body);
		JAXBContext jxb = JAXBContext.newInstance(SplitterSupplierMessage.class);
		Unmarshaller u = jxb.createUnmarshaller();
		SplitterSupplierMessage r =  (SplitterSupplierMessage) u.unmarshal(reader);
		
		assertListSize(r.getSupplierOrder(), 3);
	}

	/**Counts how many message received
	 * @throws JAXBException
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
    public void testFinalMessage() throws JAXBException, CamelExecutionException, IOException, InterruptedException {
        startEndpoint.sendBody(fileToString(getClass(), INPUT_MESSAGE));
        
        result.setExpectedMessageCount(1);
        result.assertIsSatisfied();
    }
    /**Parsing file to string
     * @param loader
     * @param fileLocation
     * @return
     * @throws IOException
     */
    public static String fileToString(Class<?> loader, String fileLocation) throws IOException {
        return IOUtils.toString(loader.getClassLoader().getResource(fileLocation), Charset.defaultCharset());
    }

}
