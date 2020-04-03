package Amazon;

public class Visa extends Payment{
    private String ID , ccv , date , owner_name;

    public Visa(String ID, String ccv, String date, String owner_name) {
        this.ID = ID;
        this.ccv = ccv;
        this.date = date;
        this.owner_name = owner_name;
    }
    public Visa() {
    }
    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }
    public String getCcv() {
        return ccv;
    }
    public void setCcv(String ccv) {
        this.ccv = ccv;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getOwner_name() {
        return owner_name;
    }
    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }
}
