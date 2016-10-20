package com.goodbooze.dbtranslator.databasemodel.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * The Class StoreJPA maps db table store.
 */
@Entity
@Table(name="store")
public class StoreJPA {

    /** The id. */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected int id;
    
    /** The name. */
    @Column
    protected String name;

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
        StoreJPA other = (StoreJPA) obj;
        if (id != other.id)
            return false;
        return true;
    }
    
    
}
