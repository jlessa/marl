declare namespace sso="http://gbstore.com.au/messages/dbtranslator/saveSupplierOrdersReply/v1.0";
declare namespace spt="http://gbstore.com.au/messages/ordermanager/orderManagerMessage/v1.0";
declare namespace stp="http://gbstore.com.au/types/ordermanager/v1.0";
declare namespace tp="http://gbstore.com.au/types/dbtranslator/v1.0";


<spt:splitterSupplierMessage
    xmlns:spt="http://gbstore.com.au/messages/ordermanager/orderManagerMessage/v1.0"
    xmlns:stp="http://gbstore.com.au/types/ordermanager/v1.0">
   {
   for $supplierOrder in //sso:supplierOrder
   return
        <spt:supplierOrder>
	 		<stp:supplierOrderId>{$supplierOrder/tp:supplierOrderId/text()}</stp:supplierOrderId>
            <stp:supplier>
            	<stp:supplierId>{$supplierOrder/tp:supplier/tp:supplierId/text()}</stp:supplierId>
                <stp:name>{$supplierOrder/tp:supplier/tp:name/text()}</stp:name>
            </stp:supplier>
   			<stp:itemSupplierOrderList>
            {
            for $order in $supplierOrder/tp:itemSupplierOrderList//tp:itemOrder
            return
            	<stp:itemSupplierOrder>
  					<stp:quantity>{$order/tp:quantity/text()}</stp:quantity>
                    <stp:product>
                    	<stp:productSupplierId>{$order/tp:product/tp:productSupplierId/text()}</stp:productSupplierId>
                        <stp:name>{$order/tp:product/tp:name/text()}</stp:name>
                    </stp:product>
                </stp:itemSupplierOrder>
            }
            </stp:itemSupplierOrderList>
        </spt:supplierOrder>
   }
</spt:splitterSupplierMessage>