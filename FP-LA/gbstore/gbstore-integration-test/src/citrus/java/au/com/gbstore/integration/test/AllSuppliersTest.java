package au.com.gbstore.integration.test;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.Test;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.testng.TestNGCitrusTestDesigner;
/**
 * The goal of this test is to test all successful path of a completed order using a 3 StoreOrder and
 * sending a message for all of Suppliers
 * @author lteixeira
 *
 */
@Test
public class AllSuppliersTest extends TestNGCitrusTestDesigner{

	@Autowired
	@Qualifier("supplierDS")
	private DataSource dataSource;

	ClassPathResource legacyMessage = new ClassPathResource("allSuppliers/messages/input/input-message.xml");

	ClassPathResource restMessage = new ClassPathResource("allSuppliers/messages/output/rest-message.json");
	ClassPathResource restResponse = new ClassPathResource("allSuppliers/messages/input/rest-response.json");

	ClassPathResource soapMessage = new ClassPathResource("allSuppliers/messages/output/soap-message.xml");
	ClassPathResource soapResponse = new ClassPathResource("allSuppliers/messages/input/soap-response.xml");

	ClassPathResource jmsMessage = new ClassPathResource("allSuppliers/messages/output/jms-message.xml");
	ClassPathResource jmsResponse = new ClassPathResource("allSuppliers/messages/input/jms-response.xml");

	@CitrusTest(name = "All_Suppliers_MoreThanOneStoreOrder_Test")
	public void allSuppliersTest() {
		echo("AllSup: Sending message and validating Supplier message");
		send("soapClient").payload(legacyMessage);
		parallel().actions(
				sequential(receive("supplierAProvider").validator("jsonValidator").payload(restMessage),
						send("supplierAProvider").payload(restResponse)),
				sequential(receive("supplierBProvider").payload(soapMessage),
						send("supplierBProvider").payload(soapResponse)),
				sequential(
						receive("supplierCProvider").payload(jmsMessage).extractFromHeader("citrus_jms_correlationId",
								"CorrelationId"),
						send("supplierCProvider").payload(jmsResponse).header("citrus_jms_correlationId",
								"${CorrelationId}")));
		
		 sleep();

		 echo("AllSup: Validating information in DataBase");
		query(dataSource)
		/*1 Entry registry with the same content of the SOAP Channel Adapter message values*/
		.statement("SELECT insertDate as idate FROM Entry where Entry.entryId = 1").validate("idate", "2002-05-30 09:00:00.0")
		/*3 StoreOrders row related to the entry*/
		.statement("select COUNT(*) as entry_cnt from gbstoreds.Entry_StoreOrder").validate("entry_cnt", "3")
		 /*7 ItemStoreOrder rows related to the StoreOrder registry (Verify quantity column)*/
		.statement("SELECT count(*) as iso_cnt FROM ItemStoreOrder where STOREORDER_STOREORDERID=1 or STOREORDER_STOREORDERID=2 or STOREORDER_STOREORDERID=3")
		.validate("iso_cnt", "7")
		.statement("SELECT quantity as qtd1 FROM ItemStoreOrder inner join StoreOrder on  ItemStoreOrder.STOREORDER_STOREORDERID=StoreOrder.storeOrderId where StoreOrder.STORE_STOREID=1 and PRODUCT_INTERNALID=1")
		.statement("SELECT quantity as qtd2 FROM ItemStoreOrder inner join StoreOrder on  ItemStoreOrder.STOREORDER_STOREORDERID=StoreOrder.storeOrderId where StoreOrder.STORE_STOREID=2 and PRODUCT_INTERNALID=1")
		.statement("SELECT quantity as qtd3 FROM ItemStoreOrder inner join StoreOrder on  ItemStoreOrder.STOREORDER_STOREORDERID=StoreOrder.storeOrderId where StoreOrder.STORE_STOREID=3 and PRODUCT_INTERNALID=1")
		.statement("SELECT quantity as qtd4 FROM ItemStoreOrder inner join StoreOrder on  ItemStoreOrder.STOREORDER_STOREORDERID=StoreOrder.storeOrderId where StoreOrder.STORE_STOREID=1 and PRODUCT_INTERNALID=2")
		.statement("SELECT quantity as qtd5 FROM ItemStoreOrder inner join StoreOrder on  ItemStoreOrder.STOREORDER_STOREORDERID=StoreOrder.storeOrderId where StoreOrder.STORE_STOREID=3 and PRODUCT_INTERNALID=2")
		.statement("SELECT quantity as qtd6 FROM ItemStoreOrder inner join StoreOrder on  ItemStoreOrder.STOREORDER_STOREORDERID=StoreOrder.storeOrderId where StoreOrder.STORE_STOREID=1 and PRODUCT_INTERNALID=3")
		.statement("SELECT quantity as qtd7 FROM ItemStoreOrder inner join StoreOrder on  ItemStoreOrder.STOREORDER_STOREORDERID=StoreOrder.storeOrderId where StoreOrder.STORE_STOREID=2 and PRODUCT_INTERNALID=3")
		.validate("qtd1", "100")
		.validate("qtd2", "400")
		.validate("qtd3", "600")
		.validate("qtd4", "200")
		.validate("qtd5", "700")
		.validate("qtd6", "300")
		.validate("qtd7", "500")
		 /*3 SupplierOrder rows related to the storeOrder registry
		 (orderNumber should be the same orderNumber returned by the
		 simulated supplier system);*/
		.statement("select orderNumber as on1 from SupplierOrder  where supplierOrderId=1")
		.validate("on1", "1")
		.statement("select orderNumber as on2 from SupplierOrder  where supplierOrderId=2")
		.validate("on2", "2")
		.statement("select orderNumber as on3 from SupplierOrder where supplierOrderId=3")
		.validate("on3", "3")
		/*1 ItemSupplierOrder row PER EACH supplier order containing:
		 quantity 1100 for the product of supplier A;
		 quantity 900 for the product of supplier B;
		 quantity 800 for the product of supplier C;*/
		.statement("select quantity as qtds1 from ItemSupplierOrder inner join SupplierOrder on ItemSupplierOrder.SUPPLIERORDER_SUPPLIERORDERID = SupplierOrder.supplierOrderId where SUPPLIER_SUPPLIERID = 1")
		.statement("select quantity as qtds2 from ItemSupplierOrder inner join SupplierOrder on ItemSupplierOrder.SUPPLIERORDER_SUPPLIERORDERID = SupplierOrder.supplierOrderId where SUPPLIER_SUPPLIERID = 2")
		.statement("select quantity as qtds3 from ItemSupplierOrder inner join SupplierOrder on ItemSupplierOrder.SUPPLIERORDER_SUPPLIERORDERID = SupplierOrder.supplierOrderId where SUPPLIER_SUPPLIERID = 3")
		.validate("qtds1", "1100")
		.validate("qtds2", "900")
		.validate("qtds3", "800");
	}
}
