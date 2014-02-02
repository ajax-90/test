package com.engagepoint.university.dao;

import utils.DataBaseSingleton;
import entities.Order;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

@LocalBean
@Stateless
public class OrderDAOejb {
    public void addOrder(Order order){
        DataBaseSingleton.getInstance().addOrder(order);
    }

    public List<Order> getOrders() {
        return  DataBaseSingleton.getInstance().getOrders();
    }
}
