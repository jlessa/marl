package au.com.marlo.training.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table (name="client")

public class Client extends Model {
	
	@Id
	@GeneratedValue (strategy=GenerationType.IDENTITY)
	@Column(unique = true, nullable= false, name="documentId")
	private int documentId;
	
	@Column(length=255)
	private String name;
	
	
	private int age;
	
	
	private String gender;
	
	@Column (columnDefinition="decimal (10,2) default 0")
	private float available_account_credit;

	
	public Client(){}
	
	public int getDocumentId() {
		return documentId;
	}

	public void setDocumentId(int documentId) {
		this.documentId = documentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public float getAvailable_account_credit() {
		return available_account_credit;
	}

	public void setAvailable_account_credit(float available_account_credit) {
		this.available_account_credit = available_account_credit;
	}
	
}
