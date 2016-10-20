package com.goodbooze.dbtranslator.databasemodel.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ItemStoreOrderJPATest {
    /**
     * tests for a true equals comparison.
     */
    @Test
    public void hashCodeTest(){
        ItemStoreOrderJPA x = new ItemStoreOrderJPA();
        x.setId(1);
        
        ItemStoreOrderJPA y = new ItemStoreOrderJPA();
        y.setId(1);
        
        assertTrue(x.equals(y) && y.equals(x));
        assertTrue(x.hashCode() == y.hashCode());
    }
    
    /**
     * tests for compare to a different object.
     */
    @Test
    public void equalsFalse(){
        ItemStoreOrderJPA x = new ItemStoreOrderJPA();
        x.setId(1);
        
        ItemStoreOrderJPA y = null;
        
        assertFalse(x.equals(y));
        
        y=new ItemStoreOrderJPA();
        y.setId(2);
        assertFalse(x.equals(y));
        
        StoreJPA store = new StoreJPA();
        assertFalse(x.equals(store));
    }
}
