package au.com.gbstore.dbtranslator.processor;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.apache.log4j.Logger;

import au.com.gbstore.dbtranslator.dao.StoreOrderDao;
import au.com.gbstore.dbtranslator.model.ItemStoreOrder;
import au.com.gbstore.dbtranslator.model.StoreOrder;
import au.com.gbstore.messages.dbtranslator.enrichmentrequest.v1.EnrichmentRequest;
import au.com.gbstore.messages.dbtranslator.enrichmentresponse.v1.EnrichmentResponse;
import au.com.gbstore.types.dbtranslator.v1.DBOrderListType;
import au.com.gbstore.types.dbtranslator.v1.DBOrderType;
import au.com.gbstore.types.dbtranslator.v1.ItemOrderListType;
import au.com.gbstore.types.dbtranslator.v1.ItemOrderType;
import au.com.gbstore.types.dbtranslator.v1.ProductType;
import au.com.gbstore.types.dbtranslator.v1.StoreOrderType;
import au.com.gbstore.types.dbtranslator.v1.SupplierType;

/** bean processor of EnrichmentRoute.
 * @author lteixeira
 *
 */
public class EnrichmentProcessor {

	private Logger log = Logger.getLogger(this.getClass());
/**get message body.
 * enrich with Db informations
 * create a EnrichmentResponse with this data
 * and sent it back
 * 
 * @param body
 * @return
 * @throws Exception 
 * @throws NumberFormatException 
 */
public String process(String body) throws NumberFormatException, Exception{
		EnrichmentRequest request = unmarshal(body);
		List<StoreOrder> storeOrderList = enrich(request);
		
		EnrichmentResponse response = new EnrichmentResponse();
		response.setOrderlist(toOrderListXML(storeOrderList));
		return marshal(response);
	}
	
	/**convert the StoreOrderList to type of storeOrderList xsd.
	 * @param storeOrderList
	 * @return
	 */
	private DBOrderListType toOrderListXML(List<StoreOrder> storeOrderList) {
		DBOrderListType dolist = new DBOrderListType();
		
		for(StoreOrder order : storeOrderList){
			log.info("StoreOrderId: " + order.getStoreOrderId());
			DBOrderType xorder = new DBOrderType();
			xorder.setStoreOrderId(order.getStoreOrderId().toString());
			ItemOrderListType list = new ItemOrderListType();
			for(ItemStoreOrder item : order.getItemStoreOrderList()){
				ItemOrderType xitem = new ItemOrderType();
				log.info("ItemStoreOrderId:  " + item.getItemStoreOrderId());
				ProductType xproduct = new ProductType();
				xproduct.setInternalId(item.getProduct().getInternalId().toString());
				xproduct.setName(item.getProduct().getName());
				xproduct.setProductSupplierId(item.getProduct().getProductSupplierId().toString());
				
				SupplierType xsupplier = new  SupplierType();
				xsupplier.setName(item.getProduct().getSupplier().getName());
				xsupplier.setSupplierId(item.getProduct().getSupplier().getSupplierId().toString());
				
				xproduct.setSupplier(xsupplier);
				xitem.setProduct(xproduct);
				xitem.setQuantity(item.getQuantity());
				
				list.getItemOrder().add(xitem);
				
			}
			xorder.setItemOrderList(list);
			dolist.getStoreOrder().add(xorder);
		}
		return dolist;
	}
	
	/** Get all information about the order.
	 * @param request
	 * @return storeOrder list
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	private List<StoreOrder> enrich(EnrichmentRequest request) throws NumberFormatException, Exception{
		StoreOrderDao soDao = new StoreOrderDao();
		
		List<StoreOrder> storeOrderList = new ArrayList<StoreOrder>();
		
		for(StoreOrderType xorder: request.getStoreOrderList().getStoreOrder()){
			StoreOrder order = soDao.findById(Long.parseLong(xorder.getStoreOrderId()));
			storeOrderList.add(order);
		}
		return storeOrderList;
	}
	
	/** Marshal string to EnrichmentRequest.
	 * @param body -
	 * @return EnrichmentRequest -
	 * @throws JAXBException
	 */
	public EnrichmentRequest unmarshal(String body) throws JAXBException{
		StringReader reader = new StringReader(body);
		JAXBContext jxb = JAXBContext.newInstance(EnrichmentRequest.class);
		Unmarshaller u = jxb.createUnmarshaller();
		return (EnrichmentRequest) u.unmarshal(reader);
	}
	
	/** Unmarshal the entry saved to string.
	 * @param response - 
	 * @return a string -
	 * @throws JAXBException
	 */
	public String marshal(EnrichmentResponse response) throws JAXBException{
		StringWriter writer = new StringWriter();
		JAXBContext jxb = JAXBContext.newInstance(EnrichmentResponse.class);
		Marshaller m = jxb.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.marshal(response, writer);
		return writer.toString();
	}
}
