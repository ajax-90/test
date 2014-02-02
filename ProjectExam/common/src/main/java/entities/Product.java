package entities;

public class Product {

    private String pictureUrl;
    private String description;
    private float price;
    private boolean ordered;
    private int amount;
    private float totalPrice;

    public float getTotalPrice() {
        totalPrice=amount*price;
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;


    public Product(int id, String pictureUrl, String description, float price) {
        this.id=id;
        this.pictureUrl = pictureUrl;
        this.description = description;
        this.price = price;

    }


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setOrdered(boolean ordered) {
        this.ordered = ordered;
    }

    public boolean isOrdered() {
        return ordered;
    }
    public String getPictureUrl() {
        return pictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        this.pictureUrl = pictureUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    @Override
    public String toString(){
        return description+" "+price;
    }

}