package Amazon;

import java.util.Vector;

public class Store {
    private Vector <Product> products;
    private Vector<String> brands;
    private USER owner;
    private String name, add, type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public void add_product(Product product1){
        products.addElement(product1);
    }
    public void add_brand(String s){
        brands.addElement(s);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public USER getOwner() {
        return owner;
    }

    public void setOwner(USER owner) {
        this.owner = owner;
    }

    public Store(Vector<Product> product, Vector<String>  brands) {
        this.products = product;
        this.brands = brands;
    }
    public Store() {
        products = new Vector<>();
        brands = new Vector<>();
    }

    public Vector<Product> getProducts() {
        return products;
    }

    public void setProducts(Vector<Product> products) {
        this.products = products;
    }

    public Vector<String> getBrands() {
        return brands;
    }

    public void setBrands(Vector<String> brands) {
        this.brands = brands;
    }
}
