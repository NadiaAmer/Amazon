package Amazon;

public class Order {
    private String ID ;
    private double deliver_price , product_price, total_price ;
    private Product [] products;
    private USER customer;

    public Order() {
    }
    public Order(String ID, double deliver_price, double product_price, double total_price, USER customer) {

        this.ID = ID;
        this.deliver_price = deliver_price;
        this.product_price = product_price;
        this.total_price = total_price;
        this.customer = customer;
    }
    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public double getDeliver_price() {
        return deliver_price;
    }
    public void setDeliver_price(double deliver_price) {
        this.deliver_price = deliver_price;
    }
    public double getProduct_price() {
        return product_price;
    }
    public void setProduct_price(double product_price) {
        this.product_price = product_price;
    }
    public double getTotal_price() {
        return total_price;
    }
    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }
    public Product[] getProducts() {
        return products;
    }
    public void setProducts(Product[] products) {
        this.products = products;
    }
    public USER getCustomer() {
        return customer;
    }
    public void setCustomer(USER customer) {
        this.customer = customer;
    }
}
