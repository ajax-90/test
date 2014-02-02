package com.engagepoint.university.dao;


import utils.DataBaseSingleton;
import entities.Product;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

@LocalBean
@Stateless
public class ProductDAOejb {
    public List<Product> getAllProducts() {
        return DataBaseSingleton.getInstance().getAllProducts();
    }


}
