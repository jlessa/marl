package au.com.gbstore.dbtranslator.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**Supplier model.
 * @author lteixeira
 *
 */
@Entity
public class Supplier {

@Id
@GeneratedValue(strategy= GenerationType.IDENTITY)
private Long supplierId;
private String name;
/**Constructor.
 */
public Supplier() {
	}
	/**get SupplierId.
	 * @return
	 */
	public Long getSupplierId() {
		return supplierId;
	}
	/**set SupplierId.
	 * @param supplierId
	 */
	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
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
}
