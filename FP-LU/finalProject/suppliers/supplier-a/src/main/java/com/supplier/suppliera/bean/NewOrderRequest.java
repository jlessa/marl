package com.supplier.suppliera.bean;

import java.util.List;

import com.supplier.suppliera.model.ItemJPA;

public class NewOrderRequest {

	private List<ItemJPA> itens;

	public List<ItemJPA> getItens() {
		return itens;
	}

	public void setItens(List<ItemJPA> itens) {
		this.itens = itens;
	}
	
}
