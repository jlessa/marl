package au.com.gbstore.integration.test;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.Test;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.testng.TestNGCitrusTestDesigner;

/**The goal of this test is to test all successful path of a completed order using just 1 order to all Suppliers
 * @author lteixeira
 *
 */
@Test
public class OneStoreOrderTest extends TestNGCitrusTestDesigner {

	@Autowired
	@Qualifier("supplierDS")
	private DataSource dataSource;

	ClassPathResource legacyMessage = new ClassPathResource("oneStoreOrder/messages/input/input-message.xml");

	ClassPathResource restMessage = new ClassPathResource("oneStoreOrder/messages/output/rest-message.json");
	ClassPathResource restResponse = new ClassPathResource("oneStoreOrder/messages/input/rest-response.json");

	ClassPathResource soapMessage = new ClassPathResource("oneStoreOrder/messages/output/soap-message.xml");
	ClassPathResource soapResponse = new ClassPathResource("oneStoreOrder/messages/input/soap-response.xml");

	ClassPathResource jmsMessage = new ClassPathResource("oneStoreOrder/messages/output/jms-message.xml");
	ClassPathResource jmsResponse = new ClassPathResource("oneStoreOrder/messages/input/jms-response.xml");

	@CitrusTest(name = "One_Store_Request_Test")
	public void oneStoreOrderTest() {
		echo("OneStoreOrd: Sending message and validating Supplier message");
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

		echo("OneStoreOrd: Validating information in DataBase");
		query(dataSource)
				/*1 Entry registry with the same content of the SOAP Channel Adapter message values*/
				.statement("SELECT insertDate as idate FROM Entry where Entry.entryId = 1").validate("idate", "2002-05-30 09:00:00.0")
				 /*1 StoreOrders row related to the entry*/
				.statement("select COUNT(*) as entry_cnt from gbstoreds.Entry_StoreOrder").validate("entry_cnt", "1")
				/* 3 ItemStoreOrder rows related to the StoreOrder registry (one
				 per each product with values 100, 200 and 300 in quantity column*/
				.statement(
						"SELECT count(*) as iso_cnt FROM gbstoreds.ItemStoreOrder where STOREORDER_STOREORDERID=1 and (PRODUCT_INTERNALID=1 or PRODUCT_INTERNALID=2 or PRODUCT_INTERNALID=3)")
				.validate("iso_cnt", "3")
				.statement(
						"SELECT quantity as qtd1 FROM gbstoreds.ItemStoreOrder where STOREORDER_STOREORDERID=1 and PRODUCT_INTERNALID=1")
				.validate("qtd1", "100")
				.statement(
						"SELECT quantity as qtd2 FROM gbstoreds.ItemStoreOrder where STOREORDER_STOREORDERID=1 and PRODUCT_INTERNALID=2")
				.validate("qtd2", "200")
				.statement(
						"SELECT quantity as qtd3 FROM gbstoreds.ItemStoreOrder where STOREORDER_STOREORDERID=1 and PRODUCT_INTERNALID=3")
				.validate("qtd3", "300")
				/* 3 SupplierOrder rows related to the storeOrder registry
				 (orderNumber should be the same orderNumber returned by the
				 simulated supplier system);*/
				.statement(
						"select orderNumber as on1 from SupplierOrder inner join SupplierOrder_StoreOrder on SupplierOrder_StoreOrder.supplierOrderId = SupplierOrder.supplierOrderId where SupplierOrder_StoreOrder.supplierOrderId=1 and SupplierOrder_StoreOrder.storeOrderId=1 ")
				.validate("on1", "1")
				.statement(
						"select orderNumber as on2 from SupplierOrder inner join SupplierOrder_StoreOrder on SupplierOrder_StoreOrder.supplierOrderId = SupplierOrder.supplierOrderId where SupplierOrder_StoreOrder.supplierOrderId=2 and SupplierOrder_StoreOrder.storeOrderId=1 ")
				.validate("on2", "2")
				.statement(
						"select orderNumber as on3 from SupplierOrder inner join SupplierOrder_StoreOrder on SupplierOrder_StoreOrder.supplierOrderId = SupplierOrder.supplierOrderId where SupplierOrder_StoreOrder.supplierOrderId=3 and SupplierOrder_StoreOrder.storeOrderId=1 ")
				.validate("on3", "3")
				/* 1 ItemSupplierOrder row PER EACH supplier order containing:
				 quantity 100 for the product of supplier A;
				 quantity 200 for the product of supplier B;
				 quantity 300 for the product of supplier C;*/
				.statement(
						"select quantity as qtds1 from ItemSupplierOrder inner join SupplierOrder on ItemSupplierOrder.SUPPLIERORDER_SUPPLIERORDERID = SupplierOrder.supplierOrderId where SUPPLIER_SUPPLIERID = 1")
				.validate("qtds1", "100")
				.statement(
						"select quantity as qtds2 from ItemSupplierOrder inner join SupplierOrder on ItemSupplierOrder.SUPPLIERORDER_SUPPLIERORDERID = SupplierOrder.supplierOrderId where SUPPLIER_SUPPLIERID = 2")
				.validate("qtds2", "200")
				.statement(
						"select quantity as qtds3 from ItemSupplierOrder inner join SupplierOrder on ItemSupplierOrder.SUPPLIERORDER_SUPPLIERORDERID = SupplierOrder.supplierOrderId where SUPPLIER_SUPPLIERID = 3")
				.validate("qtds3", "300");
	}
}
