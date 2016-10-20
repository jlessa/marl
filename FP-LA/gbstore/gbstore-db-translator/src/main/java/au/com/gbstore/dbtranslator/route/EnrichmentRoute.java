package au.com.gbstore.dbtranslator.route;

import org.apache.camel.builder.RouteBuilder;

import au.com.gbstore.dbtranslator.processor.EnrichmentProcessor;

/**EnrichmentRoute receives a message from OM.
 * Enhance the message with informations supplierProductId and SupplierId
 * @author lteixeira
 *
 */
public class EnrichmentRoute extends RouteBuilder{

@Override
	public void configure() throws Exception {
		from("{{db.enrichmentreq.from}}")
		.routeId("DBEnrichmentRoute")
		.log("DB: Enrichment request: ${body}")
		.bean(EnrichmentProcessor.class, "process(${body})")
		.log("DB: Enrichment response: ${body}")
		.to("{{om.enrichmentresp.to}}");
	}
}
