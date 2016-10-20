declare namespace sso="http://gbstore.com.au/messages/dbtranslator/saveSupplierOrdersRequest/v1.0";
declare namespace dbr="http://gbstore.com.au/messages/dbtranslator/enrichmentResponse/v1.0";
declare namespace tp="http://gbstore.com.au/types/dbtranslator/v1.0";

<sso:saveSupplierOrderRequest
    xmlns:sso="http://gbstore.com.au/messages/dbtranslator/saveSupplierOrdersRequest/v1.0" xmlns:tp="http://gbstore.com.au/types/dbtranslator/v1.0">
   
        {
          for $suppliersId in distinct-values(//tp:supplierId)
   
          return
       <sso:supplierOrder>  
       
           <tp:supplier>
              <tp:supplierId>{$suppliersId}</tp:supplierId>
              <tp:name>{//supplier[tp:supplierId=$suppliersId]/tp:name/text()}</tp:name>
           </tp:supplier>      
             
             
           <tp:itemSupplierOrderList>
               {
                 for $productId in  distinct-values(//tp:itemOrder[tp:product/tp:supplier/tp:supplierId = $suppliersId]/tp:product/tp:internalId)
                  let $itemOrder := //tp:itemOrder[tp:product/tp:internalId = $productId]
                 let $quantity := sum($itemOrder[tp:product/tp:internalId = $productId]/tp:quantity)
                 let $productSupId := $itemOrder[1]/tp:product[tp:internalId = $productId]/tp:productSupplierId
                 let $prodInternalId := $itemOrder[1]/tp:product[tp:internalId = $productId]/tp:internalId
                 let $name := $itemOrder[1]//tp:product[tp:internalId= $productId]/tp:name
                 return 
                    <tp:itemSupplierOrder> 
                           <tp:quantity>{$quantity}</tp:quantity> 
                           <tp:product>
                           	<tp:internalId>{$prodInternalId/text()}</tp:internalId>
                              <tp:productSupplierId>{$productSupId/text()}</tp:productSupplierId>
                              <tp:name>{$name/text()}</tp:name>
                           </tp:product>
               
               <tp:storeOrderIdList>
                {
                  for $storeOrders in //tp:storeOrder[.//tp:supplierId = $suppliersId]//tp:storeOrderId
					return 
					<tp:storeOrderId>{$storeOrders/text()}</tp:storeOrderId>
                }
               </tp:storeOrderIdList>
                           
                           
               </tp:itemSupplierOrder>
               }
               
           </tp:itemSupplierOrderList>
         </sso:supplierOrder>
  }
</sso:saveSupplierOrderRequest>