package au.com.gbstore.dbtranslator.processor;

import java.io.StringReader;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import au.com.gbstore.dbtranslator.dao.SupplierOrderDao;
import au.com.gbstore.dbtranslator.model.SupplierOrder;
import au.com.gbstore.messages.dbtranslator.saveordernumber.v1.SaveOrderNumber;
import au.com.gbstore.types.dbtranslator.v1.StatusType;
import au.com.gbstore.types.dbtranslator.v1.SupplierOrderResponseType;

/**bean processor of SaveOrderNumberRoute.
 * @author lteixeira
 *
 */
public class SaveOrderNumberProcessor implements Processor{

/** Gets the body message.
	 * unmarshal to SaveOrderNumber
	 * and update in DB
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	public void process(Exchange exchange) throws Exception {
		Object body = exchange.getIn().getBody();
		SaveOrderNumber request = unmarshal(body.toString());
		SupplierOrder order = saveOrderNumber(request.getSupplierOrderResponse());
		exchange.getIn().setBody(order);
	}
	
	/** Update SupplierOrder with orderNumber, new status, and updateDate.
	 * @param supOrd - supplierOrder
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public SupplierOrder saveOrderNumber(SupplierOrderResponseType supOrd) throws NumberFormatException, Exception{
		SupplierOrderDao dao = new SupplierOrderDao();
		SupplierOrder supplierOrder = dao.findById(Long.parseLong(supOrd.getSupplierOrderId()));
		supplierOrder.setOrderNumber(supOrd.getOrderNumber());
		supplierOrder.setStatus(StatusType.ORDERED.toString());
		supplierOrder.setUpdateDate(new Date());
		return dao.persist(supplierOrder);
	}
	
	/** Marshal string to SaveStoreOrderRequest.
	 * @param body - message body
	 * @return - SaveOrderNumber
	 * @throws JAXBException
	 */
	public SaveOrderNumber unmarshal(String body) throws JAXBException{
		StringReader reader = new StringReader(body);
		JAXBContext jxb = JAXBContext.newInstance(SaveOrderNumber.class);
		Unmarshaller u = jxb.createUnmarshaller();
		return (SaveOrderNumber) u.unmarshal(reader);
	}
}
