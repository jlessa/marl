package au.com.marlo.trainning.rest_client;

import java.util.logging.Logger;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;



public class ProjectRestClient {
	final static Logger logger = Logger.getAnonymousLogger();
	final static String API_URL = "http://localhost:8080/project-rest-server/methods/";
		
  public static void main(String []arg){	 	  	  
	  ProjectRestClient client = new ProjectRestClient();
	  logger.info( client.getClientFromAPI("1"));
	  logger.info( client.getAllProducts());
  }
  
  public String getClientFromAPI(String id){	  
	  	 return getResponse("client/"+id); 	  
  }
  
  public String getAllProducts(){	  	  
	  	return getResponse("product/all");	  	  
  }
    
  public WebTarget createWebTarget(String path){
	return ClientBuilder.newClient().target(API_URL + path);  
  }
  
  public String getResponse (String target){
	  WebTarget webTarget = createWebTarget(target);
	  return webTarget.request().get(String.class);
  }
}

