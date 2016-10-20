package com.goodbooze.dbtranslator.databasemodel.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StoreJPATest {
    /**
     * tests for a true equals comparison.
     */
    @Test
    public void hashCodeTest(){
        StoreJPA x = new StoreJPA();
        x.setId(1);
        
        StoreJPA y = new StoreJPA();
        y.setId(1);
        
        assertTrue(x.equals(y) && y.equals(x));
        assertTrue(x.hashCode() == y.hashCode());
    }
    
    /**
     * tests for compare to a different object.
     */
    @Test
    public void equalsFalse(){
        StoreJPA x = new StoreJPA();
        x.setId(1);
        
        StoreJPA y = null;
        
        assertFalse(x.equals(y));
        
        y=new StoreJPA();
        y.setId(2);
        assertFalse(x.equals(y));
        
        ProductJPA product = new ProductJPA();
        assertFalse(x.equals(product));
    }
}
