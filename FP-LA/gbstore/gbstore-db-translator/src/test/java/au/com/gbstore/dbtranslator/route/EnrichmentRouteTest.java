package au.com.gbstore.dbtranslator.route;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
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
import au.com.gbstore.dbtranslator.dao.StoreOrderDao;
import au.com.gbstore.dbtranslator.dao.SupplierDao;
import au.com.gbstore.dbtranslator.model.ItemStoreOrder;
import au.com.gbstore.dbtranslator.model.Product;
import au.com.gbstore.dbtranslator.model.Store;
import au.com.gbstore.dbtranslator.model.StoreOrder;
import au.com.gbstore.dbtranslator.model.Supplier;
import au.com.gbstore.messages.dbtranslator.enrichmentresponse.v1.EnrichmentResponse;

/**
 * @author lteixeira
 *
 */
public class EnrichmentRouteTest extends CamelTestSupport{
	private static final String INPUT_MESSAGE = "enrichmentRoute/msg/input/enrichment-request-message.xml";
	private static final String EMPTY_MESSAGE = "enrichmentRoute/msg/input/enrichment-empty-message.xml";
	//mocks the endpoint from OrderManager
		@Produce(uri = "direct:from")
		protected ProducerTemplate startEndpoint;

		//Endpoint sends message to DB
		@EndpointInject(uri = "mock:to")
		protected MockEndpoint mock;
		
		protected CamelContext createCamelContext() throws Exception {
			CamelContext context = new DefaultCamelContext();
			PropertiesComponent properties = new PropertiesComponent();
			properties.setLocation("classpath:enrichmentRoute/conf/au.com.gbstore.dbtranslator.cfg");
			context.addComponent("properties", properties);
			return context;
		}
		/**
		 * Call soap adapter' route builder
		 */
		@Override
		protected RouteBuilder createRouteBuilder() {
			return new EnrichmentRoute();
		}
		
		/**Build a database with certain informations.
		 * @throws Exception 
		 * 
		 */
		@BeforeClass
		public static void setUpDB() throws Exception{
			SupplierDao sdao = new SupplierDao();
			ProductDao pdao = new ProductDao();
			StoreDao stdao = new StoreDao();
			StoreOrderDao sodao = new StoreOrderDao();
			
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
			
			StoreOrder sorder = new StoreOrder();
			sorder.setStore(boteco);
			
			ItemStoreOrder item1 = new ItemStoreOrder();
			item1.setQuantity(BigInteger.valueOf(100));
			Product product = budweiser;
			item1.setProduct(product);
			item1.setStoreOrder(sorder);
			
			
			ItemStoreOrder item2 = new ItemStoreOrder();
			item2.setQuantity(BigInteger.valueOf(200));
			product = amstel;
			item2.setProduct(product);
			item2.setStoreOrder(sorder);

			ItemStoreOrder item3 = new ItemStoreOrder();
			item3.setQuantity(BigInteger.valueOf(300));
			product = heineken;
			item3.setProduct(product);
			item3.setStoreOrder(sorder);
			
			sorder.getItemStoreOrderList().add(item1);
			sorder.getItemStoreOrderList().add(item2);
			sorder.getItemStoreOrderList().add(item3);
			sorder = sodao.persist(sorder);
			
			StoreOrder sorder2 = new StoreOrder();
			sorder2.setStore(bar);
			item1.setStoreOrder(sorder2);
			item2.setStoreOrder(sorder2);
			sorder.getItemStoreOrderList().add(item1);
			sorder.getItemStoreOrderList().add(item2);
			sorder2 = sodao.persist(sorder2);
			System.out.println(sorder.getStoreOrderId());
			System.out.println(sorder2.getStoreOrderId());
		}
		/** Tests the message that will be send to OM.
		 * The response must have a productSupplierId
		 * 
		 * @throws CamelExecutionException
		 * @throws IOException
		 * @throws JAXBException
		 */
		@Test
		public void testProductSupplierId() throws CamelExecutionException, IOException, JAXBException{
			startEndpoint.sendBody(fileToString(getClass(), INPUT_MESSAGE));
			String message = mock.getExchanges().get(0).getIn().getBody(String.class);
			EnrichmentResponse response = unmarshal(message);
			assertEquals(response.getOrderlist().getStoreOrder().get(0).getItemOrderList().getItemOrder().get(0).getProduct().getProductSupplierId(), "1");
		}
		
		/** Test the message that will be send to OM.
		 * it must have supplier information after be enhanced
		 * 
		 * @throws CamelExecutionException
		 * @throws IOException
		 * @throws JAXBException
		 */
		@Test
		public void testSupplierInfo() throws CamelExecutionException, IOException, JAXBException{
			startEndpoint.sendBody(fileToString(getClass(), INPUT_MESSAGE));
			String message = mock.getExchanges().get(0).getIn().getBody(String.class);
			EnrichmentResponse response = unmarshal(message);
			assertTrue(response.getOrderlist().getStoreOrder().get(0).getItemOrderList().getItemOrder().get(0).getProduct().getSupplier().getSupplierId().contains("1"));
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
		/**Send an empty message and it is supposed to get no answer.
		 * @throws JAXBException
		 * @throws CamelExecutionException
		 * @throws IOException
		 * @throws InterruptedException
		 */
		@Test
		public void testEmptyFinalMessage() throws JAXBException, CamelExecutionException, IOException, InterruptedException {
			try{
				startEndpoint.sendBody(fileToString(getClass(), EMPTY_MESSAGE));
			}catch(Exception e){
				mock.setExpectedMessageCount(1);
				mock.assertIsNotSatisfied();
			}
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
		
		/** Unmarshal the message to EnrichmentResponse
		 * @param body
		 * @return
		 * @throws JAXBException
		 */
		public EnrichmentResponse unmarshal(String body) throws JAXBException{
			StringReader reader = new StringReader(body);
			JAXBContext jxb = JAXBContext.newInstance(EnrichmentResponse.class);
			Unmarshaller u = jxb.createUnmarshaller();
			return (EnrichmentResponse) u.unmarshal(reader);
		}

}
