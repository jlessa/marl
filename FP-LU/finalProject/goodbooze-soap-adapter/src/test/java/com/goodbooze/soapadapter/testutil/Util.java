package com.goodbooze.soapadapter.testutil;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;

import com.goodbooze.messages.createordermsg.v1.CreateOrderList;
import com.goodbooze.messages.createordermsg.v1.CreateOrderMsg;
import com.goodbooze.messages.createordermsg.v1.Order;
import com.goodbooze.messages.createordermsg.v1.OrderList;
import com.goodbooze.types.typesdefinition.v1.ItemStoreOrder;
import com.goodbooze.types.typesdefinition.v1.ItemStoreOrderList;
import com.goodbooze.types.typesdefinition.v1.Product;
import com.goodbooze.types.typesdefinition.v1.Store;

public class Util {
    /**
     * Simplifies file reading
     * 
     * @param loader
     *            Classloder
     * @param fileLocation
     * @return file content
     * @throws IOException
     */
    public static String fileToString(Class<?> loader, String fileLocation)
            throws IOException {
        return IOUtils.toString(
                loader.getClassLoader().getResource(fileLocation),
                Charset.defaultCharset());
    }
    
    public static CreateOrderMsg getCreateOrderMsg(){
        Order order = new Order();
        Store store = new Store();
        store.setId(1);
        store.setName("Store A");
        
        Product product = new Product();
        product.setInternalId(1);
        product.setName("Heineken");
        product.setProductSupplierId(1);
        
        ItemStoreOrder item = new ItemStoreOrder();
        item.setProduct(product);
        item.setQuantity(10);
        
        ItemStoreOrderList itemList = new ItemStoreOrderList();
        itemList.getItemStoreOrder().add(item);
        
        order.setStore(store);
        order.setItemList(itemList);
        
        OrderList orderList = new OrderList();
        orderList.getOrder().add(order);
                
        CreateOrderList createOrderList = new CreateOrderList();
        
        createOrderList.setOrderList(orderList);
        
        CreateOrderMsg msg = new CreateOrderMsg();
        msg.setCreateOrderList(createOrderList);
        
        return msg;
    }
}
