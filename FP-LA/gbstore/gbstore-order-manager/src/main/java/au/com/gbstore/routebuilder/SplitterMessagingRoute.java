package au.com.gbstore.routebuilder;

import org.apache.camel.builder.RouteBuilder;

/**Route the message to Splitter Component.
 *
 */
public class SplitterMessagingRoute extends RouteBuilder{

@Override
	public void configure() throws Exception {
		
		from("{{om.splittermsg.route}}")
		.routeId("OMSplitterMessaging")
		.to("{{splitterMessage}}")
		.log("OM: Sending the follow message to Splitter: ${body}")
		.inOnly("{{sp.splittermessage.to}}");
	}

}
