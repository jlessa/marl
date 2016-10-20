package au.com.gbstore.routebuilder;

import org.apache.camel.builder.RouteBuilder;

/**
 * @author lteixeira
 *
 */
public class SaveStoreOrderRoute extends RouteBuilder{

/**Get the message from Soap Channel Adapter.
 * save the Entry into DataBase
 * get a response
 * send to a next route
 * 
 * @see org.apache.camel.builder.RouteBuilder#configure()
 * @throws Exception
 */
	@Override
	public void configure() throws Exception {
		
		from("{{om.soapmessage.from}}")
		.routeId("OrderManagerRoute")
		.log("OM: Order Manager received a new request")
		.to("{{saveStoreReqTransf}}")
		.log("OM: Request will be saved in Database: ${body}")
		.inOnly("{{db.savestorereq.to}}");
		
		from("{{om.dbsaverep.from}}")
		.routeId("OMSaveReplyRoute")
		.log("Save Reply: ${body}")
		.inOnly("{{om.enrich.route}}");
		
	}

}
