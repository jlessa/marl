package au.com.gbstore.supplierb.translator.routebuilder;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;

/**Receives a message from CBR, transform to JSON and sends to SupplierA.
 * @author lteixeira
 *
 */
public class SupplierOrderRequestRoute extends RouteBuilder{

@Override
	public void configure() throws Exception {
		
		//receives message from cbr and sends to supplierA
		from("{{supb.trans.from}}")
		.routeId("SupBTranslatorSupplierOrder")
		.log("Supplier B Translator is processing order: ${body}")
		.to("{{supplierTransf}}")
		.log("SupBTransl will send this message to SupplierB: ${body}")
		.to(ExchangePattern.InOut, "{{spb.suporder.to}}")
		.log("SupBTransl: OrderNumber created: ${body}")
		.to("{{direct.save.number}}");
	}

}
