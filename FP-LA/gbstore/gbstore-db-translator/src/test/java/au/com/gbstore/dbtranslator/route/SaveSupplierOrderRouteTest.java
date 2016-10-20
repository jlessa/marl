package au.com.gbstore.dbtranslator.route;
 
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
 
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
 
import org.apache.camel.CamelContext;
import org.apache.camel.CamelExecutionException;
import org.apache.camel.EndpointInject;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
 
import au.com.gbstore.dbtranslator.dao.ProductDao;
import au.com.gbstore.dbtranslator.dao.StoreDao;
import au.com.gbstore.dbtranslator.dao.SupplierDao;
import au.com.gbstore.dbtranslator.model.Product;
import au.com.gbstore.dbtranslator.model.Store;
import au.com.gbstore.dbtranslator.model.Supplier;
import au.com.gbstore.messages.dbtranslator.savesupplierordersreply.v1.SaveSupplierOrderReply;
 
public class SaveSupplierOrderRouteTest extends CamelTestSupport {
   private static final String INPUT_MESSAGE = "saveSupplierOrderRoute/msg/input/save-supplier-order.xml";
   
   @Produce(uri = "direct:from")
   protected ProducerTemplate startEndpoint;
   
   @EndpointInject(uri = "mock:to")
   protected MockEndpoint result;
 
 
   protected CamelContext createCamelContext() throws Exception {
       CamelContext context = new DefaultCamelContext();
       PropertiesComponent properties = new PropertiesComponent();
       properties.setLocation("classpath:saveSupplierOrderRoute/conf/au.com.gbstore.dbtranslator.cfg");
       context.addComponent("properties", properties);
       return context;
   }
 
   /**
    * Call soap adapter' route builder
    */
   @Override
   protected RouteBuilder createRouteBuilder() {
       return new SaveSupplierOrderRoute();
   }
   
   /**SetUp DB before testing
 * @throws Exception 
    * 
    */
   @BeforeClass
   public static void setUpDB() throws Exception{
       SupplierDao sdao = new SupplierDao();
       ProductDao pdao = new ProductDao();
       StoreDao stdao = new StoreDao();
       
       Store boteco = new Store();
       boteco.setName("Boteco");
       stdao.persist(boteco);
       
       Store devassa = new Store();
       devassa.setName("Devassa");
       stdao.persist(devassa);
       
       Store bar = new Store();
       bar.setName("Bar");
       stdao.persist(bar);
       
       Supplier suppliera = new Supplier();
       suppliera.setName("SupplierA");
       suppliera = sdao.persist(suppliera);
       
       Supplier supplierb = new Supplier();
       supplierb.setName("SupplierB");
       supplierb = sdao.persist(supplierb);
       
       Supplier supplierc = new Supplier();
       supplierc.setName("SupplierC");
       supplierc = sdao.persist(supplierc);
       
       Product budweiser = new Product();
       budweiser.setName("Budweiser");
       budweiser.setProductSupplierId(1L);
       budweiser.setSupplier(suppliera);
       budweiser = pdao.persist(budweiser);
       
       Product amstel = new Product();
       amstel.setName("Amstel");
       amstel.setProductSupplierId(2L);
       amstel.setSupplier(supplierb);
       amstel = pdao.persist(amstel);
       
       Product heineken = new Product();
       heineken.setName("Heineken");
       heineken.setProductSupplierId(3L);
       heineken.setSupplier(supplierc);
       heineken = pdao.persist(heineken);
   }
   
   /**Test how many supplierOrders must be one to 3.
    * Message format that will be send to OM
    * @throws JAXBException
    * @throws CamelExecutionException
    * @throws IOException
    * @throws InterruptedException
    */
   @Test
   public void testCountSupplierOrders() throws JAXBException, CamelExecutionException, IOException, InterruptedException {
       Object body = startEndpoint.requestBody(fileToString(getClass(), INPUT_MESSAGE));
       SaveSupplierOrderReply rep = unmarshal(body.toString());
       assertEquals(3, rep.getSupplierOrder().size());
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
   
   /** Unmarshal to SaveSupplierOrderReply
    * @param body
    * @return
    * @throws JAXBException
    */
   public SaveSupplierOrderReply unmarshal(String body) throws JAXBException{
       StringReader reader = new StringReader(body);
       JAXBContext jxb = JAXBContext.newInstance(SaveSupplierOrderReply.class);
       Unmarshaller u = jxb.createUnmarshaller();
       return (SaveSupplierOrderReply) u.unmarshal(reader);
   }
}