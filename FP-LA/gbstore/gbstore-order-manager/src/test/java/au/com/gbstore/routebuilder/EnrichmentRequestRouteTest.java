package au.com.gbstore.routebuilder;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

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

import au.com.gbstore.messages.dbtranslator.savesupplierordersrequest.v1.SaveSupplierOrderRequest;

/**
 *Test enrichment routes
 *
 */
public class EnrichmentRequestRouteTest extends CamelTestSupport {

	private static final String INPUT_MESSAGE = "enrichmentRequestRoute/msg/input/db-save-order-response.xml";
    
    @Produce(uri = "direct:from")
    protected ProducerTemplate startEndpoint;
    
    @Produce(uri = "direct:to")
    protected ProducerTemplate mock;
    
  //receives the message from DB
  	@Produce(uri = "direct:db")
  	protected ProducerTemplate dbResponse;
  		
  	//sends to Splitter endpoint
  	@EndpointInject(uri = "mock:result")
  	protected MockEndpoint result;


    /**
     * override method to add properties to camel context.
     */
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context = new DefaultCamelContext();
        PropertiesComponent properties = new PropertiesComponent();
        properties.setLocation("classpath:enrichmentRequestRoute/conf/au.com.gbstore.ordermanager.enrichmentreqroute.cfg");
        context.addComponent("properties", properties);
        context.addRoutes(new EnrichmentRequestRoute());
        return context;
    }
    
    /**
     * Call soap adapter' route builder
     */
    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
       return new RouteBuilder() {
		
		@Override
		public void configure() throws Exception {
			//simulating DB response
			from("direct:to")
			.to("xquery:dbprocess/enrichmentResponse.xq")
			.inOnly("direct:db");
			
			from("direct:sp")
			.process(new Processor(){
				public void process(Exchange exchange){
					exchange.getIn().setBody(exchange.getIn().getBody());
				}
			});
		}
       };
    }
    
    /**Send a message
     * and confirm the success response message.
     * 
     * If the transformation is right
     * the supplier order list
     * have max 3 orders
     * 
     * @throws CamelExecutionException
     * @throws IOException
     * @throws InterruptedException
     * @throws XMLStreamException
     * @throws FactoryConfigurationError
     */
    
    @Test
    public void testTransformationFormat() throws CamelExecutionException, IOException, JAXBException, InterruptedException{
    	startEndpoint.requestBody(fileToString(getClass(), INPUT_MESSAGE)).toString();
    	String body = result.getExchanges().get(0).getIn().getBody(String.class);

    	StringReader reader = new StringReader(body);
		JAXBContext jxb = JAXBContext.newInstance(SaveSupplierOrderRequest.class);
		Unmarshaller u = jxb.createUnmarshaller();
		SaveSupplierOrderRequest r =  (SaveSupplierOrderRequest) u.unmarshal(reader);
		
		assertListSize(r.getSupplierOrder(), 3);
    }
    
    /**
     * Testing if the transformation inserted the productSupplierId.
     * @throws CamelExecutionException
     * @throws IOException
     * @throws JAXBException
     */
    @Test
    public void testEnrichmentTest() throws CamelExecutionException, IOException, JAXBException{
    	startEndpoint.requestBody(fileToString(getClass(), INPUT_MESSAGE)).toString();
    	String body = result.getExchanges().get(0).getIn().getBody(String.class);
    	StringReader reader = new StringReader(body);
		JAXBContext jxb = JAXBContext.newInstance(SaveSupplierOrderRequest.class);
		Unmarshaller u = jxb.createUnmarshaller();
		SaveSupplierOrderRequest r =  (SaveSupplierOrderRequest) u.unmarshal(reader);
		
		assertEquals(r.getSupplierOrder().get(0).getItemSupplierOrderList().getItemSupplierOrder().get(0).getProduct().getProductSupplierId(),"1");
    }
    
    
    /**
     * counts how many message it receives.
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

    /**
     * convert a file into a string.
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
