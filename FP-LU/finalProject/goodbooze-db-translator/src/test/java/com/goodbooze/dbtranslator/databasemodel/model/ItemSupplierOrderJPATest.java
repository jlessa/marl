package com.goodbooze.dbtranslator.databasemodel.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ItemSupplierOrderJPATest {
    /**
     * tests for a true equals comparison.
     */
    @Test
    public void hashCodeTest(){
        ItemSupplierOrderJPA x = new ItemSupplierOrderJPA();
        x.setId(1);
        
        ItemSupplierOrderJPA y = new ItemSupplierOrderJPA();
        y.setId(1);
        
        assertTrue(x.equals(y) && y.equals(x));
        assertTrue(x.hashCode() == y.hashCode());
    }
    
    /**
     * tests for compare to a different object.
     */
    @Test
    public void equalsFalse(){
        ItemSupplierOrderJPA x = new ItemSupplierOrderJPA();
        x.setId(1);
        
        ItemSupplierOrderJPA y = null;
        
        assertFalse(x.equals(y));
        
        y=new ItemSupplierOrderJPA();
        y.setId(2);
        assertFalse(x.equals(y));
        
        StoreJPA store = new StoreJPA();
        assertFalse(x.equals(store));
    }
}
