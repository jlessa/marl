declare namespace ns = "http://supplier.com/supplierc/messages/response/v1.0";
declare namespace tns = "http://goodbooze.com/messages/dbtranslator/saveordernumbermsg/v1.0";
declare variable $orderId as xs:string external; 

<tns:updateOrderNumber>
	<tns:orderNumber>{data(//ns:orderNumber)}</tns:orderNumber>
    <tns:supplierOrderId>{$orderId}</tns:supplierOrderId>
</tns:updateOrderNumber>