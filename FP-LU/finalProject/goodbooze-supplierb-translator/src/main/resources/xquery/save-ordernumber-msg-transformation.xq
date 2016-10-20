declare namespace ns = "http://supplier.com/messages/response/v1.0/";
declare namespace tns = "http://goodbooze.com/messages/dbtranslator/saveordernumbermsg/v1.0";
declare variable $supplierOrderId as xs:string external; 

<tns:updateOrderNumber>
	<tns:orderNumber>{data(//ns:orderNumber)}</tns:orderNumber>
    <tns:supplierOrderId>{$supplierOrderId}</tns:supplierOrderId>
</tns:updateOrderNumber>