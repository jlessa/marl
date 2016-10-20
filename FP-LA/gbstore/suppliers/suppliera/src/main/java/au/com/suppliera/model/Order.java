package au.com.suppliera.model;

/**Order model.
 * @author lteixeira
 *
 */
public class Order {
	private long quantity;
	private long productId;
	private String name;
	
	/**Get Quantity.
	 * @return
	 */
	public long getQuantity() {
		return quantity;
	}
	/**Set Quantity.
	 * @return
	 */
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	/**Get ProductId.
	 * @return
	 */
	public long getProductId() {
		return productId;
	}
	/**Set ProductId.
	 * @return
	 */
	public void setProductId(long productId) {
		this.productId = productId;
	}
	/**Get product name.
	 * @return
	 */
	public String getName() {
		return name;
	}
	/**Set product name.
	 * @return
	 */
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Order: [ Quantity: "+ getQuantity() + ", ProductId: "+ getProductId() + " ]";
	}
}
