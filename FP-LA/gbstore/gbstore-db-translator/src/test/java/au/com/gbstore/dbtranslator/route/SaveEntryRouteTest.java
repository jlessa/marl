package au.com.gbstore.dbtranslator.route;

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
import org.junit.BeforeClass;
import org.junit.Test;

import au.com.gbstore.dbtranslator.dao.ProductDao;
import au.com.gbstore.dbtranslator.dao.StoreDao;
import au.com.gbstore.dbtranslator.dao.SupplierDao;
import au.com.gbstore.dbtranslator.model.Product;
import au.com.gbstore.dbtranslator.model.Store;
import au.com.gbstore.dbtranslator.model.Supplier;
import au.com.gbstore.messages.dbtranslator.savestoreorderreply.v1.SaveStoreOrderReply;
import au.com.gbstore.messages.dbtranslator.savestoreorderrequest.v1.SaveStoreOrderRequest;

public class SaveEntryRouteTest extends CamelTestSupport{
	private static final String INPUT_MESSAGE = "saveEntryRoute/msg/input/store-request-message.xml";
	private static final String EMPTY_MESSAGE = "saveEntryRoute/msg/input/empty-message.xml";
	
	@Produce(uri = "direct:from")
	protected ProducerTemplate startEndpoint;

	@EndpointInject(uri = "mock:to")
	protected MockEndpoint mock;
	
	protected CamelContext createCamelContext() throws Exception {
		CamelContext context = new DefaultCamelContext();
		PropertiesComponent properties = new PropertiesComponent();
		properties.setLocation("classpath:saveEntryRoute/conf/au.com.gbstore.dbtranslator.cfg");
		context.addComponent("properties", properties);
		return context;
	}
	/**
	 * Call soap adapter' route builder
	 */
	@Override
	protected RouteBuilder createRouteBuilder() {
		return new SaveEntryRoute();
	}
	
	@BeforeClass
	public static void setUpDB() throws Exception{
		SupplierDao sdao = new SupplierDao();
		ProductDao pdao = new ProductDao();
		StoreDao stdao = new StoreDao();
		
		Store boteco = new Store();
		boteco.setName("Boteco");
		stdao.persist(boteco);
		
		Store devassa = new Store();
		devassa.setName("Devassa");
		stdao.persist(devassa);
		
		Store bar = new Store();
		bar.setName("Bar");
		stdao.persist(bar);
		
		Supplier suppliera = new Supplier();
		suppliera.setName("SupplierA");
		suppliera = sdao.persist(suppliera);
		
		Supplier supplierb = new Supplier();
		supplierb.setName("SupplierB");
		supplierb = sdao.persist(supplierb);
		
		Supplier supplierc = new Supplier();
		supplierc.setName("SupplierC");
		supplierc = sdao.persist(supplierc);
		
		Product budweiser = new Product();
		budweiser.setName("Budweiser");
		budweiser.setProductSupplierId(1L);
		budweiser.setSupplier(suppliera);
		budweiser = pdao.persist(budweiser);
		
		Product amstel = new Product();
		amstel.setName("Amstel");
		amstel.setProductSupplierId(2L);
		amstel.setSupplier(supplierb);
		amstel = pdao.persist(amstel);
		
		Product heineken = new Product();
		heineken.setName("Heineken");
		heineken.setProductSupplierId(3L);
		heineken.setSupplier(supplierc);
		heineken = pdao.persist(heineken);
	}
	
	/** Test the list of storeOrder
	 * it must not be empty
	 * test if it sending the right message format 
	 * 
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws JAXBException
	 */
	@Test
	public void finalMessageFormat() throws CamelExecutionException, IOException, InterruptedException, JAXBException{
		startEndpoint.requestBody(fileToString(getClass(), INPUT_MESSAGE));
		String response = mock.getExchanges().get(0).getIn().getBody(String.class);
		SaveStoreOrderReply reply = unmarshalReply(response);
		assertFalse(reply.getEntry().getStoreOrderList().getStoreOrder().isEmpty());
	}
	
	/** Send a message with no storeOrder
	 * it expects an error back
	 * 
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws JAXBException
	 */
	@Test
	public void finalWrongMessage() throws CamelExecutionException, IOException, InterruptedException, JAXBException{
		try{
			startEndpoint.requestBody(fileToString(getClass(), EMPTY_MESSAGE));
			mock.setExpectedMessageCount(1);
			mock.assertIsNotSatisfied();
		}catch(Exception e){
			e.getMessage();
		}
	}
	
	/** Test the DataType of the initial message and the final
	 * Must be equal and the same class
	 * In order to check the format of final message
	 * 
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws InterruptedException
	 * @throws JAXBException
	 */
	@Test
	public void testDataType() throws CamelExecutionException, IOException, InterruptedException, JAXBException{
		startEndpoint.requestBody(fileToString(getClass(), INPUT_MESSAGE));
		SaveStoreOrderRequest req = unmarshalReq(fileToString(getClass(), INPUT_MESSAGE));
		String response = mock.getExchanges().get(0).getIn().getBody(String.class);
		SaveStoreOrderReply reply = unmarshalReply(response);
		assertEquals(req.getEntry().getInsertDate().getClass(), reply.getEntry().getInsertDate().getClass());
	}

	/**Test how many messages received.
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
	
	/** convert a file into a string.
	 * 
	 * @param loader
	 * @param fileLocation
	 * @return
	 * @throws IOException
	 */
	public static String fileToString(Class<?> loader, String fileLocation) throws IOException {
		return IOUtils.toString(loader.getClassLoader().getResource(fileLocation), Charset.defaultCharset());
	}
	
	/** unmarshal to SaveStoreOrderReply.
	 * @param body
	 * @return
	 * @throws JAXBException
	 */
	public SaveStoreOrderReply unmarshalReply(String body) throws JAXBException{
		StringReader reader = new StringReader(body);
		JAXBContext jxb = JAXBContext.newInstance(SaveStoreOrderReply.class);
		Unmarshaller u = jxb.createUnmarshaller();
		return (SaveStoreOrderReply) u.unmarshal(reader);
	}
	/** Unmarshal to SaveStoreOrderReply.
	 * @param body
	 * @return
	 * @throws JAXBException
	 */
	public SaveStoreOrderRequest unmarshalReq(String body) throws JAXBException{
		StringReader reader = new StringReader(body);
		JAXBContext jxb = JAXBContext.newInstance(SaveStoreOrderRequest.class);
		Unmarshaller u = jxb.createUnmarshaller();
		return (SaveStoreOrderRequest) u.unmarshal(reader);
	}

}
