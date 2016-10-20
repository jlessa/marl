package com.supplier.suppliera.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;



@Entity
@Table(name = "item")
public class ItemJPA {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int id;
	@Column
	protected long quantity;
	@OneToOne(fetch=FetchType.LAZY)
	protected ProductJPA product;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn
	protected OrderJPA order;
	
	public OrderJPA getOrder() {
		return order;
	}
	public void setOrder(OrderJPA orderJPA) {
		this.order = orderJPA;
	}
	public long getQuantity() {
		return quantity;
	}
	public void setQuantity(long quantity) {
		this.quantity = quantity;
	}
	public ProductJPA getProduct() {
		return product;
	}
	public void setProduct(ProductJPA productJPA) {
		this.product = productJPA;
	}

	public int getId() {
		return id;
	}
	
	
}
