package au.com.gbstore.routebuilder;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
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
 * Test the route that sends a message to Splitter
 * @author lteixeira
 *
 */
public class SplitterMessageRouteTest extends CamelTestSupport{
	
	private static final String INPUT_MESSAGE = "splitterMessageRoute/msg/input/supplier-order.xml";
	
	 @Produce(uri = "direct:from")
	 protected ProducerTemplate startEndpoint;
	    
    @Produce(uri = "mock:to")
    protected MockEndpoint mock;
	
	protected CamelContext createCamelContext() throws Exception {
		CamelContext context = new DefaultCamelContext();
		PropertiesComponent properties = new PropertiesComponent();
		properties.setLocation("classpath:splitterMessageRoute/conf/au.com.gbstore.ordermanager.splittermessage.cfg");
		context.addComponent("properties", properties);
		return context;
	}

	/**
	 * Call soap adapter' route builder
	 */
	@Override
	protected RouteBuilder createRouteBuilder() {
		return new SplitterMessagingRoute();
	}
	
	/**Test the message that will be send to Splitter
	 * The format must match with expected
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws JAXBException
	 */
	@Test
	public void splitterTranformationTest() throws CamelExecutionException, IOException, JAXBException{
		startEndpoint.requestBody(fileToString(getClass(), INPUT_MESSAGE)).toString();
    	String body = mock.getExchanges().get(0).getIn().getBody(String.class);

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
        
        mock.setExpectedMessageCount(1);
        mock.assertIsSatisfied();
    }
    
	
    /**Convert file to string
     * @param loader
     * @param fileLocation
     * @return
     * @throws IOException
     */
    public static String fileToString(Class<?> loader, String fileLocation) throws IOException {
        return IOUtils.toString(loader.getClassLoader().getResource(fileLocation), Charset.defaultCharset());
    }

}
