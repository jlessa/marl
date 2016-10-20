package au.com.suppliera.model;

/**SupplierOrderResponse will be send to SupplierTranslator.
 * @author lteixeira
 *
 */
public class SupplierOrderResponse {

	Long supplierOrderId;
	Long orderNumber;
	
	/**Get OrderNumber.
	 * @return
	 */
	public Long getOrderNumber() {
		return orderNumber;
	}
	
	/**set OrderNumber.
	 * @param orderNumber
	 */
	public void setOrderNumber(Long orderNumber) {
		this.orderNumber = orderNumber;
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
	
	@Override
	public String toString() {
		
		return "SupplierOrderResponse: {OrderNumber = " + this.orderNumber + ", SupplierOrderId = " + this.supplierOrderId + "}";
	}
}
