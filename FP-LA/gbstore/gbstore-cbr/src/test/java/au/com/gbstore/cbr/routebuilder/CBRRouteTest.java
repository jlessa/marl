package au.com.gbstore.cbr.routebuilder;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBException;

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


public class CBRRouteTest extends CamelTestSupport{
	private static final String INPUT_MESSAGE_A = "cbrRoute/msg/input/message-to-suppliera.xml";
	private static final String INPUT_MESSAGE_B = "cbrRoute/msg/input/message-to-supplierb.xml";
	private static final String INPUT_MESSAGE_C = "cbrRoute/msg/input/message-to-supplierc.xml";
	
    @Produce(uri = "direct:from")
    protected ProducerTemplate startEndpoint;
    
  	@EndpointInject(uri = "mock:a.to")
  	protected MockEndpoint mockA;
  	
  	@EndpointInject(uri = "mock:b.to")
  	protected MockEndpoint mockB;
  	
  	@EndpointInject(uri = "mock:c.to")
  	protected MockEndpoint mockC;
    
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = new DefaultCamelContext();
        PropertiesComponent properties = new PropertiesComponent();
        properties.setLocation("classpath:cbrRoute/conf/au.com.gbstore.cbr.cfg");
        context.addComponent("properties", properties);
        return context;
    }
    /**Calls CBR route
     * @see org.apache.camel.test.junit4.CamelTestSupport#createRouteBuilder()
     */
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
       return new CBRRoute();
    }
	/**
	 * Sends a message with header
	 * and get in the right mocker
	 * 
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void routeSupplierATest() throws CamelExecutionException, IOException, InterruptedException {
		mockA.expectedBodiesReceived(fileToString(getClass(), INPUT_MESSAGE_A));
		mockA.expectedHeaderReceived("supplierId", "1");
		startEndpoint.sendBodyAndHeader(fileToString(getClass(), INPUT_MESSAGE_A), "supplierId", "1");
		mockA.assertIsSatisfied();
	}
	/**
	 * Sends a message with a header
	 * to supplier B
	 * and gets it in the right mocker
	 * 
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void routeSupplierBTest() throws CamelExecutionException, IOException, InterruptedException {
		mockB.expectedBodiesReceived(fileToString(getClass(), INPUT_MESSAGE_B));
		mockB.expectedHeaderReceived("supplierId", "2");
		startEndpoint.sendBodyAndHeader(fileToString(getClass(), INPUT_MESSAGE_B), "supplierId", "2");
		mockB.assertIsSatisfied();
	}
	/**
	 * Sends a message with a header
	 * to supplierC
	 * and gets it in the righr mocker
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void routeSupplierCTest() throws CamelExecutionException, IOException, InterruptedException {
		mockC.expectedBodiesReceived(fileToString(getClass(), INPUT_MESSAGE_C));
		mockC.expectedHeaderReceived("supplierId", "3");
		startEndpoint.sendBodyAndHeader(fileToString(getClass(), INPUT_MESSAGE_C), "supplierId", "3");
		mockC.assertIsSatisfied();
	}
	
	/**
	 * Sends a message to supplierA
	 * and counts the message received for each mocker
	 * @throws JAXBException
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
    public void testFinalMessage() throws JAXBException, CamelExecutionException, IOException, InterruptedException {
        startEndpoint.sendBodyAndHeader(fileToString(getClass(), INPUT_MESSAGE_A), "supplierId", "1");
        mockA.setExpectedMessageCount(1);
        mockB.setExpectedMessageCount(0);
        mockC.setExpectedMessageCount(0);
        mockA.assertIsSatisfied();
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
