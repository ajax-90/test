package entities;


import java.util.ArrayList;
import java.util.List;


public class Order {

    private List<Product> selected = new ArrayList<Product>();
    private String name;

    public Order( String name, List<Product> selected) {
        this.selected = selected;
        this.name = name;
    }

    public List<Product> getSelected() {
        return selected;
    }

    public void setSelected(List<Product> selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
