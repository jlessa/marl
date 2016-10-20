package au.com.gbstore.dbtranslator.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import au.com.gbstore.types.dbtranslator.v1.StatusType;

/**StoreOrder model.
 * @author lteixeira
 *
 */
@Entity
public class StoreOrder {

@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private Long storeOrderId;
private String status = StatusType.PROCESSING.toString();
@Temporal(TemporalType.TIMESTAMP)
private Date insertDate = new Date();
@Temporal(TemporalType.TIMESTAMP)
private Date updateDate;
@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
private List<ItemStoreOrder> itemStoreOrderList = new ArrayList<ItemStoreOrder>();
@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}) 
private Store store;

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
	/**get InsetDate.
	 * @return
	 */
	public Date getInsertDate() {
		return insertDate;
	}
	/**set insertDate.
	 * @param insertDate
	 */
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}
	/**get StoreOrderId.
	 * @return
	 */
	public Long getStoreOrderId() {
		return storeOrderId;
	}
	/**set StoreOrderid.
	 * @param storeOrderId
	 */
	public void setStoreOrderId(Long storeOrderId) {
		this.storeOrderId = storeOrderId;
	}
	/**get updateDate.
	 * @return
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
	/**set updateDate.
	 * @param updateDate
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	/**get itemStoreOrderList.
	 * @return
	 */
	public List<ItemStoreOrder> getItemStoreOrderList() {
		return itemStoreOrderList;
	}
	/**set ItemStoreOrderList.
	 * @param itemStoreOrderList
	 */
	public void setItemStoreOrderList(List<ItemStoreOrder> itemStoreOrderList) {
		this.itemStoreOrderList = itemStoreOrderList;
	}
	/**get Store.
	 * @return
	 */
	public Store getStore() {
		return store;
	}
	/**set Store.
	 * @param store
	 */
	public void setStore(Store store) {
		this.store = store;
	}
	
}
