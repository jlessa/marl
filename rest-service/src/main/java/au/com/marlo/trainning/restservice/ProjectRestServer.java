package au.com.marlo.trainning.restservice;

import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;

import au.com.marlo.training.dao.ModelDao;
import au.com.marlo.training.entity.Client;
import au.com.marlo.training.entity.Order;
import au.com.marlo.training.entity.Product;

@Path("/methods")
public class ProjectRestServer {
	
	private ModelDao modelDao;		
	
	public ModelDao getModelDao() {
		return modelDao;
	}
	public void setModelDao(ModelDao modelDao) {
		this.modelDao = modelDao;
	}
	private ObjectMapper createMapper(){
		ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationConfig.Feature.WRAP_ROOT_VALUE, true);
        mapper.configure(SerializationConfig.Feature.INDENT_OUTPUT, true);
		return mapper;
	}
    @GET
    @Path("/client/{id}")
    @Produces("application/json")    
    public Response getOrders(@PathParam("id") String id) { 
    	String json = "";
    	try{   
    		modelDao = new ModelDao("JPA");
            Client client = (Client) modelDao.getById(Client.class, new Integer(id));
            List<Order> orders = modelDao.getOrdersByUser(client);            
            json = createMapper().writeValueAsString(orders);
    	}catch(Exception ex){
    		Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
    	}    	       
    	return Response.ok().entity(json).build();
    }
    
    @GET
    @Path("/product/all")
    @Produces("application/json")    
    public Response getProducts() { 
    	String json = "";
    	try{    
    		modelDao = new ModelDao("JPA");
            List<Product> products = modelDao.getAllProducts();
            json = createMapper().writeValueAsString(products);
    	}catch(Exception ex){
    		Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
    	}
    	        
    	return Response.ok().entity(json).build();
    }
    
       
}

