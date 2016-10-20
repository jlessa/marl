package au.com.gbstore.dbtranslator.model;

import java.math.BigInteger;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**ItemSupplierOrder model.
 * @author lteixeira
 *
 */
/**
 * @author lteixeira
 *
 */
@Entity
public class ItemSupplierOrder {

@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private long itemSupplierOrderId;
private BigInteger quantity;
@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
private Product product;
@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
private SupplierOrder supplierOrder;

/**Constructor.
 * 
 */
public ItemSupplierOrder() {
	}
	/** get ItemSupplierOrderId.
	 * @return
	 */
	public long getItemSupplierOrderId() {
		return itemSupplierOrderId;
	}
	/** set ItemSupplierOrderId.
	 * @param itemSupplierOrderId
	 */
	public void setItemSupplierOrderId(long itemSupplierOrderId) {
		this.itemSupplierOrderId = itemSupplierOrderId;
	}
	/** get Quantity.
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
	/**get Product.
	 * @return
	 */
	public Product getProduct() {
		return product;
	}
	/**set product.
	 * @param product
	 */
	public void setProduct(Product product) {
		this.product = product;
	}
	/** get SupplierOrder.
	 * @return
	 */
	public SupplierOrder getSupplierOrder() {
		return supplierOrder;
	}
	/** set SupplierOrder.
	 * @param supplierOrder
	 */
	public void setSupplierOrder(SupplierOrder supplierOrder) {
		this.supplierOrder = supplierOrder;
	}
}
