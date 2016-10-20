package au.com.supplierb.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;

import org.junit.Before;
import org.junit.Test;

import au.com.supplierb.messages.orderrequest.v1.SupplierBMessage;
import au.com.supplierb.messages.orderresponse.v1.SupplierBTranslatorMessage;
import au.com.supplierb.types.v1.ItemSupplierOrderListType;
import au.com.supplierb.types.v1.ItemSupplierOrderType;
import au.com.supplierb.types.v1.SupplierOrderRequestType;
import au.com.supplierb.wsdl.v1_0.ErrorMessage;

public class OrderRequestServiceTest {
	SupplierBMessage message;
	
	@Before
	public void setup(){
		message  = new SupplierBMessage();
		SupplierOrderRequestType request = new SupplierOrderRequestType();
		ItemSupplierOrderListType list = new ItemSupplierOrderListType();
		ItemSupplierOrderType order = new ItemSupplierOrderType();
		order.setProductId("1");
		order.setQuantity(BigInteger.ONE);
		list.getItemOrder().add(order);
		request.setItemSupplierOrderList(list);
		request.setSupplierOrderId(BigInteger.ONE);
		message.setSupplierOrderRequest(request);
	}
	
	/**Test the service's response message
	 * It must not be null
	 * @throws ErrorMessage
	 */
	@Test
	public void testMessage() throws ErrorMessage{
		OrderRequestService service = new OrderRequestService();
		SupplierBTranslatorMessage response = service.newOrder(message);
		assertNotNull(response);
	}
	/**The orderNumber in service's response cant be empty
	 * The supplierOrderId must be the same as the one in SupplierOrderRequest
	 * @throws ErrorMessage 
	 */
	@Test
	public void testOrderNumberInMessage() throws ErrorMessage{
		OrderRequestService service = new OrderRequestService();
		SupplierBTranslatorMessage response = service.newOrder(message);
		assertEquals(response.getSupplierOrderResponse().getSupplierOrderId(), message.getSupplierOrderRequest().getSupplierOrderId());
		assertNotNull(response.getSupplierOrderResponse().getOrderNumber());
	}
	
	/**When the mesage doesnt have a supplierOrderId it does not exit
	 * So it cant be processed
	 * The service will throw a ErrorMessage
	 * @throws ErrorMessage
	 */
	@Test(expected=ErrorMessage.class)
	public void testErrorMessage() throws ErrorMessage{
		OrderRequestService service = new OrderRequestService();
		SupplierBMessage emptySupplierIdMessage = new SupplierBMessage();
		emptySupplierIdMessage.setSupplierOrderRequest(new SupplierOrderRequestType());
		service.newOrder(emptySupplierIdMessage);
	}
}
