package au.com.gbstore.supplierb.translator.routebuilder;

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
		.routeId("SupBTransSaveOrderNumber")
		.log("SupBTrans: OrderNumber received from SupplierB: ${body}")
		.to("{{supResponseTransf}}")
		.log("SupBTransl: Saving this order number into DB: ${body}")
		.inOnly("{{sptrans.db.to}}");
		
	}
}
