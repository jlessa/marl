package au.com.marlo.trainning.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

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
		 
	
	private Client createClient(){
		Client client = new Client();		
		return client;
	}
	
	private List<Order> createOrderList(){
		List<Order> orders = new ArrayList<>();		
		return orders;
	}
	
	private List<Product> createProductList(){
		List<Product> products = new ArrayList<>();		
		return products;
	}
	
	@Test
	public void testGetOrders() {
		ProjectRestServer server = new ProjectRestServer();
		server.setModelDao(mock(ModelDao.class));
		when(server.getModelDao().getById(Client.class, 1)).thenReturn(createClient());
		when(server.getModelDao().getOrdersByUser(createClient())).thenReturn(createOrderList());
		Response response = server.getOrders("1");
		assertEquals(response.getStatus(), 200);		
	}

	@Test
	public void testGetProducts() {
		ProjectRestServer server = new ProjectRestServer();
		server.setModelDao(mock(ModelDao.class));		
		when(server.getModelDao().getAllProducts()).thenReturn(createProductList());
		Response response = server.getProducts();
		assertEquals(response.getStatus(), 200);
	}

}
