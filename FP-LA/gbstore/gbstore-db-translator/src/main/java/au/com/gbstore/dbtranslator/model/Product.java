package au.com.gbstore.dbtranslator.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**Product model.
 * @author lteixeira
 *
 */
@Entity
public class Product {

@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private Long internalId;
private String name;
@ManyToOne(fetch=FetchType.LAZY, cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}) 
private Supplier supplier;
private Long productSupplierId;

/**Constructor.
 */
public Product() {
	}
	/**get InternalId.
	 * @return
	 */
	public Long getInternalId() {
		return internalId;
	}
	/**set internalId.
	 * @param internalId
	 */
	public void setInternalId(Long internalId) {
		this.internalId = internalId;
	}
	/**get Name.
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**set Name.
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
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
	/**get ProductSupplierId.
	 * @return
	 */
	public Long getProductSupplierId() {
		return productSupplierId;
	}
	/**set ProductSupplierId.
	 * @param productSupplierId
	 */
	public void setProductSupplierId(Long productSupplierId) {
		this.productSupplierId = productSupplierId;
	}
}
