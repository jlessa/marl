package au.com.gbstore.dbtranslator.route;

import org.apache.camel.builder.RouteBuilder;

import au.com.gbstore.dbtranslator.processor.SaveOrderNumberProcessor;

/**Save orderNumber from Supplier.
 * @author lteixeira
 *
 */
public class SaveOrderNumberRoute extends RouteBuilder{

@Override
	public void configure() throws Exception {
		from("{{db.saveordernumber.req}}")
		.routeId("DBSaveOrderNumber")
		.log("DB will save order number ${body}")
		.process(new SaveOrderNumberProcessor())
		.log("DB saved orderNumber sucessfully");
	}

}
