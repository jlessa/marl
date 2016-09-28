package au.com.marlo.training.dao;

import java.nio.file.attribute.AclEntry.Builder;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import au.com.marlo.training.entity.Client;
import au.com.marlo.training.entity.Model;
import au.com.marlo.training.entity.Order;
import au.com.marlo.training.entity.Product;
import au.com.marlo.training.helper.Initializer;

public class ModelDao implements ModelDaoContract{
	private EntityManager entityManager;
	
	public ModelDao(String persistenceUnit){
		entityManager = Persistence.createEntityManagerFactory(persistenceUnit).createEntityManager();
		Initializer.initDb(persistenceUnit, this);
	}
	
	public void save (Model model){
		entityManager.getTransaction().begin();
		entityManager.persist(model);
		entityManager.getTransaction().commit();
		
	} 
	
	public Model getById (Class<?> model, int id){
		return (Model) entityManager.find(model , id);		
	} 
	
	public List<Order> getOrdersByUser (Client client){							
		TypedQuery<Order> query = entityManager.createQuery(
				"SELECT o FROM Order o WHERE o.client.documentId = :client_document", Order.class
				);
		query.setParameter("client_document", client.getDocumentId());
		return 	query.getResultList();	
	} 
	
	public List<Product> getAllProducts (){
		TypedQuery<Product> query = entityManager.createQuery(
				"SELECT p FROM Product p", Product.class
				);		
		return 	query.getResultList();
	}
	
	public void destroy(Model model){
		entityManager.getTransaction().begin();
		entityManager.remove(model);
		entityManager.getTransaction().commit();
	}
	
	public void close (){
		entityManager.close();
	}
	
	
		
}
