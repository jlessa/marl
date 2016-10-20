package au.com.suppliera.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import au.com.suppliera.model.SupplierOrderRequest;
import au.com.suppliera.model.SupplierOrderResponse;

/** SupplierA route.
 * get the message from SupplierA-Translator
 * responds with orderNumber
 * 
 * post to http://localhost:8080/suppliera/order/request
 * @author lteixeira
 *
 */

public class SupplierARoute extends RouteBuilder{
	private Log log = LogFactory.getLog(getClass());
	
	/**Receive a message from SupplierATranslator.
	 * generate a response with SupplierOrderId and OrderNumber
	 * and send back to SupplierATranslator
	 *
	 */
	@Override
	public void configure() throws Exception{

		log.info("Inside of SupplierA");
		//Rest configurations
		restConfiguration().component("servlet")
        .bindingMode(RestBindingMode.json)
        .dataFormatProperty("prettyPrint", "true")
        .port(8080);
		
		//Rest route
		rest("/order").description("Routing to Supplier A")
		.consumes("application/json")
		.produces("application/json")
		
        .post("/request")
        .type(SupplierOrderRequest.class)
		.outType(SupplierOrderResponse.class)
		.route().from("direct:orderNumber")
		.routeId("SupplierARoute").log("SupplierA will process this request: ${body}")
		.to("bean:supplierOrderService?method=getResponse")
		.log("SupA sent this message: ${body}");
	}

}
