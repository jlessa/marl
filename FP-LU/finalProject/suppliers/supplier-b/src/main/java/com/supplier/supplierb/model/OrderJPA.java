package com.supplier.supplierb.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "\"order\"")
public class OrderJPA {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	protected int id;
	@Column
	protected String status;
	@Column
	@Temporal(value=TemporalType.TIMESTAMP)
	protected Date insertDate;
	@Column
	@Temporal(value=TemporalType.TIMESTAMP)
	protected Date updateDate;
	@OneToMany(fetch=FetchType.LAZY, cascade=CascadeType.ALL, mappedBy="order")
	protected List<ItemJPA> itemList;
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getInsertDate() {
		return insertDate;
	}
	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public List<ItemJPA> getItemList() {
		return itemList;
	}
	public void setItemList(List<ItemJPA> itemList) {
		this.itemList = itemList;
	}
	public int getId() {
		return id;
	}
	
	
}