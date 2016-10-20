package com.goodbooze.dbtranslator.databasemodel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;



/**
 * The Class ProductJPA maps db table product.
 */
@Entity
@Table(name="product")
public class ProductJPA {
    
    /** The internal id. */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected int internalId;
    
    /** The product supplier id. */
    @Column(unique=true, nullable=false)
    @GeneratedValue
    protected int productSupplierId;
    
    /** The name. */
    @Column
    protected String name;
    
    /** The supplier jpa. */
    @ManyToOne(fetch=FetchType.LAZY)
    protected SupplierJPA supplierJPA;

    /**
     * Gets the internal id.
     *
     * @return the internal id
     */
    public int getInternalId() {
        return internalId;
    }


    /**
     * Sets the internal id.
     *
     * @param value the new internal id
     */
    public void setInternalId(int value) {
        this.internalId = value;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param value the new name
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the product supplier id.
     *
     * @return the product supplier id
     */
    public int getProductSupplierId() {
        return productSupplierId;
    }


    /**
     * Sets the product supplier id.
     *
     * @param productSupplierId the new product supplier id
     */
    public void setProductSupplierId(int productSupplierId) {
        this.productSupplierId = productSupplierId;
    }


    /**
     * Gets the supplier jpa.
     *
     * @return the supplier jpa
     */
    public SupplierJPA getSupplierJPA() {
        return supplierJPA;
    }


    /**
     * Sets the supplier jpa.
     *
     * @param supplierJPA the new supplier jpa
     */
    public void setSupplierJPA(SupplierJPA supplierJPA) {
        this.supplierJPA = supplierJPA;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + internalId;
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
        ProductJPA other = (ProductJPA) obj;
        if (internalId != other.internalId)
            return false;
        return true;
    }

    

}
