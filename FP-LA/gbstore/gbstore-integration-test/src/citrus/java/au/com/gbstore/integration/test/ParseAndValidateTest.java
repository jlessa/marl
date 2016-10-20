package au.com.gbstore.integration.test;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.testng.annotations.Test;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.testng.TestNGCitrusTestDesigner;

/**The goal of this test is to validate the supplier system request message format.
 * @author lteixeira
 *
 */
@Test
public class ParseAndValidateTest extends TestNGCitrusTestDesigner {

	@Autowired
	@Qualifier("supplierDS")
	private DataSource dataSource;

	ClassPathResource legacyMessage = new ClassPathResource("parseAndValidate/messages/input/input-message.xml");
	ClassPathResource restMessage = new ClassPathResource("parseAndValidate/messages/output/rest-message.json");
	ClassPathResource soapMessage = new ClassPathResource("parseAndValidate/messages/output/soap-message.xml");
	ClassPathResource jmsMessage = new ClassPathResource("parseAndValidate/messages/output/jms-message.xml");

	@CitrusTest(name = "Parse_And_Validate_Message_Test")
	public void parseAndValidateMessageTest() {
		echo("ParseVal: Parsing and validating Supplier message");
		send("soapClient").payload(legacyMessage);
		receive("supplierAProvider").validator("jsonValidator").payload(restMessage);
		receive("supplierBProvider").payload(soapMessage);
		receive("supplierCProvider").payload(jmsMessage);
		
		sleep();
		echo("ParseVal: Validating information in DataBase");
		query(dataSource)
		.statement("select COUNT(*) as overall_cnt from SupplierOrder_StoreOrder")
		.validate("overall_cnt", "3")
		.statement("SELECT orderNumber as ord1, updateDate as upd1 FROM gbstoreds.SupplierOrder where supplierOrderId=1")
		.validate("ord1", "NULL")
		.validate("upd1", "NULL")
		.statement("SELECT orderNumber as ord2, updateDate as upd2 FROM gbstoreds.SupplierOrder where supplierOrderId=2")
		.validate("ord2", "NULL")
		.validate("upd2", "NULL")
		.statement("SELECT orderNumber as ord3, updateDate as upd3 FROM gbstoreds.SupplierOrder where supplierOrderId=3")
		.validate("ord3", "NULL")
		.validate("upd3", "NULL");
	}

}
