package au.com.gbstore.dbtranslator.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
/**Store model.
 * @author lteixeira
 *
 */
@Entity
public class Store {

@Id
@GeneratedValue(strategy=GenerationType.IDENTITY)
private Long storeId;
private String name;
/**Constructor.
 * 
 */
public Store() {
	}
	/**get StoreId.
	 * @return
	 */
	public Long getStoreId() {
		return storeId;
	}
	/**set StoreId.
	 * @param storeId
	 */
	public void setStoreId(Long storeId) {
		this.storeId = storeId;
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
