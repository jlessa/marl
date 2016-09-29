package au.com.marlo.trainning.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.any;



import org.junit.Test;

import au.com.marlo.trainning.rest_client.ProjectRestClient;

public class ProjectRestClientTest {

	@Test
	public void testGetClientFromAPI() {	
		ProjectRestClient spyClient = spy(new ProjectRestClient());
		doReturn("OK").when(spyClient).getResponse(any(String.class));		
		String response = spyClient.getClientFromAPI("1");
		assertEquals("OK", response);
	}

	@Test
	public void testGetAllProducts() {
		ProjectRestClient spyClient = spy(new ProjectRestClient());
		doReturn("OK").when(spyClient).getAllProducts();		
		String response = spyClient.getAllProducts();
		assertEquals("OK", response);
	}

}
