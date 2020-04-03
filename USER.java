package Amazon;

public class USER {
    private String name , ID , password , email , number , address, BirthDate;
    private Cart cart;
    private Payment payment;
    private Product[] favourite;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private int year, month, day;

    public String getBirthDate() {
        return BirthDate;
    }

    public void setBirthDate(Integer y, Integer m, Integer d) {
        String month, day;
        if (m < 10)
            month = "0" + m.toString();
        else
            month = m.toString();
        if (d < 10)
            day = "0" + d.toString();
        else
            day = d.toString();

        BirthDate = y.toString() + "-" + month + "-" + day;
    }

    public USER() {
    }
    public USER(String name, String ID, String password, String email, String number, String address, Cart cart, Payment payment, Product[] favourite) {
        this.name = name;
        this.ID = ID;
        this.password = password;
        this.email = email;
        this.number = number;
        this.address = address;
        this.cart = cart;
        this.payment = payment;
        this.favourite = favourite;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public Cart getCart() {
        return cart;
    }
    public void setCart(Cart cart) {
        this.cart = cart;
    }
    public Payment getPayment() {
        return payment;
    }
    public void setPayment(Payment payment) {
        this.payment = payment;
    }
    public Product[] getFavourite() {
        return favourite;
    }
    public void setFavourite(Product[] favourite) {
        this.favourite = favourite;
    }
}
