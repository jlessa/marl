package com.goodbooze.dbtranslator.databasemodel.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SupplierOrderJPATest {
    /**
     * tests for a true equals comparison.
     */
    @Test
    public void hashCodeTest(){
        SupplierOrderJPA x = new SupplierOrderJPA();
        x.setId(1);
        
        SupplierOrderJPA y = new SupplierOrderJPA();
        y.setId(1);
        
        assertTrue(x.equals(y) && y.equals(x));
        assertTrue(x.hashCode() == y.hashCode());
    }
    
    /**
     * tests for compare to a different object.
     */
    @Test
    public void equalsFalse(){
        SupplierOrderJPA x = new SupplierOrderJPA();
        x.setId(1);
        
        SupplierOrderJPA y = null;
        
        assertFalse(x.equals(y));
        
        y=new SupplierOrderJPA();
        y.setId(2);
        assertFalse(x.equals(y));
        
        ProductJPA product = new ProductJPA();
        assertFalse(x.equals(product));
    }
}
