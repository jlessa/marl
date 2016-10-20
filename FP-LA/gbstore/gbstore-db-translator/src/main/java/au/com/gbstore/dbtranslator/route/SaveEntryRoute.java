package au.com.gbstore.dbtranslator.route;

import org.apache.camel.builder.RouteBuilder;

import au.com.gbstore.dbtranslator.processor.SaveEntryProcessor;

/**Save Entry in DB.
 * receive a message from OM
 * save it in DB
 * send a message back to OM
 * @author lteixeira
 *
 */
public class SaveEntryRoute extends RouteBuilder {

@Override
	public void configure() throws Exception {
		from("{{db.saveentry.from}}")
		.routeId("DBSaveEntry")
		.log("DB is processing ${body}")
		.bean(SaveEntryProcessor.class, "process(${body})")
		.log("DB saved Entry: ${body}")
		.to("{{om.saveentry.rep.to}}");
	}

}
