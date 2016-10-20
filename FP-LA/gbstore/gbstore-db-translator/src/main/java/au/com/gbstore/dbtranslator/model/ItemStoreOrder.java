package au.com.gbstore.dbtranslator.model;

import java.math.BigInteger;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**ItemStoreOrder model.
 * @author lteixeira
 *
 */
@Entity
public class ItemStoreOrder {

private BigInteger quantity;
@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private Long itemStoreOrderId;
@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
private Product product;
@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
private StoreOrder storeOrder;

/**Constructor.
 */
public ItemStoreOrder() {
	}
	/**get quantity.
	 * @return
	 */
	public BigInteger getQuantity() {
		return quantity;
	}
	/**set Quantity.
	 * @param bigInteger
	 */
	public void setQuantity(BigInteger bigInteger) {
		this.quantity = bigInteger;
	}
	/**get ItemStoreOrderId.
	 * @return
	 */
	public Long getItemStoreOrderId() {
		return itemStoreOrderId;
	}
	/**set ItemStoreOrderId.
	 * @param itemStoreOrderId
	 */
	public void setItemStoreOrderId(Long itemStoreOrderId) {
		this.itemStoreOrderId = itemStoreOrderId;
	}
	/**get Product.
	 * @return
	 */
	public Product getProduct() {
		return product;
	}
	/**set Product.
	 * @param product
	 */
	public void setProduct(Product product) {
		this.product = product;
	}
	/**get StoreOrder.
	 * @return
	 */
	public StoreOrder getStoreOrder() {
		return storeOrder;
	}
	/**set StoreOrder.
	 * @param storeOrder
	 */
	public void setStoreOrder(StoreOrder storeOrder) {
		this.storeOrder = storeOrder;
	}

}
