package au.com.gbstore.dbtranslator.processor;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import au.com.gbstore.dbtranslator.dao.ProductDao;
import au.com.gbstore.dbtranslator.dao.StoreOrderDao;
import au.com.gbstore.dbtranslator.dao.SupplierDao;
import au.com.gbstore.dbtranslator.dao.SupplierOrderDao;
import au.com.gbstore.dbtranslator.model.ItemSupplierOrder;
import au.com.gbstore.dbtranslator.model.Product;
import au.com.gbstore.dbtranslator.model.StoreOrder;
import au.com.gbstore.dbtranslator.model.Supplier;
import au.com.gbstore.dbtranslator.model.SupplierOrder;
import au.com.gbstore.messages.dbtranslator.savesupplierordersreply.v1.SaveSupplierOrderReply;
import au.com.gbstore.messages.dbtranslator.savesupplierordersrequest.v1.SaveSupplierOrderRequest;
import au.com.gbstore.types.dbtranslator.v1.ItemOrderListType;
import au.com.gbstore.types.dbtranslator.v1.ItemOrderType;
import au.com.gbstore.types.dbtranslator.v1.ItemSupplierOrderType;
import au.com.gbstore.types.dbtranslator.v1.ProductType;
import au.com.gbstore.types.dbtranslator.v1.SupplierOrderReplyType;
import au.com.gbstore.types.dbtranslator.v1.SupplierOrderType;
import au.com.gbstore.types.dbtranslator.v1.SupplierOrderType.ItemSupplierOrderList;
import au.com.gbstore.types.dbtranslator.v1.SupplierType;

/**processor of SaveSupplierOrderRoute.
 * @author lteixeira
 *
 */
public class SaveSupplierOrderProcessor implements Processor{

private SupplierOrderDao sODao = new SupplierOrderDao();
private StoreOrderDao storeODao = new StoreOrderDao();
private ProductDao pDao = new ProductDao();
private SupplierDao supplierDao =new SupplierDao();

/**gets the message body.
 * unmarshal it to SaveSupplierOrderRequest
 * create a response and send it back
 */
public void process(Exchange exchange) throws Exception {
		String body = exchange.getIn().getBody(String.class);
		
		SaveSupplierOrderRequest request = unmarshal(body);
		List<SupplierOrder> orderList  = saveSupplierOrder(request);
		
		SaveSupplierOrderReply response = toResponse(orderList);
		
		String resp = marshal(response);
		exchange.getOut().setBody(resp);
}
	private SaveSupplierOrderReply toResponse(List<SupplierOrder> orderList) {
		SaveSupplierOrderReply response = new SaveSupplierOrderReply();
		for(SupplierOrder order : orderList ){
			SupplierOrderReplyType suporder = new SupplierOrderReplyType();
			
			SupplierType supplier = new SupplierType();
			supplier.setName(order.getSupplier().getName());
			supplier.setSupplierId(order.getSupplier().getSupplierId().toString());
			suporder.setSupplier(supplier);
			
			suporder.setSupplierOrderId(order.getSupplierOrderId().toString());
			ItemOrderListType itemList = new ItemOrderListType();
			for(ItemSupplierOrder item : order.getItemSupplierOrderList()){
				ItemOrderType itemXml = new ItemOrderType();
				itemXml.setQuantity(item.getQuantity());
				
				ProductType product = new ProductType();
				product.setInternalId(item.getProduct().getInternalId().toString());
				product.setName(item.getProduct().getName());
				product.setProductSupplierId(item.getProduct().getProductSupplierId().toString());
				product.setSupplier(supplier);
				itemXml.setProduct(product);
				
				itemList.getItemOrder().add(itemXml);
			}
			suporder.setItemSupplierOrderList(itemList);
			response.getSupplierOrder().add(suporder);
		}
		return response;
	}
	/**Creates and  save a SupplierOrder.
	 * @param request
	 * @throws Exception 
	 */
	private List<SupplierOrder> saveSupplierOrder(SaveSupplierOrderRequest request)
			throws Exception {
		List<SupplierOrder> supOrdList = new ArrayList<SupplierOrder>();
		
		for(SupplierOrderType supplierOrder: request.getSupplierOrder()){
			SupplierOrder order = new SupplierOrder();
			order.setOrderNumber(null);
			Long supplierId = Long.parseLong(supplierOrder.getSupplier().getSupplierId());
			Supplier supplier = supplierDao.findById(supplierId);
			order.setSupplier(supplier);
			
			List<ItemSupplierOrder> list = new ArrayList<ItemSupplierOrder>();
			List<StoreOrder> storeOrderList = new ArrayList<StoreOrder>();
			ItemSupplierOrderList isolist = supplierOrder.getItemSupplierOrderList();
			for(ItemSupplierOrderType itemSupplier : isolist.getItemSupplierOrder()){
				ItemSupplierOrder item = new ItemSupplierOrder();
				item.setQuantity(itemSupplier.getQuantity());
				
				Long productId = Long.parseLong(itemSupplier.getProduct().getInternalId());
				Product product = pDao.findById(productId);
				item.setProduct(product);
				list.add(item);
				
				for(String id : itemSupplier.getStoreOrderIdList().getStoreOrderId()){
					StoreOrder storeOrder = storeODao.findById(Long.parseLong(id));
					storeOrderList.add(storeOrder);
				}
				item.setSupplierOrder(order);
				order.setStoreOrderList(storeOrderList);
			}

			order.setItemSupplierOrderList( list);
			order.setStoreOrderList(storeOrderList);
			supOrdList.add(sODao.persist(order));
		}
		return supOrdList;
	}

	/** Marshal string to SupplierOrder.
	 * @param body
	 * @return
	 * @throws JAXBException
	 */
	public SaveSupplierOrderRequest unmarshal(String body) throws JAXBException{
		StringReader reader = new StringReader(body);
		JAXBContext jxb = JAXBContext.newInstance(SaveSupplierOrderRequest.class);
		Unmarshaller u = jxb.createUnmarshaller();
		return (SaveSupplierOrderRequest) u.unmarshal(reader);
	}
	
	/** Marshal string to SupplierOrder.
	 * @param body
	 * @return
	 * @throws JAXBException
	 */
	public String marshal(SaveSupplierOrderReply response) throws JAXBException{
		StringWriter writer = new StringWriter();
		JAXBContext jxb = JAXBContext.newInstance(SaveSupplierOrderReply.class);
		Marshaller m = jxb.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.marshal(response, writer);
		return writer.toString();
	}
}
