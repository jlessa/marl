package com.goodbooze.dbtranslator.databasemodel.model;

import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EntryJPATest {

    /**
     * tests for a true equals comparison.
     */
    @Test
    public void hashCodeTest(){
        EntryJPA x = new EntryJPA();
        x.setEntryId(1);
        
        EntryJPA y = new EntryJPA();
        y.setEntryId(1);
        
        assertTrue(x.equals(y) && y.equals(x));
        assertTrue(x.hashCode() == y.hashCode());
    }
    
    /**
     * tests for compare to a different object.
     */
    @Test
    public void equalsFalse(){
        EntryJPA x = new EntryJPA();
        x.setEntryId(1);
        x.setInsertDate(new Date());
        
        EntryJPA y = null;
        
        assertFalse(x.equals(y));
        
        y=new EntryJPA();
        y.setEntryId(2);
        assertFalse(x.equals(y));
        
        StoreJPA store = new StoreJPA();
        assertFalse(x.equals(store));
    }
}
