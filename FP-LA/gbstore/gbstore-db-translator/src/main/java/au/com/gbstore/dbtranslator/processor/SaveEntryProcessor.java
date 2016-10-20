package au.com.gbstore.dbtranslator.processor;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.log4j.Logger;

import au.com.gbstore.dbtranslator.dao.EntryDao;
import au.com.gbstore.dbtranslator.dao.ProductDao;
import au.com.gbstore.dbtranslator.dao.StoreDao;
import au.com.gbstore.dbtranslator.model.Entry;
import au.com.gbstore.dbtranslator.model.ItemStoreOrder;
import au.com.gbstore.dbtranslator.model.Product;
import au.com.gbstore.dbtranslator.model.Store;
import au.com.gbstore.dbtranslator.model.StoreOrder;
import au.com.gbstore.messages.dbtranslator.savestoreorderreply.v1.SaveStoreOrderReply;
import au.com.gbstore.messages.dbtranslator.savestoreorderrequest.v1.SaveStoreOrderRequest;
import au.com.gbstore.types.dbtranslator.v1.EntryReqType;
import au.com.gbstore.types.dbtranslator.v1.EntryType;
import au.com.gbstore.types.dbtranslator.v1.ItemStoreOrderType;
import au.com.gbstore.types.dbtranslator.v1.ProductStoreType;
import au.com.gbstore.types.dbtranslator.v1.StoreOrderListType;
import au.com.gbstore.types.dbtranslator.v1.StoreOrderType;
import au.com.gbstore.types.dbtranslator.v1.StoreOrderType.ItemStoreOrderList;
import au.com.gbstore.types.dbtranslator.v1.StoreType;

/**bean processor of SaveEntryRoute.
 * @author lteixeira
 *
 */
public class SaveEntryProcessor{

	private Logger log = Logger.getLogger(this.getClass());
	
	/** Gets body and unmarshal to SaveStoreOrderRequest.
	 * Gets the entry saved marshal it
	 * @see org.apache.camel.Processor#process(org.apache.camel.Exchange)
	 */
	public String process(String body) throws Exception {
		//Body from the OM route - Entry - Store Order Request
		SaveStoreOrderRequest req = unmarshal(body);
		Entry entry = saveEntry(req.getEntry());
		EntryType ent = toEntryXml(entry);
		SaveStoreOrderReply reply = new SaveStoreOrderReply();
		reply.setEntry(ent);
		return marshal(reply);
	}
	
	/**Sends to DB to save.
	 * @param request - entryReqType
	 * @return entry - Entry
	 * @throws Exception 
	 */
	private Entry saveEntry(EntryReqType request) throws Exception {
		log.debug("Converting the message to an DB object and persisting it");
		EntryDao dao = new EntryDao();
		Entry entry = toEntryObject(request);
		return dao.persist(entry);
	}
	
	/** Converts to Entry model.
	 * @param request
	 * @return
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	private Entry toEntryObject(EntryReqType request) throws NumberFormatException, Exception{
		log.info("Converting the message to an DB object");
		StoreDao storedao = new StoreDao();
		ProductDao prodDao = new ProductDao();
		Entry entry = new Entry();
		entry.setInsertDate(request.getInsertDate().toGregorianCalendar().getTime());
		entry.setProcessDate(request.getProcessDate().toGregorianCalendar().getTime());
		for(StoreOrderType order : request.getStoreOrderList().getStoreOrder()){
			StoreOrder sorder = new StoreOrder();
			log.info("Searching store into DB");
			Store store = storedao.findById(Long.parseLong(order.getStore().getStoreId()));
			sorder.setStore(store);
			for(ItemStoreOrderType itens : order.getItemStoreOrderList().getItemStoreOrder()){
				ItemStoreOrder item = new ItemStoreOrder();
				item.setQuantity(itens.getQuantity());
				log.info("Searching product into DB");
				Product product = prodDao.findById(Long.parseLong(itens.getProduct().getInternalId()));
				item.setProduct(product);
				item.setStoreOrder(sorder);
				sorder.getItemStoreOrderList().add(item);
			}
			entry.getStoreOrderList().add(sorder);
		}
		return entry;
	}
	
	/** Converts the model Entry to EntryType from canonical model.
	 * @param entry
	 * @return
	 * @throws DatatypeConfigurationException 
	 */
	private EntryType toEntryXml(Entry entry) throws DatatypeConfigurationException{
		EntryType xmlEntry = new EntryType();
		xmlEntry.setEntryId(entry.getEntryId().toString());
		xmlEntry.setInsertDate(entry.getInsertDate());
		xmlEntry.setProcessDate(entry.getProcessDate());
		StoreOrderListType sol = new StoreOrderListType();
		for(StoreOrder order: entry.getStoreOrderList()){
			StoreOrderType xmlOrder = new StoreOrderType();
			ItemStoreOrderList xmlList = new ItemStoreOrderList();
			xmlOrder.setStoreOrderId(order.getStoreOrderId().toString());
			
			StoreType store = new StoreType();
			store.setStoreId(order.getStore().getStoreId().toString());
			store.setName(order.getStore().getName());
			xmlOrder.setStore(store);
			for(int j = 0; j<order.getItemStoreOrderList().size(); j++){
				ItemStoreOrder item = order.getItemStoreOrderList().get(j);
				ItemStoreOrderType xmlItem = new ItemStoreOrderType();
				
				ProductStoreType product = new ProductStoreType();
				product.setInternalId(item.getProduct().getInternalId().toString());
				product.setName(item.getProduct().getName());
				
				xmlItem.setProduct(product);
				xmlItem.setQuantity(item.getQuantity());
				
				xmlList.getItemStoreOrder().add(xmlItem);
			}
			xmlOrder.setItemStoreOrderList(xmlList);
			sol.getStoreOrder().add(xmlOrder);
			
		}
		xmlEntry.setStoreOrderList(sol);
		return xmlEntry;
	}
	/** Marshal string to SaveStoreOrderRequest.
	 * @param body
	 * @return
	 * @throws JAXBException
	 */
	public SaveStoreOrderRequest unmarshal(String body) throws JAXBException{
		StringReader reader = new StringReader(body);
		JAXBContext jxb = JAXBContext.newInstance(SaveStoreOrderRequest.class);
		Unmarshaller u = jxb.createUnmarshaller();
		return (SaveStoreOrderRequest) u.unmarshal(reader);
	}
	
	/** Unmarshal the entry saved to string.
	 * @param entry
	 * @return
	 * @throws JAXBException
	 */
	public String marshal(SaveStoreOrderReply reply) throws JAXBException{
		StringWriter writer = new StringWriter();
		JAXBContext jxb = JAXBContext.newInstance(SaveStoreOrderReply.class);
		Marshaller m = jxb.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.marshal(reply, writer);
		return writer.toString();
	}
}
