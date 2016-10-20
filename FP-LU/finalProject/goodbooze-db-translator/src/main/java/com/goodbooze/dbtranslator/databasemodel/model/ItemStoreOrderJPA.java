package com.goodbooze.dbtranslator.databasemodel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



/**
 * The Class ItemStoreOrderJPA represents db table itemstoreorder.
 */
@Entity
@Table(name="itemstoreorder")
public class ItemStoreOrderJPA {

    /** The id. */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected int id;
    
    /** The quantity. */
    @Column
    protected long quantity;
    
    /** The product jpa. */
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name="productInternalId", referencedColumnName="internalId"),
        @JoinColumn(name="productSupplierId", referencedColumnName="productSupplierId")
    })
    protected ProductJPA productJPA;
    
    /** The store order jpa. */
    @ManyToOne(fetch=FetchType.LAZY)
    protected StoreOrderJPA storeOrderJPA;


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
     * Gets the quantity.
     *
     * @return the quantity
     */
    public long getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity.
     *
     * @param value the new quantity
     */
    public void setQuantity(long value) {
        this.quantity = value;
    }

    /**
     * Gets the product.
     *
     * @return the product
     */
    public ProductJPA getProduct() {
        return productJPA;
    }

    /**
     * Sets the product.
     *
     * @param value the new product
     */
    public void setProduct(ProductJPA value) {
        this.productJPA = value;
    }


    /**
     * Gets the store order.
     *
     * @return the store order
     */
    public StoreOrderJPA getStoreOrder() {
        return storeOrderJPA;
    }


    /**
     * Sets the store order.
     *
     * @param storeOrderJPA the new store order
     */
    public void setStoreOrder(StoreOrderJPA storeOrderJPA) {
        this.storeOrderJPA = storeOrderJPA;
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
        ItemStoreOrderJPA other = (ItemStoreOrderJPA) obj;
        if (id != other.id)
            return false;
        return true;
    }
    
    

}
