package com.goodbooze.dbtranslator.databasemodel.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;




/**
 * The Class EntryJPA represents db table entry.
 */
@Entity
@Table(name="entry")
public class EntryJPA {

    /** The entry id. */
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    protected int entryId;
    
    /** The insert date. */
    @Column
    protected Date insertDate;
    
    /** The process date. */
    @Column
    protected Date processDate;
    
    /** The store order list. */
    @OneToMany(cascade={CascadeType.PERSIST}, mappedBy="entry")
    protected List<StoreOrderJPA> storeOrderList;


    /**
     * Gets the entry id.
     *
     * @return the entry id
     */
    public int getEntryId() {
        return entryId;
    }


    /**
     * Sets the entry id.
     *
     * @param value the new entry id
     */
    public void setEntryId(int value) {
        this.entryId = value;
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
     * Gets the process date.
     *
     * @return the process date
     */
    public Date getProcessDate() {
        return processDate;
    }


    /**
     * Sets the process date.
     *
     * @param value the new process date
     */
    public void setProcessDate(Date value) {
        this.processDate = value;
    }


    /**
     * Gets the store order list.
     *
     * @return the store order list
     */
    public List<StoreOrderJPA> getStoreOrderList() {
        if (storeOrderList == null) {
            storeOrderList = new ArrayList<StoreOrderJPA>();
        }
        return this.storeOrderList;
    }


    /**
     * Sets the store order list.
     *
     * @param storeOrderList the new store order list
     */
    public void setStoreOrderList(List<StoreOrderJPA> storeOrderList) {
        this.storeOrderList = storeOrderList;
    }


    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + entryId;
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
        EntryJPA other = (EntryJPA) obj;
        if (entryId != other.entryId)
            return false;
        return true;
    }

    
}
