package au.com.gbstore.suppliera.translator.routebuilder;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.xmljson.XmlJsonDataFormat;

/**Receives a message from CBR.
 * transform to JSON
 * sends to SupplierA
 * @author lteixeira
 *
 */
public class SupplierOrderRequestRoute extends RouteBuilder{

@Override
	public void configure() throws Exception {
		
		XmlJsonDataFormat xmljson = new XmlJsonDataFormat();
		xmljson.setSkipNamespaces(true);
		xmljson.setRemoveNamespacePrefixes(true);
		
		//receives message from cbr and sends to supplierA
		from("{{supa.trans.from}}")
		.routeId("SupATranslatorSupplierOrder")
		.log("Supplier A Translator is processing order")
		.to("{{supplierTransf}}")
		.marshal(xmljson)
		.log("SupTransl will send this message to SupplierA: ${body}")
		//set the header with post parameter and content-type
		.setHeader(Exchange.HTTP_METHOD, constant("POST"))
		.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
		.to(ExchangePattern.InOut, "{{spa.suporder.to}}")
		.log("SupATransl: OrderNumber created: ${body}")
		.to("{{direct.save.number}}");
	}

}
