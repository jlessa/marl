package au.com.gbstore.suppliera.translator.routebuilder;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.xmljson.XmlJsonDataFormat;

/**Get the Supplier response.
 * and send to DB
 * to save Order Number
 * @author lteixeira
 *
 */
public class SaveOrderNumberRoute extends RouteBuilder{
@Override
	public void configure() throws Exception {
		
		XmlJsonDataFormat jsonxml = new XmlJsonDataFormat();
		jsonxml.setEncoding("UTF-8");
		jsonxml.setRootName("saveOrderNumber");
		
		//send to DB to save OrderNumber
		from("{{direct.save.number}}")
		.routeId("SupplierATranslatorSavesOrderNumber")
		.unmarshal(jsonxml)
		.log("SupATrans: OrderNumber received from SupplierA: ${body}")
		.to("xquery:process/supARespTransf.xq")
		.log("SupATransl: Saving this order number into DB: ${body}")
		.inOnly("{{sptrans.db.to}}");
		
	}
}
