package au.com.marlo.trainning.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import au.com.marlo.training.dao.ModelDao;
import au.com.marlo.training.entity.Client;
import au.com.marlo.training.entity.Order;
import au.com.marlo.training.entity.Product;
import au.com.marlo.trainning.restservice.ProjectRestServer;

@RunWith(MockitoJUnitRunner.class)
public class ProjectRestServerTest {

	@Mock private ModelDao modelDao;
	@InjectMocks ProjectRestServer project;
	
	private Client client;	
	private Product product;
	private Order order;
	private List<Order> orders;
	private List<Product> products;
	
	private Client initClient(){
		client = new Client();
		client.setAge(25);
		client.setDocumentId(1);
		client.setAvailable_account_credit(new Float(200));
		client.setGender("M");
		client.setName("Fulano");	
		return client;
	}
	
	private Product initProduct(){
		product = new Product();
		product.setDescription("Test Product");
		product.setCategory("Test");
		product.setPrice(10);	
		product.setCode(1);;
		return product;
	}
	
	private Order initOrder(){
		order = new Order();
		order.setOrderNumber(1);
		order.setClient(initClient());
		order.getProducts().add(initProduct());
		return order;
	}
	
	private List<Order> initOrderList(){
		orders = new ArrayList<>();		
		orders.add(initOrder());				
		return orders;
	}
	
	private List<Product> initProductList(){
		products = new ArrayList<>();	
		products.add(initProduct());
		return products;
	}
	
	private ObjectMapper createMapper(){
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
        return mapper;
	}
	
	private String ordersToJson(List<Order> orders) throws JsonGenerationException, JsonMappingException, IOException{
		ObjectMapper mapper = createMapper();
		return mapper.writeValueAsString(orders);
	}
	
	private String productsToJson(List<Product> products) throws JsonGenerationException, JsonMappingException, IOException{
		ObjectMapper mapper = createMapper();
		return mapper.writeValueAsString(products);
	}
	
	@Test
	public void testGetOrders() throws JsonParseException, JsonMappingException, IOException {
		ProjectRestServer server = new ProjectRestServer();				
		server.setModelDao(mock(ModelDao.class));
		
		when(server.getModelDao().getById(Client.class, 1)).thenReturn(initClient());
		when(server.getModelDao().getOrdersByUser(any(Client.class))).thenReturn(initOrderList());		
		
		Response response = server.getOrders("1");
		
		assertEquals(response.getStatus(), 200);
		assertEquals(ordersToJson(orders), response.getEntity());
				
	}

	@Test
	public void testGetProducts() throws JsonGenerationException, JsonMappingException, IOException {
		ProjectRestServer server = new ProjectRestServer();				
		server.setModelDao(mock(ModelDao.class));
		
		when(server.getModelDao().getAllProducts()).thenReturn(initProductList());			
		
		Response response = server.getProducts();			
		
		assertEquals(response.getStatus(), 200);
		assertEquals(productsToJson(products), response.getEntity());
	}

}
