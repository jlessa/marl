package au.com.gbstore.routebuilder;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
/**.
 * Request the enrichment to DB
 */
public class EnrichmentRequestRoute extends RouteBuilder{

@Override
	public void configure() throws Exception {
		from("{{om.enrich.route}}")
		.routeId("OMEnrichmentRoute")
		.to("{{enrichmentTransformation}}")
		.log("OM: Enrichment Request: ${body}")
		.inOnly("{{db.enrichmentreq.to}}");

		from("{{om.enrichmentresp.from}}")
		.routeId("OMEnrichmentResponseRoute")
		.log("DB response: ${body}")
		.to("{{supplierTransformation}}")
		.log("OM: SupplierOrder will be saved on DB")
		.to(ExchangePattern.InOut, "{{db.savesupplierorder.reqrep}}")
		.log("OM: Sending to SplitterRoute: ${body}")
		.inOnly("{{om.splittermsg.route}}");
	}

}
