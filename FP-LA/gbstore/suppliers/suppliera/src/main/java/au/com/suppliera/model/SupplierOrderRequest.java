package au.com.suppliera.model;

/**SupplierOrderRequest
 * 
 * @author lteixeira
 *
 */
public class SupplierOrderRequest {

    protected Long orderId;
    protected SupplierOrderRequest.OrderList orderList;

    /**
     * Gets the value of the supplierOrderId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * Sets the value of the supplierOrderId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOrderId(Long value) {
        this.orderId = value;
    }

    /**
     * Gets the value of the itemSupplierOrderList property.
     * 
     * @return
     *     possible object is
     *     {@link SupplierOrderType.ItemSupplierOrderList }
     *     
     */
    public SupplierOrderRequest.OrderList getOrderList() {
        return orderList;
    }

    /**
     * Sets the value of the itemSupplierOrderList property.
     * 
     * @param value
     *     allowed object is
     *     {@link SupplierOrderType.ItemSupplierOrderList }
     *     
     */
    public void setOrderList(SupplierOrderRequest.OrderList value) {
        this.orderList = value;
    }
    public static class OrderList {

        protected Order order;

        /**
         * Gets the value of the itemSupplierOrder property.
         **/
        public Order getOrder() {
            if (order == null) {
                order = new Order();
            }
            return this.order;
        }

    }
	@Override
	public String toString() {
		return "SupplierOrderRequest: [ SupplierOrderId: " + getOrderId() + ", OrderList: " + orderList.getOrder().toString() + " ]";
	}
}
