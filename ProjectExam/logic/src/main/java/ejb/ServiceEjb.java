package ejb;

import com.engagepoint.university.dao.OrderDAOejb;
import com.engagepoint.university.dao.ProductDAOejb;
import entities.Order;
import entities.Product;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import java.util.List;

@LocalBean
@Stateless
public class ServiceEjb{

    @EJB
    OrderDAOejb orderDAOejb;
    @EJB
    ProductDAOejb productDAOejb;

    public float productSum(){
        float Sum=0;
        List<Order> orderList = orderDAOejb.getOrders();
        List<Product> productList = productDAOejb.getAllProducts();

        for (int i=0; i<productList.size(); i++)
        {
            Sum += productList.get(i).getTotalPrice();
        }

        return Sum;
    }
}
