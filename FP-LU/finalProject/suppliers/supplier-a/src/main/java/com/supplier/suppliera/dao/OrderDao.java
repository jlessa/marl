package com.supplier.suppliera.dao;

import javax.persistence.EntityManager;

import com.supplier.suppliera.model.OrderJPA;



public class OrderDao {

	private EntityManager em;
	public OrderDao(EntityManager em){
		this.em = em;
	}
	
	public boolean create(OrderJPA orderJPA){
		try{
			em.getTransaction().begin();
			em.persist(orderJPA);
			em.getTransaction().commit();
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
}
