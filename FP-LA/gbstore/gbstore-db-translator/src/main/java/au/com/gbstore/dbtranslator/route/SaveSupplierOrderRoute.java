package au.com.gbstore.dbtranslator.route;

import org.apache.camel.builder.RouteBuilder;

import au.com.gbstore.dbtranslator.processor.SaveSupplierOrderProcessor;

/**save SupplierOrder from OM.
 * @author lteixeira
 *
 */
public class SaveSupplierOrderRoute extends RouteBuilder{

@Override
	public void configure() throws Exception {
		from("{{db.savesupplier.reqrep}}")
		.routeId("DBSaveSupplierOrderRoute")
		.log("DB will save this supplierOrder: ${body}")
		.process(new SaveSupplierOrderProcessor())
		.log("DB sending a reply: ${body}");
	}
}
