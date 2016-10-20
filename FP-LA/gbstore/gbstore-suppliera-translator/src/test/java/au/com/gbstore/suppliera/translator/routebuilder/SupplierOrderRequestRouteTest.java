package au.com.gbstore.suppliera.translator.routebuilder;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBException;

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

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class SupplierOrderRequestRouteTest extends CamelTestSupport{

	private static final String INPUT_MESSAGE = "supOrderReqRoute/msg/input/message-to-suppliera.xml";
	private static final String RESPONSE_MESSAGE = "supOrderReqRoute/msg/input/suppliera-response.json";
		
	    @Produce(uri = "direct:from")
	    protected ProducerTemplate startEndpoint;
	    
	  	@EndpointInject(uri = "mock:to")
	  	protected MockEndpoint mock;
	  	
	    protected CamelContext createCamelContext() throws Exception {
	        CamelContext context = new DefaultCamelContext();
	        PropertiesComponent properties = new PropertiesComponent();
	        properties.setLocation("classpath:supOrderReqRoute/conf/au.com.gbstore.suppliera.translator.cfg");
	        context.addComponent("properties", properties);
	        context.addRoutes(new SupplierOrderRequestRoute());
	        return context;
	    }
	    @Override
	    protected RouteBuilder createRouteBuilder() throws Exception {
	       return new RouteBuilder() {
			
			@Override
			public void configure() throws Exception {
				from("{{spa.suporder.to}}")
				.process(new Processor() {
					public void process(Exchange exchange) throws Exception {
						exchange.getIn().setBody(fileToString(getClass(), RESPONSE_MESSAGE));
					}
				});
			}
		};
	    }
	    /**Message receive must be a json
	     * @throws CamelExecutionException
	     * @throws IOException
	     * @throws JAXBException
	     */
	    @Test
	    public void messageFormat() throws CamelExecutionException, IOException, JAXBException{
	    	startEndpoint.requestBody(fileToString(getClass(), INPUT_MESSAGE));
	    	String body = mock.getExchanges().get(0).getIn().getBody(String.class);
	    	
	    	JSONObject bodyJson = (JSONObject) JSONSerializer.toJSON(body);
	    	assertEquals(1, bodyJson.entrySet().size());
	    	assertTrue(bodyJson.has("supplierOrderResponse"));
	    }
	    
	    /**
		 * Suppose to receive one message for each supplier
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
