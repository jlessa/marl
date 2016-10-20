package com.goodbooze.dbtranslator.testutil;

import javax.persistence.EntityManager;

import com.goodbooze.dbtranslator.databasemodel.connect.ConnectionFactory;

public class Util {
    
    public static void cleanUpBase() throws InterruptedException{
        ConnectionFactory.close();
        //wait 3 seconds for data base get closed
        Thread.sleep(3000);
    }
    
    public static EntityManager getEntityManager(){
        return ConnectionFactory.getEntityManager();
    }
}
