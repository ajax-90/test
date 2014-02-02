package utils;

import entities.Product;
import entities.Order;

import java.util.ArrayList;
import java.util.List;


public class DataBaseSingleton {
    private static DataBaseSingleton dataBaseSingleton;
    private List<Product> productList = new ArrayList<Product>();
    private List<Order> orders = new ArrayList<Order>();

    private DataBaseSingleton() {
        productList = getProductList();
    }

    public static DataBaseSingleton getInstance() {
        if( dataBaseSingleton == null) {
            dataBaseSingleton = new DataBaseSingleton();
        }
            return dataBaseSingleton;

    }

    public void addOrder(Order order) {
        List<Product> sortedProducts = getProductList();
        for (Product pr : sortedProducts) {
            for (Product orderProduct : order.getSelected()) {
                if (pr.getId() == orderProduct.getId()) {
                    pr.setAmount(orderProduct.getAmount());
                }
            }
        }
        order.setSelected(sortedProducts);
        orders.add(order);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public List<Product> getAllProducts(){
        return productList;
    }

    private List<Product> getProductList(){
        List<Product> list = new ArrayList<Product>();
        for (int i = 1; i < 5; i++) {
            list.add(new Product(i,String.valueOf(i), "item" + i, i));
        }
        return list;
    }
}
