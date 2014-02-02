import ejb.ServiceEjb;
import entities.Order;
import entities.Product;
import utils.DataBaseSingleton;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean
@SessionScoped
public class AdminBean {

    private List<Order> orders;
    private String[] array = {"item0","item1","item2","item3"};
    private List<String> selectedColumns = Arrays.asList(array);

    private float sum;

    @EJB
    ServiceEjb service;

    public List<Order> getOrders() {
        orders = DataBaseSingleton.getInstance().getOrders();
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void setService(ServiceEjb service) {
        this.service = service;
    }

    public float getSum() {
        sum=service.productSum();
        return sum;
    }

    public void setSum(float sum) {
        this.sum = sum;
    }
    public List<String> getSelectedColumns() {
        return selectedColumns;
    }

    public void setSelectedColumns(List<String> selectedColumns) {
        this.selectedColumns = selectedColumns;
    }

    Map<Integer, String> row=new HashMap<Integer, String>();
    public void init(){
        for(Order order:orders){
            List<Product> products = order.getSelected();
            row.put(1,order.getName());
            for(Product product:products)
                row.put(product.getId() + 1, String.valueOf(product.getAmount()));
        }
    }



}
