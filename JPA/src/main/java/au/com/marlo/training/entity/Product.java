package au.com.marlo.training.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name="product")

public class Product extends Model{
	

	@Id
	@GeneratedValue (strategy=GenerationType.IDENTITY)
	@Column(unique = true, nullable= false, name="code")
	private int code;
	
	
	private String description;
	
	@Column (columnDefinition="decimal(10,2) default 0")
	private float price;
	
	
	private String category;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	 
}
