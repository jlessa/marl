package au.com.gbstore.supplierc.translator.routebuilder;

import org.apache.camel.builder.RouteBuilder;

/**Get the Supplier response and send to DB to save Order Number.
 * @author lteixeira
 *
 */
public class SaveOrderNumberRoute extends RouteBuilder{
@Override
	public void configure() throws Exception {
		
		//send  orderNumber to save
		from("{{direct.save.number}}")
		.routeId("SupCTransSaveOrderNumber")
		.log("SupCTrans: OrderNumber received from SupplierC: ${body}")
		.to("{{supResponseTransf}}")
		.log("SupCTransl: Saving this order number into DB: ${body}")
		.inOnly("{{sptrans.db.to}}");
		
	}
}
