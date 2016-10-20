package au.com.gbstore.integration.test;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.Test;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.testng.TestNGCitrusTestDesigner;
/**
 * The goal of this test is to test all successful path of a completed order using 3 StoreOrders
 * and sending to only SupplierA
 * @author lteixeira
 *
 */
@Test
public class JustSupplierATest extends TestNGCitrusTestDesigner{

	@Autowired
	@Qualifier("supplierDS")
	private DataSource dataSource;

	ClassPathResource legacyMessage = new ClassPathResource("justSupplierA/messages/input/input-message.xml");
	ClassPathResource restMessage = new ClassPathResource("justSupplierA/messages/output/rest-message.json");
	ClassPathResource restResponse = new ClassPathResource("justSupplierA/messages/input/rest-response.json");

	@CitrusTest(name = "Only_SupplierA_Test")
	public void justSupplierATest() {
		echo("OnlySupA: Sending message and validating Supplier message");
		send("soapClient").payload(legacyMessage);
		receive("supplierAProvider").validator("jsonValidator").payload(restMessage);
		send("supplierAProvider").payload(restResponse);
		
		 sleep();
		 
		 echo("OnlySupA: Validating information in DataBase");
		 query(dataSource)
		 /*1 Entry registry with the same content of the SOAP Channel Adapter message values;*/
		 .statement("SELECT insertDate as idate FROM Entry where Entry.entryId = 1").validate("idate", "2002-05-30 09:00:00.0")
		 /*3 StoreOrders row related to the entry;*/
		 .statement("select COUNT(*) as entry_cnt from gbstoreds.Entry_StoreOrder").validate("entry_cnt", "3")
		 /*5 ItemStoreOrder rows related to the StoreOrder registry (Verify quantity column);*/
		.statement("SELECT count(*) as iso_cnt FROM ItemStoreOrder").validate("iso_cnt", "5")
		.statement("SELECT quantity as qtd1 FROM ItemStoreOrder inner join StoreOrder on  ItemStoreOrder.STOREORDER_STOREORDERID=StoreOrder.storeOrderId where StoreOrder.STORE_STOREID=1 and PRODUCT_INTERNALID=1")
		.statement("SELECT quantity as qtd2 FROM ItemStoreOrder inner join StoreOrder on  ItemStoreOrder.STOREORDER_STOREORDERID=StoreOrder.storeOrderId where StoreOrder.STORE_STOREID=2 and PRODUCT_INTERNALID=5")
		.statement("SELECT quantity as qtd3 FROM ItemStoreOrder inner join StoreOrder on  ItemStoreOrder.STOREORDER_STOREORDERID=StoreOrder.storeOrderId where StoreOrder.STORE_STOREID=2 and PRODUCT_INTERNALID=4")
		.statement("SELECT quantity as qtd4 FROM ItemStoreOrder inner join StoreOrder on  ItemStoreOrder.STOREORDER_STOREORDERID=StoreOrder.storeOrderId where StoreOrder.STORE_STOREID=3 and PRODUCT_INTERNALID=1")
		.statement("SELECT quantity as qtd5 FROM ItemStoreOrder inner join StoreOrder on  ItemStoreOrder.STOREORDER_STOREORDERID=StoreOrder.storeOrderId where StoreOrder.STORE_STOREID=3 and PRODUCT_INTERNALID=4")
		.validate("qtd1", "100")
		.validate("qtd2", "500")
		.validate("qtd3", "700")
		.validate("qtd4", "300")
		.validate("qtd5", "600")
		 /*1 SupplierOrder rows related to the storeOrder registry (orderNumber should be the same orderNumber returned by the simulated supplier system);*/
		.statement("select orderNumber as on1 from SupplierOrder inner join SupplierOrder_StoreOrder on SupplierOrder_StoreOrder.supplierOrderId = SupplierOrder.supplierOrderId where SupplierOrder.supplierOrderId=1 group by SupplierOrder.orderNumber")
		.validate("on1", "1")
		/*3 ItemSupplierOrder row PER EACH supplier order containing:
		 quantity 400 of product 1 in supplier A;
		 quantity 1300 of product 2 in supplier A;
		 quantity 500  of product 3 in  supplier A;*/
		.statement("select quantity as qtds1 from ItemSupplierOrder inner join SupplierOrder on ItemSupplierOrder.SUPPLIERORDER_SUPPLIERORDERID = SupplierOrder.supplierOrderId where SUPPLIER_SUPPLIERID = 1 and ItemSupplierOrder.PRODUCT_INTERNALID = 1")
		.statement("select quantity as qtds2 from ItemSupplierOrder inner join SupplierOrder on ItemSupplierOrder.SUPPLIERORDER_SUPPLIERORDERID = SupplierOrder.supplierOrderId where SUPPLIER_SUPPLIERID = 1 and ItemSupplierOrder.PRODUCT_INTERNALID = 4")
		.statement("select quantity as qtds3 from ItemSupplierOrder inner join SupplierOrder on ItemSupplierOrder.SUPPLIERORDER_SUPPLIERORDERID = SupplierOrder.supplierOrderId where SUPPLIER_SUPPLIERID = 1 and ItemSupplierOrder.PRODUCT_INTERNALID = 5")
		.validate("qtds1", "400")
		.validate("qtds2", "1300")
		.validate("qtds3", "500");
	}
}
