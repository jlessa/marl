package com.supplier.supplierb.dao;

import javax.persistence.EntityManager;

import com.supplier.supplierb.model.ProductJPA;

public class ProductDao {
	
	private EntityManager em;
	
	public ProductDao(EntityManager em){
		this.em = em;
	}
	
	public ProductJPA find(int id){
		return em.find(ProductJPA.class, id);
	}
}
