package au.com.suppliera.route;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.spring.SpringCamelContext;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import au.com.suppliera.model.SupplierOrderRequest;
import au.com.suppliera.model.SupplierOrderRequest.OrderList;
import au.com.suppliera.model.SupplierOrderResponse;
import au.com.suppliera.service.SupplierOrderService;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class SupplierARouteTest extends CamelSpringTestSupport{
	private static final String INPUT_MESSAGE = "supplierARoute/msg/input/supplier-order-request.json";
	private SupplierOrderRequest request;
	private SupplierOrderResponse response;

	@Override
	protected AbstractApplicationContext createApplicationContext() {
		return new ClassPathXmlApplicationContext("META-INF/spring/camel-context.xml");
	}
	protected CamelContext createCamelContext() throws Exception {
		CamelContext context = SpringCamelContext.springCamelContext(createApplicationContext());
		return context;
	}

	@Override
	protected RouteBuilder createRouteBuilder() throws Exception {
		return new SupplierARoute();
	}
	@Before
	public void setUp(){
		request = new SupplierOrderRequest();
		request.setOrderId(30L);
		request.setOrderList(new OrderList());
		
		response = new SupplierOrderResponse();
		response.setSupplierOrderId(request.getOrderId());
		response.setOrderNumber(200L);
	}
	
	/**Test if the service has generated a OrderNumber.
	 * And get the response to SupplierATranslator
	 * test the response's supplierOrderId is the same as the request
	 * @throws Exception 
	 */
	@Test
	public void testFinalMessage() throws Exception {
		ProducerTemplate template = createCamelContext().createProducerTemplate();
		
		String body = template.requestBody("direct:orderNumber", fileToString(getClass(), INPUT_MESSAGE), String.class);

		JSONObject bodyJson = (JSONObject) JSONSerializer.toJSON(body);
    	assertNotNull(bodyJson.get("orderNumber"));
		assertEquals(30L, bodyJson.getLong("supplierOrderId"));
	}
	
	/**Send a empty object to service and get a NullPointerException
	 * @throws Exception 
	 * 
	 */
	@Test(expected = NullPointerException.class)
	public void testServiceEmptyRequest() throws Exception{
		SupplierOrderService service = new SupplierOrderService();
		service.getResponse(null);
	}

	/**convert a file into a string
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
