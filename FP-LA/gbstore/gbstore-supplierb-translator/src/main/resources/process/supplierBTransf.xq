declare namespace stc="http://gbstore.com.au/messages/splitter/splitterMessage/v1.0";
declare namespace tp="http://gbstore.com.au/types/splitter/v1.0";
declare namespace ns3="http://supplierb.com.au/messages/orderrequest/v1.0";
declare namespace ns3t="http://supplierb.com.au/types/v1.0";
<ns3:supplierBMessage xmlns:ns3="http://supplierb.com.au/messages/orderrequest/v1.0"
xmlns:ns3t="http://supplierb.com.au/types/v1.0">
	<ns3:supplierOrderRequest>
		<ns3t:supplierOrderId>{//tp:supplierOrderId/text()}</ns3t:supplierOrderId>
		<ns3t:itemSupplierOrderList>
		{
			for $order in //tp:itemSupplierOrder
			return
			<ns3t:itemOrder>
				<ns3t:quantity>{$order/tp:quantity/text()}</ns3t:quantity>
				<ns3t:productId>{$order/tp:product/tp:productSupplierId/text()}</ns3t:productId>
			</ns3t:itemOrder>
		}
		</ns3t:itemSupplierOrderList>
	</ns3:supplierOrderRequest>
</ns3:supplierBMessage>