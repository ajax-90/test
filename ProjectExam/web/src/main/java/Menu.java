import com.engagepoint.university.dao.ProductDAOejb;
import entities.Product;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;
import java.util.List;


@ManagedBean
@ViewScoped
public class Menu {

    private List<Product> filtered = new ArrayList<Product>();
    private List<Product> selected = new ArrayList<Product>();
    @EJB
    ProductDAOejb productDAOejb;

    @PostConstruct
    public void init() {

        filtered.addAll(productDAOejb.getAllProducts());
    }

    public void order() {
        for (Product item : productDAOejb.getAllProducts())
            if (item.isOrdered())
                selected.add(item);
    }

    public List<Product> getSelected() {
        return selected;
    }

    public void setSelected(List<Product> selected) {
        this.selected = selected;
    }

    public List<Product> getMenuItemList() {
        return productDAOejb.getAllProducts();
    }

    public List<Product> getFiltered() {
        return filtered;
    }

    public void setFiltered(List<Product> filtered) {
        this.filtered = filtered;
    }




}
