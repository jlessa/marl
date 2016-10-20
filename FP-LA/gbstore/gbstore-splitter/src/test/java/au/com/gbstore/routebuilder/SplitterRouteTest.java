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

import au.com.gbstore.messages.splitter.splittermessage.v1.SplitterOrderToCbr;

public class SplitterRouteTest extends CamelTestSupport{
	
private static final String INPUT_MESSAGE = "splitterRoute/msg/input/splitter-message.xml";
    
    @Produce(uri = "direct:from")
    protected ProducerTemplate startEndpoint;
    
  	@EndpointInject(uri = "mock:to")
  	protected MockEndpoint result;
    
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = new DefaultCamelContext();
        PropertiesComponent properties = new PropertiesComponent();
        properties.setLocation("classpath:splitterRoute/conf/au.com.gbstore.splitter.cfg");
        context.addComponent("properties", properties);
        return context;
    }
    /** Call routes
     * @see org.apache.camel.test.junit4.CamelTestSupport#createRouteBuilder()
     */
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
       return new SplitterRoute();
    }
    
	/**
	 *header content
	 *matches with supplierId in the body
	 * @throws IOException 
	 * @throws CamelExecutionException 
	 * @throws JAXBException 
	 */
	@Test
	public void headerCBRMessageTest() throws CamelExecutionException, IOException, JAXBException {
		startEndpoint.requestBody(fileToString(getClass(), INPUT_MESSAGE));
		String body = result.getExchanges().get(0).getIn().getBody(String.class);
		String header = result.getExchanges().get(0).getIn().getHeader("supplierId", String.class);
		SplitterOrderToCbr message = unmarshaller(body);
		
		assertEquals(message.getSupplierOrder().getSupplierId(), header);
	}
	
	/**
	 * Headers in the final message
	 * must be this values
	 * 1, 2, or 3
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void expectedHeadersTest() throws CamelExecutionException, IOException, InterruptedException{
		result.expectedHeaderValuesReceivedInAnyOrder("supplierId", "1","2","3");
		startEndpoint.requestBody(fileToString(getClass(), INPUT_MESSAGE));
		result.assertIsSatisfied();
	}
	/**
	 * Supposed to receive one message for each supplier
	 * 
	 * @throws JAXBException
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	 @Test
	    public void testFinalMessage() throws JAXBException, CamelExecutionException, IOException, InterruptedException {
	       startEndpoint.sendBody(fileToString(getClass(), INPUT_MESSAGE));
	       result.setExpectedMessageCount(3);
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
    
    /** Unmarshal body to SplitterOrderToCbr
     * @param body
     * @return
     * @throws JAXBException
     */
    public SplitterOrderToCbr unmarshaller(String body) throws JAXBException{
		StringReader reader = new StringReader(body);
		JAXBContext jxb = JAXBContext.newInstance(SplitterOrderToCbr.class);
		Unmarshaller u = jxb.createUnmarshaller();
		return (SplitterOrderToCbr) u.unmarshal(reader);
	}

}
