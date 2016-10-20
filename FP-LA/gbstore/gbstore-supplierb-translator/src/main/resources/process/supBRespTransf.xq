declare namespace dbr="http://gbstore.com.au/messages/dbtranslator/saveOrderNumber/v1.0";
declare namespace tp="http://gbstore.com.au/types/dbtranslator/v1.0";
declare namespace stp="http://supplierb.com.au/types/v1.0";
<dbr:saveOrderNumber xmlns:dbr="http://gbstore.com.au/messages/dbtranslator/saveOrderNumber/v1.0" xmlns:tp="http://gbstore.com.au/types/dbtranslator/v1.0">
<dbr:supplierOrderResponse>
<tp:orderNumber>{//stp:orderNumber/text()}</tp:orderNumber>
<tp:supplierOrderId>{//stp:supplierOrderId/text()}</tp:supplierOrderId>
</dbr:supplierOrderResponse>
</dbr:saveOrderNumber>