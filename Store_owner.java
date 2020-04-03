package Amazon;

public class Store_owner extends USER {
    Store store;

    public Store_owner(Store store) {
        this.store = store;
    }
    public Store_owner() {
    }
    public Store getStore() {
        return store;
    }
    public void setStore(Store store) {
        this.store = store;
    }
}
