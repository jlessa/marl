package com.goodbooze.dbtranslator.databasemodel.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ProductJPATest {
    /**
     * tests for a true equals comparison.
     */
    @Test
    public void hashCodeTest(){
        ProductJPA x = new ProductJPA();
        x.setInternalId(1);
        
        ProductJPA y = new ProductJPA();
        y.setInternalId(1);
        
        assertTrue(x.equals(y) && y.equals(x));
        assertTrue(x.hashCode() == y.hashCode());
    }
    
    /**
     * tests for compare to a different object.
     */
    @Test
    public void equalsFalse(){
        ProductJPA x = new ProductJPA();
        x.setInternalId(1);
        
        ProductJPA y = null;
        
        assertFalse(x.equals(y));
        
        y=new ProductJPA();
        y.setInternalId(2);
        assertFalse(x.equals(y));
        
        StoreJPA store = new StoreJPA();
        assertFalse(x.equals(store));
    }
}
