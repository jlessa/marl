package au.com.marlo.trainning.projectsoapserver.handler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import au.com.marlo.trainning.xmltrainning.client.Client;
import au.com.marlo.trainning.xmltrainning.order.Order;
import au.com.marlo.trainning.xmltrainning.product.Product;

public class EntitytoContractHandler {
	public static Client convertClient(au.com.marlo.training.entity.Client entityClient){
		Client contractClient = new Client();
		
		contractClient.setAge(new BigInteger(new Integer(entityClient.getAge()).toString()));
		contractClient.setAvailableAccountCredit(new BigDecimal(new Double(entityClient.getAvailable_account_credit()).toString()));
		contractClient.setDocumentId(new BigInteger(new Integer(entityClient.getDocumentId()).toString()));
		contractClient.setGender(entityClient.getGender());
		contractClient.setName(entityClient.getName());
		return contractClient;
	}
	
	public static Product convertProduct(au.com.marlo.training.entity.Product entityProduct){
		Product contractProduct = new Product();
		contractProduct.setProductCode(new Integer(entityProduct.getCode()).toString());
		contractProduct.setDescription(entityProduct.getDescription());
		contractProduct.setCategory(entityProduct.getCategory());
		contractProduct.setPrice(new BigDecimal(entityProduct.getPrice()));
		return contractProduct;
	}
	
	public static Order convertOrder(au.com.marlo.training.entity.Order entityOrder){
		Order contractOrder = new Order();
		contractOrder.setOrderNumber(new BigInteger(new Integer(entityOrder.getOrderNumber()).toString()));
		contractOrder.setUser(convertClient(entityOrder.getClient()));
		List<au.com.marlo.training.entity.Product> products = entityOrder.getProducts();
		for(au.com.marlo.training.entity.Product product : products){
			contractOrder.getProduct().add(convertProduct(product));
		}
		return contractOrder;
	}
	
	public static List<Order> convertListOrder(List<au.com.marlo.training.entity.Order> entityOrders){
		List<Order> contractOrders = new ArrayList<>();
		for(au.com.marlo.training.entity.Order order : entityOrders){
			contractOrders.add(convertOrder(order));
		}		
		return contractOrders;
	}
}
