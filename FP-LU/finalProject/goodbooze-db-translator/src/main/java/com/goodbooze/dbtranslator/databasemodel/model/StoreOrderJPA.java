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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * The Class StoreOrderJPA maps db table storeorder.
 */
@Entity
@Table(name="storeorder")
public class StoreOrderJPA {
    
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
    
    /** The update date. */
    @Column
    protected Date updateDate;
    
    /** The entry. */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="entryId", nullable=false)
    protected EntryJPA entry;
    
    /** The item store order order list. */
    @OneToMany(mappedBy="storeOrderJPA", cascade={ CascadeType.PERSIST})
    protected List<ItemStoreOrderJPA> itemStoreOrderOrderList;
    
    /** The store jpa. */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn
    protected StoreJPA storeJPA;
    
    /** The supplier order list. */
    @ManyToMany(mappedBy="storeOrderList")
    protected List<SupplierOrderJPA> supplierOrderList;

    
    /**
     * Gets the supplier order list.
     * @return the supplier order list.
     */
    public List<SupplierOrderJPA> getSupplierOrderList() {
        return supplierOrderList;
    }
    
    /**
     * Gets the supplier order list.
     * @param supplierOrderList
     */
    public void setSupplierOrderList(List<SupplierOrderJPA> supplierOrderList) {
        this.supplierOrderList = supplierOrderList;
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
     * Gets the item store order order list.
     *
     * @return the item store order order list
     */
    public List<ItemStoreOrderJPA> getItemStoreOrderOrderList() {
        if (itemStoreOrderOrderList == null) {
            itemStoreOrderOrderList = new ArrayList<ItemStoreOrderJPA>();
        }
        return this.itemStoreOrderOrderList;
    }

    /**
     * Gets the store.
     *
     * @return the store
     */
    public StoreJPA getStore() {
        return storeJPA;
    }

    /**
     * Sets the store.
     *
     * @param value the new store
     */
    public void setStore(StoreJPA value) {
        this.storeJPA = value;
    }

    /**
     * Gets the entry.
     *
     * @return the entry
     */
    public EntryJPA getEntry() {
        return entry;
    }

    /**
     * Sets the entry.
     *
     * @param entry the new entry
     */
    public void setEntry(EntryJPA entry) {
        this.entry = entry;
    }

    /**
     * Gets the store jpa.
     *
     * @return the store jpa
     */
    public StoreJPA getStoreJPA() {
        return storeJPA;
    }

    /**
     * Sets the store jpa.
     *
     * @param storeJPA the new store jpa
     */
    public void setStoreJPA(StoreJPA storeJPA) {
        this.storeJPA = storeJPA;
    }

    /**
     * Sets the item store order order list.
     *
     * @param itemStoreOrderOrderList the new item store order order list
     */
    public void setItemStoreOrderOrderList(List<ItemStoreOrderJPA> itemStoreOrderOrderList) {
        this.itemStoreOrderOrderList = itemStoreOrderOrderList;
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
        StoreOrderJPA other = (StoreOrderJPA) obj;
        if (id != other.id)
            return false;
        return true;
    }

    
    
}
