package au.com.supplierb.service;

import java.math.BigInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.supplier.persistence.dao.OrderNumberGenerator;
import au.com.supplierb.messages.orderrequest.v1.SupplierBMessage;
import au.com.supplierb.messages.orderresponse.v1.SupplierBTranslatorMessage;
import au.com.supplierb.types.v1.SupplierOrderResponseType;
import au.com.supplierb.wsdl.v1_0.ErrorMessage;
import au.com.supplierb.wsdl.v1_0.NewOrderWS;

/**Soap service of SupplierB. 
 * It gets a request from SupplierBTranslator and generate a OrderNumber
 * http://localhost:8080/supplierb/request?wsdl
 * @author lteixeira
 *
 */
public class OrderRequestService implements NewOrderWS{
	private Log log = LogFactory.getLog(getClass());
	
	/**This method will build the response with a supplierOrderId and a OrderNumber.
	 * @param parameters
	 * @return response
	 */
	public SupplierBTranslatorMessage newOrder(SupplierBMessage parameters) throws ErrorMessage {
		
		if(parameters.getSupplierOrderRequest().getSupplierOrderId() == null){
			throw new ErrorMessage();
		}
		
		SupplierBTranslatorMessage response = new SupplierBTranslatorMessage();
		SupplierOrderResponseType sor = new SupplierOrderResponseType();
		sor.setSupplierOrderId(parameters.getSupplierOrderRequest().getSupplierOrderId());
		
		log.info("SupB: Getting a supplier order response: supplierOrderId = " + sor.getSupplierOrderId());
		
		OrderNumberGenerator generator = new OrderNumberGenerator();
		Long orderNumber = null;
		try {
			orderNumber = generator.generateNewOrderNumber();
		} catch (Exception e) {
			e.printStackTrace();
			throw new ErrorMessage();
		}
		
		sor.setOrderNumber(BigInteger.valueOf(orderNumber));
		response.setSupplierOrderResponse(sor);
		return response;
	}

}
