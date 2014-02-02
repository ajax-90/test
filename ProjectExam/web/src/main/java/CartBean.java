import entities.Product;
import entities.Order;
import utils.DataBaseSingleton;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;


@ManagedBean
@SessionScoped
public class CartBean {
    private Product selectSingle;

    private List<Product> selected;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Product> getSelected() {
        return selected;
    }

    public void setSelected(List<Product> selected) {
        this.selected = selected;
    }

    public void delete() {
        if (selected.contains(selectSingle)) {
            selected.remove(selectSingle);
        }
    }

    public String makeOrder(){
        Order order = new Order(name, selected);
        DataBaseSingleton.getInstance().addOrder(order);
        return "admin";
    }


    public Product getSelectSingle() {
        return selectSingle;
    }

    public void setSelectSingle(Product selectSingle) {
        this.selectSingle = selectSingle;
    }
}
