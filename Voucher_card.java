package Amazon;

public class Voucher_card  extends Payment{
    private
    String amount , end_date;

    public Voucher_card(String amount, String end_date) {
        this.amount = amount;
        this.end_date = end_date;
    }
    public Voucher_card() {
    }
    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getEnd_date() {
        return end_date;
    }
    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }
}
