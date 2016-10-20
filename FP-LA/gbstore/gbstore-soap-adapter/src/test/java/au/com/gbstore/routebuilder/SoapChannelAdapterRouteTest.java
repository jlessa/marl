package au.com.gbstore.routebuilder;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
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

import au.com.gbstore.messages.legacysystem.errormessage.v1.Error;
import au.com.gbstore.messages.legacysystem.storeorderrequest.v1.StoreOrderRequest;
import au.com.gbstore.messages.legacysystem.storeorderresponse.v1.StoreOrderResponse;
import au.com.gbstore.types.legacysystem.v1.EntryType;
import au.com.gbstore.types.legacysystem.v1.ProductStoreType;
import au.com.gbstore.types.legacysystem.v1.StoreOrderListType;
import au.com.gbstore.types.legacysystem.v1.StoreOrderType;

/**
 * @author lteixeira
 *
 */
public class SoapChannelAdapterRouteTest extends CamelTestSupport {

    private static final String INPUT_MESSAGE = "soapAdapterRoute/msg/input/store-request-message.xml";

    @EndpointInject(uri = "mock:to")
    protected MockEndpoint mock;

    @Produce(uri = "direct:from")
    protected ProducerTemplate startEndpoint;

    /**
     * override method to add properties to camel context
     */
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = new DefaultCamelContext();
        PropertiesComponent properties = new PropertiesComponent();
        properties.setLocation("classpath:soapAdapterRoute/conf/au.com.gbstore.soapadapter.cfg");
        context.addComponent("properties", properties);
        return context;
    }
    
    /* 
     * Call soap adapter' route builder
     */
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new SoapChannelAdapterRoute();
    }
    
    /**
     * Throws an exception
     * and expects an error message
     * @throws JAXBException
     * @throws InterruptedException
     */
    @Test
    public void testExceptionError() throws JAXBException, InterruptedException{
    	String resp = startEndpoint.requestBody(new Exception("Testing Error")).toString();
    	assertStringContains(resp, "<faultCode>1030</faultCode>");
    }
    
    /**
     * Test the Error Object
     * Throws an exception
     * and receive a errorMessage
     * s
     * @throws JAXBException
     * @throws InterruptedException
     * @throws CamelExecutionException
     * @throws IOException
     */
    @Test
    public void testExceptionErrorObject() throws JAXBException, InterruptedException, CamelExecutionException, IOException{
    	Error expected = new Error();
    	expected.setFaultCode("1030");
    	expected.setFaultString("Testing Error");
		Object body = startEndpoint.requestBody(new Exception("Testing Error"));
		
		StringReader reader = new StringReader(body.toString());
		JAXBContext jxb = JAXBContext.newInstance(Error.class);
		Unmarshaller unmarshaller = jxb.createUnmarshaller();
		Error error =  (Error)unmarshaller.unmarshal(reader);
		
		assertStringContains(error.getFaultCode(), expected.getFaultCode());
		
    }
    
    /**Send a message
     * and confirm the success response message
     * 
     * @throws JAXBException
     * @throws CamelExecutionException
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testResponseMessage() throws JAXBException, CamelExecutionException, IOException, InterruptedException{
    	StoreOrderResponse response = new StoreOrderResponse();
    	response.setMessage("Your request has been received");
    	
    	StringWriter writer = new StringWriter();
		JAXBContext jxb = JAXBContext.newInstance(StoreOrderResponse.class);
		Marshaller marshaller = jxb.createMarshaller();
		marshaller.marshal(response, writer);
		
		String resp = startEndpoint.requestBody(fileToString(getClass(), INPUT_MESSAGE)).toString();
		
		assertEquals(writer.toString(), resp); 
		
    }
    
    /**
     * Test the message format
     * Send a message
     * and confirm the success response message
     * 
     * @throws JAXBException
     * @throws CamelExecutionException
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testResponseMessageObject() throws JAXBException, CamelExecutionException, IOException, InterruptedException{
    	String message = "Your request has been received";
		
		Object body = startEndpoint.requestBody(fileToString(getClass(), INPUT_MESSAGE));
		
		StringReader reader = new StringReader(body.toString());
		JAXBContext jxb = JAXBContext.newInstance(StoreOrderResponse.class);
		Unmarshaller unmarshaller = jxb.createUnmarshaller();
		StoreOrderResponse response = (StoreOrderResponse) unmarshaller.unmarshal(reader);
		
		assertEquals(message, response.getMessage());
    }
    
    /**
     * Tests whether the transformation insert a date
     * into processDate tag
     */
    @Test
    public void testMessageTransformation() throws CamelExecutionException, IOException, JAXBException{
    	startEndpoint.sendBody(fileToString(getClass(), INPUT_MESSAGE));
    	Object body = mock.getExchanges().get(0).getIn().getBody();
    	
    	StringReader reader = new StringReader(body.toString());
		JAXBContext jxb = JAXBContext.newInstance(StoreOrderRequest.class);
		Unmarshaller unmarshaller = jxb.createUnmarshaller();
		StoreOrderRequest req = (StoreOrderRequest) unmarshaller.unmarshal(reader);
    	
		EntryType entry = req.getEntry();
		StoreOrderListType list = entry.getStoreOrderList();
		StoreOrderType storeOrder = list.getStoreOrder().get(0);
		ProductStoreType product = storeOrder.getItemStoreOrderList().getItemStoreOrder().get(0).getProduct();
		assertFalse(list.getStoreOrder().isEmpty());
		assertTrue(entry.getInsertDate().isValid());
    	assertTrue(entry.getProcessDate().isValid());
    	assertEquals(list.getStoreOrder().size(), 1);
    	assertEquals(product.getInternalId(),"1");
    }
    
    /**Test the message format
     * 
     * @throws CamelExecutionException
     * @throws IOException
     * @throws JAXBException
     */
    @Test
    public void testObjectTransformed() throws CamelExecutionException, IOException, JAXBException{
    	startEndpoint.sendBody(fileToString(getClass(), INPUT_MESSAGE));
    	Object body = mock.getExchanges().get(0).getIn().getBody();
    	
    	StringReader reader = new StringReader(body.toString());
		JAXBContext jxb = JAXBContext.newInstance(StoreOrderRequest.class);
		Unmarshaller unmarshaller = jxb.createUnmarshaller();
		StoreOrderRequest req = (StoreOrderRequest) unmarshaller.unmarshal(reader);
    	
		EntryType entry = req.getEntry();
		assertFalse(entry.getStoreOrderList().getStoreOrder().isEmpty());
    }
    
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
