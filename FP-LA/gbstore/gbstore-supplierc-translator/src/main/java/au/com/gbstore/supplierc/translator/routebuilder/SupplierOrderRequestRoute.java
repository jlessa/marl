package au.com.gbstore.supplierc.translator.routebuilder;

import org.apache.camel.builder.RouteBuilder;

/**Receives a message from CBR, transform to JSON and sends to SupplierA.
 * @author lteixeira
 *
 */
public class SupplierOrderRequestRoute extends RouteBuilder{

@Override
	public void configure() throws Exception {
		
		//receives message from cbr and sends to supplierA
		from("{{supc.trans.from}}")
		.routeId("SupCTranslatorSupplierOrder")
		.log("Supplier C Translator is processing order: ${body}")
		.to("{{supplierTransf}}")
		.log("SupCTransl will send this message to SupplierC: ${body}")
		.inOut("{{spc.suporder.to}}")
		.log("SupCTransl: OrderNumber created: ${body}")
		.to("{{direct.save.number}}");
	}

}
