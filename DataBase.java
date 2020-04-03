package Amazon;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Objects;
import java.util.Vector;

public class DataBase {
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:8889/YourStore";
    private static final String USER = "root";
    private static final String PASS = "root";
    private static Connection conn;
    private static Statement stmt;
    public DataBase() {
        conn = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
        } catch(Exception e){
            e.printStackTrace();
        }
    }
    private boolean exec(String qry){
        try {
            stmt = conn.createStatement();
            int i = stmt.executeUpdate(qry);
            return i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    private ResultSet get_result(String qry){
        try {
            stmt = conn.createStatement();
            return stmt.executeQuery(qry);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void close() {
        try {
            if (stmt != null)
                stmt.close();
        } catch (SQLException se2) {
            se2.printStackTrace();
        }
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
    }
    public boolean save_user(USER user ,String type){
        String cid = String.valueOf(Add_cart());
        String pid = String.valueOf(add_payment(user.getPayment()));
        String sql = "INSERT INTO `User` (`Uid`, `Name`, `Pass`, `BirthDate`, `Address`, `Email`, `Phone`, `type`, `Cartid`, `Payid`)VALUES( '"+user.getID()+"' , '"+user.getName()+"', '"+user.getPassword()+"', '"+user.getBirthDate()+"', '"+user.getAddress()+"', '"+user.getEmail()+"', '"+user.getNumber()+"',  '"+type+"' , "+cid+", "+pid+");";
        exec(sql);
        sql = "update `Cart` set `Uid` = '"+user.getID()+"' where `Cid` = "+cid+"";
        return exec(sql);
    }
    public int add_payment(Payment payment){
        String sql = "INSERT INTO `Payment` (`type`, `Amount`)\n" +
                "VALUES\n" +
                "\t( '"+payment.getType()+"', "+String.valueOf(payment.getAmount())+");";
        exec(sql);
        sql = "select max(`Payid`) from `Payment`";
        ResultSet rs = get_result(sql);
        try {
            rs.first();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public boolean search(String id, String pass){
        String sql = "select `Uid` , `Pass` from `User`";
        ResultSet rs = get_result(sql);
        try {
            assert rs != null;
            while (rs.next()){
                if (Objects.equals(rs.getString("Uid"), id) && Objects.equals(rs.getString("Pass"), pass))
                    return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public ResultSet search(String key, char t){
        String qry;
        if (t == 'n'){
            qry = "Select p.`Productid` as 'Product ID', p.`Name` as 'Product Name'  , p.`price` , p.`Category` , p.`Type` , b.`Name` as 'Brand Name'\n" +
                    "from `Product` as p, `Brand` as b\n" +
                    "where b.`Bid` = p.`Brand_id`\n" +
                    "and (p.`Name` like '%"+key+"%' or b.`Name` like '%"+key+"%' or  p.`Category` like '%"+key+"%' );";
        }
        else {
            qry = "Select p.`Productid` as 'Product ID', p.`Name` as 'Product Name'  , p.`price`  , p.`Category`, p.`Type` , p.`Shows` as 'Number of views'  , b.`Name` as 'Brand Name'\n" +
                    "from `Product` as p, `Brand` as b\n" +
                    "where b.`Bid` = p.`Brand_id`\n" +
                    "and (p.`Name` like '%" + key + "%' or b.`Name` like '%" + key + "%' or  p.`Category` like '%\"+key+\"%');";
        }
        ResultSet rs = get_result(qry);
        try {
            assert rs != null;
            while (rs.next()){
                exec("update `Product` set `Shows` = `Shows` + 1 where `Productid` = "+rs.getString(1)+"");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }
    public int add_product(Product product){
        String sql_check = "SELECT `Name`, `Bid` from `Brand`;";
        boolean done = false;
        int bid = -1;
        try {
            ResultSet rs = get_result(sql_check);
            assert rs != null;
            while (rs.next()){
                if (rs.getString(1).equals(product.getBrand())){
                    done = true;
                    bid = rs.getInt(2);
                    break;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        if (done) {
            String sql = "INSERT INTO `Product` (`Name`, `Brand_id`, `price`, `Category` , `Type`)" +
                    "VALUES" +
                    "('"+product.getName()+"', "+String.valueOf(bid)+", "+String.valueOf(product.getPrice())+" , '"+product.getCategory()+"', '"+product.getType()+"');";
            exec(sql);
            try {
                ResultSet resultSet = get_result("select max(`Productid`)from `Product`;");
                resultSet.first();
                bid = resultSet.getInt(0);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return bid;
    }
    public boolean add_store(Store store){
        Vector<Product> products = store.getProducts();
        Vector<String> brands = store.getBrands();
        String sql_check = "SELECT `Name` from `Store`;";
        boolean done = true;
        try {
            ResultSet rs = get_result(sql_check);
            assert rs != null;
            while (rs.next()){
                if (Objects.equals(rs.getString(0), store.getName())){
                    done = false;
                    break;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        if (done){
            String sql = "INSERT INTO `Store` (`Name`, `Owner`, `Address`, `Type`) " +
                    "VALUES " +
                    "('"+store.getName()+"', '"+store.getOwner().getID()+"', '"+store.getAdd()+"', '"+store.getType()+"');";
            exec(sql);
            String  sid = "1";
            try {
                sid = get_result("select max(`Sid`)from `Store`;").getString(0);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            for (String brand : brands)
                exec("INSERT INTO `Store_brand` (`Sid`, `Bid`) " +
                        "VALUES " +
                        " (" + sid + ", (SELECT `Bid` from `Brand` where `Name` = '" + brand + "';) );");
            for (Product product : products)
                exec("INSERT INTO `Store_item` (`Sid`, `Pid`) " +
                        "VALUES " +
                        "(" + sid + ", " + String.valueOf(add_product(product)) + ");");
            return true;
        }
        else {
            return false;
        }
    }
    public boolean add_brand(String brand){
        String sql_check = "SELECT `Name`, `Bid` from `Brand`;";
        boolean done = true;
        try {
            ResultSet rs = get_result(sql_check);
            assert rs != null;
            while (rs.next()){
                if (Objects.equals(rs.getString(0), brand)){
                    done = false;
                    break;
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        if (done){
            String sql = "INSERT INTO `Brand` (`Name`) " +
                    "VALUES" +
                    " ('"+brand+"');";
            exec(sql);
        }
        return done;
    }
    public String getName(String id){
        try {
            String sql = "SELECT `Name` FROM `User` where `Uid` = '"+id+"';";
            ResultSet rs = get_result(sql);
            assert rs != null;
            rs.first();
            return rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
    public int Add_cart(){
        String sql = "insert into `Cart` (`price`) values (0)";
        exec(sql);
        sql = "select max(`Cid`) from `Cart`";
        ResultSet rs = get_result(sql);
        try {
            rs.first();
            return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }
    public boolean add_to_cart(String uid, String pid){
        String sql = null;
        try {
            String cid;
            ResultSet resultSet = get_result("select `Cartid` from `User` where `Uid` = "+uid+"");
            resultSet.first();
            cid = resultSet.getString(1);
            sql = "INSERT INTO `CartItem` (`Pid`, `Cid`)\n" +
                    "VALUES\n" +
                    "\t("+pid+", "+cid+");\n";
            double total = get_cart(cid).getPrice() + get_product(pid).getPrice();
            exec("update `Cart` set `price` = "+String.valueOf(total)+" where `Cid` = "+cid+" ;");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exec(sql);
    }
    public boolean add_to_fav(String uid, String pid){
        String sql = null;
        sql = "INSERT INTO `FavItem` (`Pid`, `Uid`)\n" +
                    "VALUES\n" +
                    "\t("+pid+", "+uid+");\n";
        return exec(sql);
    }
    public ResultSet get_carts(String uid, char t){
        String sql;
        if (t == 'n'){
            sql = "select `Pid` as 'Product Id' , p.`Name` as 'Product Name' , (SELECT `Name` from `Brand` where `Bid` = `Brand_id`) as 'Brand', p.`Price` , p.`Category` , p.`Type` \n" +
                    "from  `Cart` as c, `CartItem` as ci, `Product` as p\n" +
                    "WHERE c.`Uid` = '"+uid+"'\n" +
                    "and  c.`Cid` = ci.`Cid`\n" +
                    "and p.`Productid` = ci.`Pid`;";
        }
        else {
            sql = "select `Pid` as 'Product Id' , p.`Name` as 'Product Name' , (SELECT `Name` from `Brand` where `Bid` = `Brand_id`) as 'Brand', p.`Price` , p.`Category` , p.`Type` , p.`Shows` as 'Number of views'\n" +
                    "from  `Cart` as c, `CartItem` as ci, `Product` as p\n" +
                    "WHERE c.`Uid` = '" + uid + "'\n" +
                    "and  c.`Cid` = ci.`Cid`\n" +
                    "and p.`Productid` = ci.`Pid`;";
        }
        return get_result(sql);
    }
    public ResultSet get_fav(String uid, char t){
        String sql;
        if (t == 'n'){
            sql = "select `Pid` as 'Product Id' , p.`Name` as 'Product Name' , (SELECT `Name` from `Brand` where `Bid` = `Brand_id`) as 'Brand', p.`Price` , p.`Category` , p.`Type` \n" +
                    "                from   `FavItem` as ci, `Product` as  p\n" +
                    "                WHERE ci.`Uid` = '"+uid+"'\n" +
                    "                and p.`Productid` = ci.`Pid`;";
        }
        else {
            sql = "select `Pid` as 'Product Id' , p.`Name` as 'Product Name' , (SELECT `Name` from `Brand` where `Bid` = `Brand_id`) as 'Brand', p.`Price` , p.`Category` , p.`Type` , p.`Shows` as 'Number of views'\n" +
                    "                from   `FavItem` as ci, `Product` as  p\n" +
                    "                WHERE ci.`Uid` = '"+uid+"'\n" +
                    "                and p.`Productid` = ci.`Pid`;";
        }
        return get_result(sql);
    }
    public USER get_user(String uid){
        USER user = new USER();
        ResultSet resultSet = get_result("select * from `User` where `Uid` = '"+uid+"';");
        try {
            resultSet.first();
            user.setID(uid);
            user.setPayment(get_payment(resultSet.getString("PayID")));
            user.setCart(get_cart(resultSet.getString("Cartid")));
            user.setType(resultSet.getString("type"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    public Cart get_cart(String cid){
        String sql = "select * from `Cart` where `Cid` = "+cid+"";
        ResultSet resultSet = get_result(sql);
        Cart cart = new Cart(cid);
        try {
            resultSet.first();
            cart.setPrice(resultSet.getDouble(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cart;
    }
    public Payment get_payment(String pid){
        String sql = "select * from `Payment` where `Payid` = "+pid+"";
        ResultSet resultSet = get_result(sql);
        Payment pay = new Payment(pid);
        try {
            resultSet.first();
            pay.setAmount(resultSet.getFloat(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pay;
    }
    public Product get_product(String pid){
        String sql = "select * from `Product` where `Productid` = "+pid+"";
        ResultSet resultSet = get_result(sql);
        Product p = new Product(pid);
        try {
            resultSet.first();
            p.setPrice(resultSet.getFloat(4));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return p;
    }
    public int buy(String uid, String type)  {
        USER user = get_user(uid);
        String sql = "";
        int on = -1;
        if (user.getCart().getPrice() == 0)
            return -1;
        if (type.equals("cash")){
            sql = "INSERT INTO `Order` ( `Dprice`, `Pprice`, `Total_price`, `Cid`, `Pid`)\n" +
                    "VALUES\n" +
                    "\t( 150, "+String.valueOf(user.getCart().getPrice())+", "+String.valueOf(user.getCart().getPrice() + 150)+", '"+uid+"', "+user.getPayment().getId()+");\n";
            exec(sql);
            sql = "select max(`O_num`) from `Order`";
            ResultSet resultSet = get_result(sql);
            try {
                resultSet.first();
                on = resultSet.getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return on;
        }
        else {
            if (user.getCart().getPrice() > user.getPayment().getAmount())
                return -1;
            else {
                double new_balance = user.getPayment().getAmount() - user.getCart().getPrice();
                sql = "update `Payment` set `Amount` = " + String.valueOf(new_balance) + " where `Payid` = " + String.valueOf(user.getPayment().getId()) + ";";
                String order = "INSERT INTO `Order` ( `Dprice`, `Pprice`, `Total_price`, `Cid`, `Pid`)\n" +
                        "VALUES\n" +
                        "\t( 100, "+String.valueOf(user.getCart().getPrice())+", "+String.valueOf(user.getCart().getPrice())+", '"+uid+"', "+user.getPayment().getId()+");\n";
                exec(sql);
                exec(order);
                sql = "select max(`O_num`) from `Order`";
                ResultSet resultSet = get_result(sql);
                try {
                    resultSet.first();
                    on = resultSet.getInt(1);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                return on;
            }
        }
    }
    public boolean make_order(String on,  String uid){
        String sql;
        ResultSet resultSet = get_carts(uid, 's');
        try {
            while (resultSet.next()){
                sql = "INSERT INTO `OrderItem` (`Pid`, `Order_id`)\n" +
                        "VALUES\n" +
                        "\t("+resultSet.getString(1)+", "+on+");\n";
                exec(sql);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean make_admin(String uid){
        String sql = "update `User` set `type` = 'a' where `Uid` = '"+uid+"'";
        return exec(sql);
    }
    public boolean set_voucher(String uid, int value){
        USER user = get_user(uid);
        String pid = user.getPayment().getId();
        value += user.getPayment().getAmount();
        String sql = "update `Payment` set `Amount` = "+String.valueOf(value)+" where `Payid` = "+pid+" ;";
        return exec(sql);
    }
    public ResultSet getStores(String uid){
        return get_result("select `Name` from `Store` where `Owner` = '"+uid+"';");
    }
    public String get_store_id(String name){
        String sql = "Select `Sid` from `Store` where `Name` = '"+name+"';";
        ResultSet resultSet = get_result(sql);
        String id = "-1";
        try {
            resultSet.first();
            id = resultSet.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
    public boolean add_to_store(String pid, String sid){
        String check = "select `Type`  , (select `Type` from `Store` where `Sid` = "+sid+") as 'sid' from `Product` where `Productid` = "+pid+"";
        ResultSet resultSet = get_result(check);
        boolean done = false;
        try {
            resultSet.first();
            done = (Objects.equals(resultSet.getString(1), resultSet.getString(2)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!done)
            return false;
        String sql = "INSERT INTO `Store_item` (`Sid`, `Pid`)\n" +
                "VALUES\n" +
                "\t("+sid+", "+pid+");\n";
        return exec(sql);
    }
    public boolean suggest(String pname, String bname){
        String sql = "INSERT INTO `Sug` ( `pname`, `bname`)\n" +
                "VALUES\n" +
                "\t( '"+pname+"', '"+bname+"');\n";
        return exec(sql);
    }
    public ResultSet get_suggested(){
        String sql = "select `bname` as 'Brand Name', `pname` as 'Product Name' from `Sug`;";
        ResultSet resultSet = get_result(sql);
        return resultSet;
    }
    private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        Vector<Vector<String>> data = new Vector<>();
        rs.beforeFirst();
        while (rs.next()) {
            Vector<String> vector = new Vector<>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++)
                vector.addElement(rs.getString(columnIndex));
            data.addElement(vector);
        }

        return new DefaultTableModel(data, columnNames);

    }
    public ResultSet get_all_in_store(String s) {
        String sid = get_store_id(s);
        String sql = "Select p.`Productid` as 'Product ID', p.`Name` as 'Product Name'  , p.`price` , (SELECT `Name` from `Brand` where `Bid` = `Brand_id`) as 'Brand', p.`Shows` as 'Number of views'\n" +
                "from `Product` as p, `Store_item` as s\n" +
                "where s.`Sid` = "+sid+"\n" +
                "and p.`Productid` = s.`Pid`";
        return get_result(sql);
    }
    public void delete_cart_items(String cid){
        String sql;
        sql = "delete from `CartItem` where `Cid` = "+cid+"";
        exec(sql);
        sql = "update `Cart` set `price` = 0 where `Cid` = "+cid+" ";
        exec(sql);
    }
    public Order get_order(String on){
        String sql = "select * from `Order` where `O_num` = "+on+"";
        ResultSet resultSet = get_result(sql);
        Order order = new Order();
        try {
            resultSet.first();
            order.setCustomer(get_user(resultSet.getString("Cid")));
            order.setDeliver_price(resultSet.getDouble(2));
            order.setID(on);
            order.setProduct_price(resultSet.getDouble(3));
            order.setTotal_price(order.getDeliver_price() + order.getProduct_price());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return order;
    }
    public String max_order(){
        String sql = "select `Pid` , count(`Pid`) as 'count', `Name`  \n" +
                "from `OrderItem`, `Product`\n" +
                "where `Pid` = `Productid`\n" +
                "group by `Pid`\n" +
                "ORDER BY count desc;";
        ResultSet resultSet = get_result(sql);
        try {
            resultSet.first();
            return resultSet.getString(3);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  "";
    }
    public String max_brand(){
        String sql = "select `Pid` , count(`Pid`) as 'count', `Brand`.`Name`  from `OrderItem`, `Brand`, `Product`\n" +
                "where `Bid` = `Brand_id`\n" +
                "and `Productid` = `Pid`\n" +
                "group by `Pid`\n" +
                "ORDER BY count desc;";
        ResultSet resultSet = get_result(sql);
        try {
            resultSet.first();
            return resultSet.getString(3);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  "";
    }
    public String n_shows(String sid){
        String sql = "select sum(`Shows`) \n" +
                "from  `Product`, `Store_item` , `Store` \n" +
                "where `Store`.`Sid` = `Store_item`.`Sid`\n" +
                "and `Product`.`Productid` = `Store_item`.`Pid`\n" +
                "and `Store`.`Owner` = '"+sid+"';";
        ResultSet resultSet = get_result(sql);
        try {
            resultSet.first();
            return resultSet.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  "";
    }
    public String n_users(String sid){
        String sql = "select count(distinct `Productid`)\n" +
                "from  `Product`, `Store_item` , `Store`, `OrderItem`\n" +
                "where `Store`.`Sid` = `Store_item`.`Sid`\n" +
                "and `Product`.`Productid` = `Store_item`.`Pid`\n" +
                "and `Store`.`Owner` = '"+sid+"'\n" +
                "and `OrderItem`.`Pid` = `Product`.`Productid`;";
        ResultSet resultSet = get_result(sql);
        try {
            resultSet.first();
            return resultSet.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  "";
    }
    public boolean delete_cart_item(String cid, String pid){
        String sql = "delete from `CartItem` where `Cid` = "+cid+" and `Pid` = "+pid+"";
        return exec(sql);
    }
}