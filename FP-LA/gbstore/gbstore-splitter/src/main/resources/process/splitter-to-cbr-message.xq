declare namespace ns2="http://gbstore.com.au/messages/ordermanager/orderManagerMessage/v1.0";
declare namespace ns22="http://gbstore.com.au/types/ordermanager/v1.0";
declare namespace stc="http://gbstore.com.au/messages/splitter/splitterMessage/v1.0";
declare namespace tp="http://gbstore.com.au/types/splitter/v1.0";

<stc:splitterOrderToCbr xmlns:stc="http://gbstore.com.au/messages/splitter/splitterMessage/v1.0" xmlns:tp="http://gbstore.com.au/types/splitter/v1.0">
<stc:supplierOrder>
<tp:supplierId>{//ns22:supplierId/text()}</tp:supplierId>
<tp:supplierOrderId>{//ns22:supplierOrderId/text()}</tp:supplierOrderId>   
      
    <tp:itemSupplierOrderList>
    {
    for $item in //ns22:itemSupplierOrder
    return
      <tp:itemSupplierOrder>
        <tp:quantity>{$item/ns22:quantity/text()}</tp:quantity>
        <tp:product>
          <tp:productSupplierId>{$item/ns22:product/ns22:productSupplierId/text()}</tp:productSupplierId>
          <tp:name>{$item/ns22:product/ns22:name/text()}</tp:name>
        </tp:product>
      </tp:itemSupplierOrder>
    }
    </tp:itemSupplierOrderList>
</stc:supplierOrder>
</stc:splitterOrderToCbr>