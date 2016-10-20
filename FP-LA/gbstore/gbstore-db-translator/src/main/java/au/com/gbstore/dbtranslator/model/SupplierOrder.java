package au.com.gbstore.dbtranslator.model;


import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import au.com.gbstore.types.dbtranslator.v1.StatusType;

/**SupplierOrder model.
 * @author lteixeira
 */
@Entity
public class SupplierOrder {

private String status=StatusType.PROCESSING.toString();
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private Long supplierOrderId;
@Temporal(TemporalType.TIMESTAMP)
private Date insertDate = new Date();
@Temporal(TemporalType.TIMESTAMP)
private Date updateDate = null;
private String orderNumber;
@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
private List<ItemSupplierOrder> itemSupplierOrderList;
@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
private Supplier supplier;
@ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
@JoinTable(joinColumns={@JoinColumn(name="supplierOrderId")},
        inverseJoinColumns={@JoinColumn(name="storeOrderId")})
private List<StoreOrder> storeOrderList;                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      

/**SupplierOrder constructor.
 * 
 */
public SupplierOrder() {
	}
	/**get Status.
	 * @return
	 */
	public String getStatus() {
		return status;
	}
	/**set Status.
	 * @param status
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**get SupplierOrderId.
	 * @return
	 */
	public Long getSupplierOrderId() {
		return supplierOrderId;
	}
	/**set SupplierOrderId.
	 * @param supplierOrderId
	 */
	public void setSupplierOrderId(Long supplierOrderId) {
		this.supplierOrderId = supplierOrderId;
	}
	/**get InsetDate.
	 * @return
	 * @throws DatatypeConfigurationException
	 */
	public XMLGregorianCalendar getInsertDate() throws DatatypeConfigurationException {
		if(insertDate == null)
			return null;
		GregorianCalendar gCalendar = new GregorianCalendar();
		gCalendar.setTime(insertDate);
		return  DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
	}
	/**set insertDate.
	 * @param insertDate
	 */
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}
	/**get OrderNumber.
	 * @return
	 */
	public String getOrderNumber() {
		return orderNumber;
	}
	/**set OrderNumber.
	 * @param orderNumber
	 */
	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}
	/**get UpdateDate.
	 * @return
	 * @throws DatatypeConfigurationException
	 */
	public XMLGregorianCalendar getUpdateDate() throws DatatypeConfigurationException {
		if(updateDate==null)
			return null;
		GregorianCalendar gCalendar = new GregorianCalendar();
		gCalendar.setTime(updateDate);
		return  DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
	}
	/**set UpdateDate.
	 * @param updatedDate
	 */
	public void setUpdateDate(Date updatedDate) {
		this.updateDate = updatedDate;
	}
	/**get Supplier.
	 * @return
	 */
	public Supplier getSupplier() {
		return supplier;
	}
	/**set Supplier.
	 * @param supplier
	 */
	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}
	/**get StoreOrderlist.
	 * @return
	 */
	public List<StoreOrder> getStoreOrderList() {
		return storeOrderList;
	}
	/**set storeOrderList.
	 * @param storeOrderList
	 */
	public void setStoreOrderList(List<StoreOrder> storeOrderList) {
		this.storeOrderList = storeOrderList;
	}
	/**get itemSupplierOrderList.
	 * @return
	 */
	public List<ItemSupplierOrder> getItemSupplierOrderList() {
		return itemSupplierOrderList;
	}

	public void setItemSupplierOrderList(List<ItemSupplierOrder> itemSupplierOrderList) {
		this.itemSupplierOrderList = itemSupplierOrderList;
	}
	
}
