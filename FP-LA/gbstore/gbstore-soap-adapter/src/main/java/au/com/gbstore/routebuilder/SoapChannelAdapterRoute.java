package au.com.gbstore.routebuilder;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;

import au.com.gbstore.processor.ErrorMessageProcessor;
import au.com.gbstore.processor.ResponseMessageProcessor;

/**
 * @author lteixeira
 *  Route the message from soap service
 * to OrderManager
 *
 */
public class SoapChannelAdapterRoute extends RouteBuilder{
	
	/** 
	 * @see org.apache.camel.builder.RouteBuilder#configure()
	 * 
	 * Soap Adapter Channel component's routes
	 * receive a message
	 * insert processDate
	 * send to Order Manager Component
	 */
	@Override
	public void configure() throws Exception {
		
		onException(Exception.class)
		.log("${body}")
		.log(LoggingLevel.ERROR, "An error has occured \n ${body}")
		.process(new ErrorMessageProcessor())
	    .handled(true);
		
		
		from("{{messageroute.from}}")
		.routeId("SoapChannelAdapterRoute")
		.log(LoggingLevel.INFO, "GoodBooze is processing your request")
		.to("xslt:process/processDateTransformation.xsl")
		.inOnly("{{messageroute.to}}")
		.process(new ResponseMessageProcessor())
	    .log(LoggingLevel.INFO, "GooBooze's response: ${body}");
	}

}
