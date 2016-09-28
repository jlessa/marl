package au.com.marlo.training.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



@Entity
@Table(name="service_order")
public class Order extends Model{
	

	@Id
	@GeneratedValue (strategy=GenerationType.IDENTITY)
	@Column(unique = true, nullable= false, name="ordernumber")
	private int orderNumber;
	
	@ManyToMany(cascade={CascadeType.PERSIST})
	@JoinTable( name="order_product",
				joinColumns=@JoinColumn(name="order_number"))
	private List<Product> products = new ArrayList<>();
	
	@ManyToOne (targetEntity=Client.class, cascade={CascadeType.PERSIST})
	@JoinColumn(name="client_document")
	private Client client = new Client();

	public int getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}	
	
	public void addProduct (Product product){
		products.add(product);
	}
	
	
}
