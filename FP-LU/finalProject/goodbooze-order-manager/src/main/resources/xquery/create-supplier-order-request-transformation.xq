declare namespace ns = "http://goodbooze.com/messages/dbtranslator/SaveSupplierOrder/v1.0";
declare namespace ns1 = "http://goodbooze.com/types/typesdefinition/v1.0";
declare namespace tns= "http://goodbooze.com/messages/supplierorderrequestmsg/v1.0"; 
declare namespace tns1 = "http://goodbooze.com/types/typesdefinition/v1.0";

<tns:supplierOrderRequestMsg xmlns:tns="http://goodbooze.com/messages/supplierorderrequestmsg/v1.0" 
						xmlns:tns1="http://goodbooze.com/types/typesdefinition/v1.0" 
						xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
						xsi:schemaLocation="http://goodbooze.com/messages/supplierorderrequestmsg/v1.0 SupplierOrderRequestMsg.xsd ">

<tns:supplierOrderList>
{
for $supplierOrder in //ns:supplierOrder return
<tns1:supplierOrder>
  	<tns1:id>{data($supplierOrder/ns:id)}</tns1:id>
	<tns1:status>{data($supplierOrder/ns:status)}</tns1:status>
	<tns1:insertDate>{data($supplierOrder/ns:insertDate)}</tns1:insertDate>
	<tns1:supplier>
		<tns1:id>{data($supplierOrder/ns:supplierId)}</tns1:id>
	</tns1:supplier>
	<tns1:itemSupplierOrderList>
    {for $itemSupplierOrder in $supplierOrder/ns:itemSupplierOrderList/ns1:itemSupplier return
              <tns1:itemSupplier>
                    <tns1:quantity>{data($itemSupplierOrder/ns1:quantity)}</tns1:quantity>
                    <tns1:product>
                        <tns1:productSupplierId>{data($itemSupplierOrder/ns1:product/ns1:productSupplierId)}</tns1:productSupplierId>
                        <tns1:name>{data($itemSupplierOrder/ns1:product/ns1:name)}</tns1:name>
                    </tns1:product>
                </tns1:itemSupplier>
     }        
    </tns1:itemSupplierOrderList>
    <tns1:storeOrderList>
    {for $storeOrderId in $supplierOrder/ns:storeOrderList/ns1:storeOrderId return
    	<tns1:storeOrderId>{data($storeOrderId)}</tns1:storeOrderId>
    }
    </tns1:storeOrderList>
    </tns1:supplierOrder>
}
</tns:supplierOrderList>

</tns:supplierOrderRequestMsg>