declare namespace ns = "http://goodbooze.com/messages/supplierordermsg/v1.0";
declare namespace ns1 = "http://goodbooze.com/types/typesdefinition/v1.0";
declare namespace tns = "http://supplier.com/supplierc/messages/request/v1.0";

<tns:newOrder>
<tns:itens>
{for $item in //ns1:itemSupplier return
	<tns:item>
    	<tns:quantity>{data($item/ns1:quantity)}</tns:quantity>
        <tns:product>
        	<tns:id>{data($item/ns1:product/ns1:productSupplierId)}</tns:id>
            <tns:name>{data($item/ns1:product/ns1:name)}</tns:name>
        </tns:product>
    </tns:item>
}
</tns:itens>
</tns:newOrder>