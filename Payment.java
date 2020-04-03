package Amazon;

public class Payment {
    private  String type;
    private float amount;
    private USER user;
    private String id;

    public Payment(String id) {
        this.id = id;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public USER getUser() {
        return user;
    }

    public void setUser(USER user) {
        this.user = user;
    }

    public Payment() {
    }
    public Payment(String type, float amount) {
        this.type = type;
        this.amount = amount;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public float getAmount() {
        return amount;
    }
    public void setAmount(float amount) {
        this.amount = amount;
    }
    public boolean buy (Order order){
        return true;
    }
}
