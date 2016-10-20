package au.com.gbstore.cbr.routebuilder;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;



/**Routes each message to each supplier.
 * @author lteixeira
 *
 */
public class CBRRoute extends RouteBuilder{
final static String SUPPLIER_A_ID = "1";
final static String SUPPLIER_B_ID = "2";
final static String SUPPLIER_C_ID = "3";
	
@Override
	public void configure() throws Exception {
	
		from("{{cbr.cbrroute.from}}")
		.routeId("CBRRoute")
		.log("CBR is processing: ${body}")
		.setExchangePattern(ExchangePattern.InOnly)
		.choice()
		.when(header("supplierId").isEqualTo(SUPPLIER_A_ID))
			.log("CBR: Sending to SupplierA: ${body}")
			.to("{{spa.suppliera.to}}")
		.when(header("supplierId").isEqualTo(SUPPLIER_B_ID))
			.log("CBR: Sending to SupplierB: ${body}")
			.to("{{spb.supplierb.to}}")
		.when(header("supplierId").isEqualTo(SUPPLIER_C_ID))
			.log("CBR: Sending to SupplierC: ${body}")
			.to("{{spc.supplierc.to}}");
	}
}
