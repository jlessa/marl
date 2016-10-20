declare namespace dbe="http://gbstore.com.au/messages/dbtranslator/enrichmentRequest/v1.0";
declare namespace tp="http://gbstore.com.au/types/dbtranslator/v1.0";
declare namespace dbr="http://gbstore.com.au/messages/dbtranslator/enrichmentResponse/v1.0";

<dbr:EnrichmentResponse xmlns:dbr="http://gbstore.com.au/messages/dbtranslator/enrichmentResponse/v1.0" xmlns:tp="http://gbstore.com.au/types/dbtranslator/v1.0">
<dbr:orderlist>
	{
	for $storeOrder in //tp:storeOrder
	return
		<tp:storeOrder>
			<tp:storeOrderId>{$storeOrder/tp:storeOrderId/text()}</tp:storeOrderId>
			<tp:itemOrderList>
				{
				for $itemOrder in $storeOrder/tp:itemStoreOrderList//tp:itemStoreOrder
				return
					<tp:itemOrder>
					
						<tp:product>
							<tp:internalId>{$itemOrder/tp:product/tp:internalId/text()}</tp:internalId>
							<tp:productSupplierId>{$itemOrder/tp:product/tp:internalId/text()}</tp:productSupplierId>
							<tp:name>{$itemOrder/tp:product/tp:name/text()}</tp:name>
							<tp:supplier>
								<tp:supplierId>{$itemOrder/tp:product/tp:internalId/text()}</tp:supplierId>
								<tp:name></tp:name>
							</tp:supplier>
						</tp:product>
						<tp:quantity>{$itemOrder//tp:quantity/text()}</tp:quantity>
					</tp:itemOrder>
				}
			</tp:itemOrderList>
		</tp:storeOrder>
	}
</dbr:orderlist>
</dbr:EnrichmentResponse>