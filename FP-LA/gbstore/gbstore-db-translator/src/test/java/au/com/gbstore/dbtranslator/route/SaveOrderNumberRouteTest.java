package au.com.gbstore.dbtranslator.route;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.DatatypeConfigurationException;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import au.com.gbstore.dbtranslator.model.SupplierOrder;
import au.com.gbstore.messages.dbtranslator.saveordernumber.v1.SaveOrderNumber;
import au.com.gbstore.types.dbtranslator.v1.StatusType;

public class SaveOrderNumberRouteTest extends CamelTestSupport{
private static final String INPUT_MESSAGE = "saveOrderNumberRoute/msg/input/save-order-number.xml";
	
	@Produce(uri = "direct:from")
	protected ProducerTemplate startEndpoint;
	
	protected CamelContext createCamelContext() throws Exception {
		CamelContext context = new DefaultCamelContext();
		PropertiesComponent properties = new PropertiesComponent();
		properties.setLocation("classpath:saveOrderNumberRoute/conf/au.com.gbstore.dbtranslator.cfg");
		context.addComponent("properties", properties);
		return context;
	}
	/**
	 * Call soap adapter' route builder
	 */
	@Override
	protected RouteBuilder createRouteBuilder() {
		return new SaveOrderNumberRoute();
	}

	/**Test if it got the orderNumber and if it is correct.
	 * @throws JAXBException
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void testOrderNumber() throws JAXBException, CamelExecutionException, IOException, InterruptedException {
		SupplierOrder body = (SupplierOrder) startEndpoint.requestBody(fileToString(getClass(), INPUT_MESSAGE));
		assertEquals(body.getOrderNumber(), "1234");
	}
	
	
	/**Test the new status.
	 * @throws JAXBException
	 * @throws CamelExecutionException
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test
	public void testNewStatus() throws CamelExecutionException, IOException, JAXBException, DatatypeConfigurationException{
		SupplierOrder body = (SupplierOrder)startEndpoint.requestBody(fileToString(getClass(), INPUT_MESSAGE));
		assertEquals(StatusType.ORDERED.toString(), body.getStatus());
	}
	
	/** convert a file into a string.
	 * 
	 * @param loader
	 * @param fileLocation
	 * @return
	 * @throws IOException
	 */
	public static String fileToString(Class<?> loader, String fileLocation) throws IOException {
		return IOUtils.toString(loader.getClassLoader().getResource(fileLocation), Charset.defaultCharset());
	}
	
	/** Unmarshal to SaveOrderNumber
	 * @param body
	 * @return
	 * @throws JAXBException
	 */
	public SaveOrderNumber unmarshal(String body) throws JAXBException{
		StringReader reader = new StringReader(body);
		JAXBContext jxb = JAXBContext.newInstance(SaveOrderNumber.class);
		Unmarshaller u = jxb.createUnmarshaller();
		return (SaveOrderNumber) u.unmarshal(reader);
	}

}
