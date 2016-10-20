package com.goodbooze.dbtranslator.databasemodel.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SupplierJPATest {
    /**
     * tests for a true equals comparison.
     */
    @Test
    public void hashCodeTest(){
        SupplierJPA x = new SupplierJPA();
        x.setId(1);
        
        SupplierJPA y = new SupplierJPA();
        y.setId(1);
        
        assertTrue(x.equals(y) && y.equals(x));
        assertTrue(x.hashCode() == y.hashCode());
    }
    
    /**
     * tests for compare to a different object.
     */
    @Test
    public void equalsFalse(){
        SupplierJPA x = new SupplierJPA();
        x.setId(1);
        
        SupplierJPA y = null;
        
        assertFalse(x.equals(y));
        
        y=new SupplierJPA();
        y.setId(2);
        assertFalse(x.equals(y));
        
        ProductJPA product = new ProductJPA();
        assertFalse(x.equals(product));
    }
}
