declare namespace stc="http://gbstore.com.au/messages/splitter/splitterMessage/v1.0";
declare namespace tp="http://gbstore.com.au/types/splitter/v1.0";
declare namespace spa="http://supplier.com.au/suppliera/message/supplierOrderRequest/v1.0";

<spa:supplierOrderRequest>
<spa:orderId>{//tp:supplierOrderId/text()}</spa:orderId>
<spa:orderList>
{
for $order in //tp:itemSupplierOrder
return
<spa:order>
<spa:quantity>{$order/tp:quantity/text()}</spa:quantity>
<spa:productId>{$order/tp:product/tp:productSupplierId/text()}</spa:productId>
<spa:name>{$order/tp:product/tp:name/text()}</spa:name>
</spa:order>
}
</spa:orderList>
</spa:supplierOrderRequest>