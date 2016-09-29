package au.com.marlo.training.dao;

import java.util.List;

import au.com.marlo.training.entity.Client;
import au.com.marlo.training.entity.Model;
import au.com.marlo.training.entity.Order;
import au.com.marlo.training.entity.Product;

public interface ModelDaoContract {
	public void save(Model model);
	public Model getById (Class<?> model, int id);
	public List<Order> getOrdersByUser (Client client);
	public List<Product> getAllProducts ();
	public void destroy (Model model);
	public void close ();
}
