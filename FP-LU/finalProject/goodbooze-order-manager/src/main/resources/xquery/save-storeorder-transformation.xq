declare namespace tns1 = "http://goodbooze.com/types/typesdefinition/v1.0";
declare namespace tns = "http://goodbooze.com/messages/dbtranslator/savestoreordermsg/v1.0";
declare namespace ns = "http://goodbooze.com/messages/createordermsg/v1.0";
declare namespace ns1 = "http://goodbooze.com/types/typesdefinition/v1.0";

<tns:saveStoreOrder xsi:schemaLocation="http://goodbooze.com/messages/dbtranslator/savestoreordermsg/v1.0 saveStoreOrder.xsd "
					xmlns:tns="http://goodbooze.com/messages/dbtranslator/savestoreordermsg/v1.0"
					xmlns:tns1="http://goodbooze.com/types/typesdefinition/v1.0"
					xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" >
	
	<tns:storeOrderList>

		{
		for $order in //ns:createOrderMsg/ns:createOrderList/ns:orderList/ns:order 
		return

			<tns1:storeOrder>
				     <tns1:status>new</tns1:status>
	      			<tns1:insertDate>{data(//ns:insertDate)}</tns1:insertDate>
	      			<tns1:itemStoreOrderList>
	      			{
	      			for $item in $order/ns:itemList/ns1:itemStoreOrder 
                    return
	      			<tns1:itemStoreOrder>
						<tns1:quantity>{data($item/ns1:quantity)}</tns1:quantity>
          				<tns1:product>
           					<tns1:internalId>{data($item/ns1:product/ns1:internalId)}</tns1:internalId>
            					<tns1:name>{data($item/ns1:product/ns1:name)}</tns1:name>
          				</tns1:product>
	      			</tns1:itemStoreOrder>

	      			}
	      			</tns1:itemStoreOrderList>
				    	<tns1:store>
			        		<tns1:id>{data($order/ns:store/ns1:id)}</tns1:id>
			        		<tns1:name>{data($order/ns:store/ns1:name)}</tns1:name>
			      	</tns1:store>
			</tns1:storeOrder>
		}	
	</tns:storeOrderList>
	
</tns:saveStoreOrder>