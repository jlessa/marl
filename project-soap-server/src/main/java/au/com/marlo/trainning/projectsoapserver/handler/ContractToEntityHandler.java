package au.com.marlo.trainning.projectsoapserver.handler;

import java.util.ArrayList;
import java.util.List;

import au.com.marlo.trainning.xmltrainning.client.Client;
import au.com.marlo.trainning.xmltrainning.order.Order;
import au.com.marlo.trainning.xmltrainning.product.Product;

public class ContractToEntityHandler {
	public static au.com.marlo.training.entity.Client convertClient(Client contractClient){
		au.com.marlo.training.entity.Client entityClient = new au.com.marlo.training.entity.Client();
		entityClient.setAge(contractClient.getAge().intValue());
		entityClient.setAvailable_account_credit(contractClient.getAvailableAccountCredit().floatValue());
		entityClient.setDocumentId(contractClient.getDocumentId().intValue());
		entityClient.setGender(contractClient.getGender());
		entityClient.setName(contractClient.getName());
		return entityClient;
	}
	
	public static au.com.marlo.training.entity.Product convertProduct(Product contractProduct){
		au.com.marlo.training.entity.Product entityProduct = new au.com.marlo.training.entity.Product();
		entityProduct.setCode(new Integer(contractProduct.getProductCode()));
		entityProduct.setDescription(contractProduct.getDescription());
		entityProduct.setCategory(contractProduct.getCategory());
		entityProduct.setPrice(contractProduct.getPrice().floatValue());
		return entityProduct;
	}
	
	public static au.com.marlo.training.entity.Order convertOrder(Order contractOrder){
		au.com.marlo.training.entity.Order entityOrder = new au.com.marlo.training.entity.Order();
		entityOrder.setClient(convertClient(contractOrder.getUser()));
		entityOrder.setOrderNumber(contractOrder.getOrderNumber().intValue());
		List<Product> products = contractOrder.getProduct();
		for(Product product : products){
			entityOrder.addProduct(convertProduct(product));
		}
		return entityOrder;
	}
	
	public static List<au.com.marlo.training.entity.Order> convertListOrder(List<Order> contractOrders){
		List<au.com.marlo.training.entity.Order> entityOrders = new ArrayList<>();
		for(Order order : contractOrders){
			entityOrders.add(convertOrder(order));
		}		
		return entityOrders;
	}
}
