package au.com.supplier.persistence.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**DB object will increment orderNumber sequence.
 * @author lteixeira
 *
 */
@Entity
public class OrderNumber {
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	Long id;
	
	/**get OrderNumber's id
	 * @return
	 */
	public Long getId() {
		return id;
	}
}
