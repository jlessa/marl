package com.goodbooze.dbtranslator.databasemodel.model;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;




/**
 * The Class SupplierOrderJPA maps db table named supplierorder.
 */
@Entity
@Table(name="supplierorder")
public class SupplierOrderJPA {

    /** The id. */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected int id;
    
    /** The status. */
    @Column
    protected String status;
    
    /** The insert date. */
    @Column
    protected Date insertDate;
    
    /** The order number. */
    @Column
    protected String orderNumber;
    
    /** The update date. */
    @Column
    protected Date updateDate;
    
    /** The supplier jpa. */
    @ManyToOne(fetch=FetchType.LAZY)
    protected SupplierJPA supplierJPA;
    
    /** The item supplier order list. */
    @OneToMany(mappedBy="supplierOrderJPA", cascade={CascadeType.PERSIST})
    protected List<ItemSupplierOrderJPA> itemSupplierOrderList;
    
    /** The store order list. */
    @ManyToMany
    @JoinTable(name="supplierorder_storeorder", joinColumns={@JoinColumn(name="SUPPLIERORDERJPA_ID")},inverseJoinColumns={@JoinColumn(name="STOREORDERLIST_ID")})
    protected List<StoreOrderJPA> storeOrderList; 


    /**
     * Gets the id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param value the new id
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param value the new status
     */
    public void setStatus(String value) {
        this.status = value;
    }


    /**
     * Gets the insert date.
     *
     * @return the insert date
     */
    public Date getInsertDate() {
        return insertDate;
    }

    /**
     * Sets the insert date.
     *
     * @param value the new insert date
     */
    public void setInsertDate(Date value) {
        this.insertDate = value;
    }

    /**
     * Gets the order number.
     *
     * @return the order number
     */
    public String getOrderNumber() {
        return orderNumber;
    }


    /**
     * Sets the order number.
     *
     * @param value the new order number
     */
    public void setOrderNumber(String value) {
        this.orderNumber = value;
    }

    /**
     * Gets the update date.
     *
     * @return the update date
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * Sets the update date.
     *
     * @param value the new update date
     */
    public void setUpdateDate(Date value) {
        this.updateDate = value;
    }

    /**
     * Gets the supplier.
     *
     * @return the supplier
     */
    public SupplierJPA getSupplier() {
        return supplierJPA;
    }

    /**
     * Sets the supplier.
     *
     * @param value the new supplier
     */
    public void setSupplier(SupplierJPA value) {
        this.supplierJPA = value;
    }

    /**
     * Gets the item supplier order list.
     *
     * @return the item supplier order list
     */
    public List<ItemSupplierOrderJPA> getItemSupplierOrderList() {
        if (itemSupplierOrderList == null) {
            itemSupplierOrderList = new ArrayList<ItemSupplierOrderJPA>();
        }
        return this.itemSupplierOrderList;
    }

    /**
     * Gets the store order list.
     *
     * @return the store order list
     */
    public List<StoreOrderJPA> getStoreOrderList() {
        return storeOrderList;
    }

    /**
     * Sets the store order list.
     *
     * @param storeOrderList the new store order list
     */
    public void setStoreOrderList(List<StoreOrderJPA> storeOrderList) {
        this.storeOrderList = storeOrderList;
    }

    /**
     * Sets the item supplier order list.
     *
     * @param itemSupplierOrderOrderList the new item supplier order list
     */
    public void setItemSupplierOrderList(
            List<ItemSupplierOrderJPA> itemSupplierOrderOrderList) {
        itemSupplierOrderList = itemSupplierOrderOrderList;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SupplierOrderJPA other = (SupplierOrderJPA) obj;
        if (id != other.id)
            return false;
        return true;
    }
    
    

}
