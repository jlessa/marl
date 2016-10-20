declare namespace ns = "http://goodbooze.com/messages/dbtranslator/savestoreordermsg/v1.0";
declare namespace ns1 = "http://goodbooze.com/types/typesdefinition/v1.0";
declare namespace tns= "http://goodbooze.com/messages/dbtranslator/SaveSupplierOrder/v1.0";
declare namespace tns1 = "http://goodbooze.com/types/typesdefinition/v1.0";

<tns:saveSupplierOrder
	xmlns:tns="http://goodbooze.com/messages/dbtranslator/SaveSupplierOrder/v1.0"
	xmlns:tns1="http://goodbooze.com/types/typesdefinition/v1.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	xsi:schemaLocation="http://goodbooze.com/messages/dbtranslator/SaveSupplierOrder/v1.0 SaveSupplierOrderMsg.xsd ">
<tns:supplierOrderList>
{
for $supplier in distinct-values(//ns1:productSupplierId)
return 
<tns:supplierOrder>
	<tns:status>new</tns:status>
	<tns:insertDate>{current-date()}</tns:insertDate>
    <tns:supplierId>{$supplier}</tns:supplierId>
    <tns:itemSupplierOrderList>        
			
          {for $item in distinct-values(//ns1:product[ns1:productSupplierId = $supplier]/ns1:internalId) return
          	
              <tns1:itemSupplier>
                  <tns1:quantity>{sum(//ns1:itemStoreOrder[ns1:product/ns1:internalId = $item]/ns1:quantity)}</tns1:quantity>
                  <tns1:product>
                      <tns1:internalId>{$item}</tns1:internalId>
                      <tns1:name>{distinct-values(//ns1:product[ns1:internalId = $item]/ns1:name)}</tns1:name>
                  </tns1:product>
              </tns1:itemSupplier>  
          }
    </tns:itemSupplierOrderList>
    <tns:storeOrderList>
    	{for $storeOrder in //ns1:storeOrder[ns1:itemStoreOrderList/ns1:itemStoreOrder/ns1:product/ns1:productSupplierId = $supplier]/ns1:id return
        <tns:storeOrderId>{data($storeOrder)}</tns:storeOrderId>
        }
    </tns:storeOrderList>
    
    
</tns:supplierOrder>
}
</tns:supplierOrderList>
</tns:saveSupplierOrder>