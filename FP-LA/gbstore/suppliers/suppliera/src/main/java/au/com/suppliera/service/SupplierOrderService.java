package au.com.suppliera.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.supplier.persistence.dao.OrderNumberGenerator;
import au.com.suppliera.model.SupplierOrderRequest;
import au.com.suppliera.model.SupplierOrderResponse;

/**Process the order and return a response with OrderNumber
 * @author lteixeira
 *
 */
public class SupplierOrderService {
	
	private Log log = LogFactory.getLog(getClass());
	
	/**This method will build the response with SupplierOrderId and a orderNumber
	 * @param req
	 * @return
	 * @throws Exception 
	 */
	public SupplierOrderResponse getResponse(SupplierOrderRequest req) throws Exception{
		//if orderId is null, there is no order and it wont be processed
		if(req.getOrderId()==null) return null;
		
		SupplierOrderResponse sor = new SupplierOrderResponse();
		sor.setSupplierOrderId(req.getOrderId());
		sor.setOrderNumber(generateOrderNumber());
		log.info("SupA: Getting a supplier order response: id = " + sor.getSupplierOrderId() + " OrderNumber = " + sor.getOrderNumber());
		return sor;
	}
	
	public Long generateOrderNumber() throws Exception{
		OrderNumberGenerator generator = new OrderNumberGenerator();
		return generator.generateNewOrderNumber();
	}
}
