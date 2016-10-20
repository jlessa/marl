xquery version "1.0";
declare namespace v1="http://gbstore.com.au/messages/legacysystem/storeOrderRequest/v1.0";
declare namespace v11="http://gbstore.com.au/types/legacysystem/v1.0";
declare namespace dbe="http://gbstore.com.au/messages/dbtranslator/saveStoreOrderRequest/v1.0";
declare namespace tp="http://gbstore.com.au/types/dbtranslator/v1.0";

<dbe:saveStoreOrderRequest xmlns:dbe="http://gbstore.com.au/messages/dbtranslator/saveStoreOrderRequest/v1.0"
    xmlns:tp="http://gbstore.com.au/types/dbtranslator/v1.0">
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
            	<tp:store>
                	<tp:storeId>{$storeOrder/v11:store/v11:storeId/text()}</tp:storeId>
                    <tp:name>{$storeOrder/v11:store/v11:name/text()}</tp:name>
                </tp:store>
                <tp:itemStoreOrderList>
                {
                for $item in $storeOrder/v11:itemStoreOrderList//v11:itemStoreOrder
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