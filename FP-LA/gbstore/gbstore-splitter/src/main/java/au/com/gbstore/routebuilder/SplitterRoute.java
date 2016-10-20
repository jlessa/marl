package au.com.gbstore.routebuilder;

import org.apache.camel.builder.RouteBuilder;

import au.com.gbstore.bean.SplitterBean;


/**
 * @author lteixeira
 *
 *Splits the message
 *and sends to CBR
 */
public class SplitterRoute extends RouteBuilder{

@Override
	public void configure() throws Exception {

		//splits the message received from OrderManager
		from("{{sp.splittermessage.from}}")
		.routeId("SplitterRoute")
		.log("Splitter is processing message: ${body}")
		.split().method(SplitterBean.class)
		.to("{{direct.cbr.route}}");
		
		//send each message to CBR
		from("{{direct.cbr.route}}")
		.routeId("SplitterRouteToCBR")
		.to("{{sp.to.cbr.transf}}")
		.log("Splitter: Sending the follow message to: ${body}")
		.inOnly("{{cbr.cbrreq.to}}");
	}

}
