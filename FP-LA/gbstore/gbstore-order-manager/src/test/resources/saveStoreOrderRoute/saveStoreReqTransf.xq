xquery version "1.0";
declare namespace v1="http://goodbooze.com.au/gbstore/messages/legacysystem/storeOrderRequest/v1.0";
declare namespace v11="http://goodbooze.com.au/gbstore/types/legacysystem/v1.0";
declare namespace dbe="http://goodbooze.com.au/gbstore/messages/dbtranslator/saveStoreOrderRequest/v1.0";
declare namespace tp="http://goodbooze.com.au/gbstore/types/ordermanager/v1.0";

<dbe:saveStoreOrderRequest xmlns:dbe="http://goodbooze.com.au/gbstore/messages/dbtranslator/saveStoreOrderRequest/v1.0"
    xmlns:tp="http://goodbooze.com.au/gbstore/types/ordermanager/v1.0">
{    
   for $entry in //v1:entry
   return
   <dbe:entry>
       <tp:insertDate>{$entry/v11:insertDate/text()}</tp:insertDate>
       <tp:processDate>{$entry/v11:processDate/text()}</tp:processDate>
       <tp:storeOrderList>
       	{
            for $storeOrder in $entry/v11:storeOrderList//v11:storeOrder
            return
            <tp:storeOrder>
            	<tp:storeOrderId>{$storeOrder/v11:storeOrderId/text()}</tp:storeOrderId>
                <tp:itemStoreOrderList>
                {
                for $item in $storeOrder/v11:itemStoreOrderList/v11:itemStoreOrder
                return
                <tp:itemStoreOrder>
                	<tp:product>
                    	<tp:internalId>{$item/v11:product/v11:internalId/text()}</tp:internalId>
						<tp:name>{$item/v11:product/v11:name/text()}</tp:name>                    
                    </tp:product>
                    <tp:quantity>{$item/v11:quantity/text()}</tp:quantity>
                </tp:itemStoreOrder>  
                
                }
                </tp:itemStoreOrderList>
            </tp:storeOrder>
            
         }
   		</tp:storeOrderList>
   </dbe:entry>
   }
</dbe:saveStoreOrderRequest>