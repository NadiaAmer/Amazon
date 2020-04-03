package Amazon;

public class Cart {
    private Product products;
    private double price;
    private USER myUser;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Cart(String id) {

        this.id = id;
    }

    public USER getUser() {
        return myUser;
    }

    public void setUser(USER myUser) {
        this.myUser = myUser;
    }

    public Cart() {
        price = 0;
    }
    public Cart(Product products, double price) {
        this.products = products;
        this.price = price;
    }
    public Product getProducts() {
        return products;
    }
    public void setProducts(Product products) {
        this.products = products;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public boolean add_product(Product products){
        return true;
    }
    public boolean delete_product(Product products){
        return true;
    }


}
