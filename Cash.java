package Amazon;

public class Cash extends Payment{
    private
    double delivery_money , end_money;

    public Cash(double delivery_money, double end_money) {
        this.delivery_money = delivery_money;
        this.end_money = end_money;
    }
    public Cash() {
    }
    public double getDelivery_money() {
        return delivery_money;
    }
    public void setDelivery_money(double delivery_money) {
        this.delivery_money = delivery_money;
    }
    public double getEnd_money() {
        return end_money;
    }
    public void setEnd_money(double end_money) {
        this.end_money = end_money;
    }
}
