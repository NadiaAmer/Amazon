package GUI;

import Amazon.DataBase;
import Amazon.Order;
import Amazon.USER;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Vector;

public class MainWindow extends JDialog {
    private JPanel contentPane;
    private JTextField username;
    private JTextField name;
    private JTextField search;
    private JTable table;
    private JScrollPane list;
    private JButton searchButton;
    private JTextField pid;
    private JButton addToCartButton;
    private JButton buyCartButton;
    private JComboBox comboBox1;
    private JButton showButton;
    private JButton addToFavouriteButton;
    private JButton adminPanelButton;
    private JButton storeOwnerPanelButton;
    private JComboBox snames;
    private JLabel mostp;
    private JLabel label;
    private JCheckBox vocherCardCheckBox;
    private JTextField wallet;
    private JButton refreshButton;
    private JButton removeFromCartButton;
    private DataBase dataBase;
    private String type;
    public MainWindow(DataBase dataBase, String id, String Name, String type) {
        this.type = type;
        setSize(200, 200);
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - this.getWidth()) / 2;
        final int y = (screenSize.height - this.getHeight()) / 2;
        setLocation(x, y);
        if (Objects.equals(type, "n")){
            adminPanelButton.setVisible(false);
            adminPanelButton.setEnabled(false);
            storeOwnerPanelButton.setEnabled(false);
            storeOwnerPanelButton.setVisible(false);
            mostp.setVisible(false);
            label.setVisible(false);
            snames.setVisible(false);
            comboBox1.removeItemAt(2);
        }
        else if (Objects.equals(type, "s")){
            adminPanelButton.setVisible(false);
            adminPanelButton.setEnabled(false);
        }
        else if (Objects.equals(type, "a")){
            storeOwnerPanelButton.setEnabled(false);
            storeOwnerPanelButton.setVisible(false);
            mostp.setVisible(false);
            label.setVisible(false);
            snames.setVisible(false);
            comboBox1.removeItemAt(2);
        }
        username.setText(id);
        name.setText(Name);
        this.dataBase = dataBase;
        setContentPane(contentPane);
        setModal(true);
        USER user = dataBase.get_user(id);
        wallet.setText(String.valueOf(user.getPayment().getAmount()));

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ResultSet rs = dataBase.search(search.getText(), type.charAt(0));
                    table.setModel(buildTableModel(rs));
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dataBase.close();
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean done =  dataBase.add_to_cart(username.getText(), pid.getText());
                MessageBox messageBox = new MessageBox();
                if (done)
                    messageBox.setText("Added Successfully.");
                else
                    messageBox.setText("Failed to add!");
                messageBox.exec();
            }
        });
        showButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ResultSet resultSet;
                if (comboBox1.getSelectedItem().equals("Cart"))
                    resultSet = dataBase.get_carts(username.getText(), type.charAt(0));
                else if (comboBox1.getSelectedItem().equals("by Store name")){
                    resultSet = dataBase.get_all_in_store(snames.getSelectedItem().toString()) ;
                }
                else
                    resultSet = dataBase.get_fav(username.getText(), type.charAt(0));
                try {
                    table.setModel(buildTableModel(resultSet));
                    if (comboBox1.getSelectedItem().equals("by Store name"))
                        max_viewed();
                    else
                        mostp.setText(null);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        });
        addToFavouriteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean done =  dataBase.add_to_fav(username.getText(), pid.getText());
                MessageBox messageBox = new MessageBox();
                if (done)
                    messageBox.setText("Added Successfully.");
                else
                    messageBox.setText("Failed to add!");
                messageBox.exec();
            }
        });
        buyCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = "", Type = "VCard";
                MessageBox messageBox = new MessageBox();
                USER user = dataBase.get_user(username.getText());
                if (!vocherCardCheckBox.isSelected())
                    Type = "cash";
                int on = dataBase.buy(username.getText(), Type);
                boolean done = dataBase.make_order(String.valueOf(on), username.getText());
                if (done && on > 0) {
                    Order order = dataBase.get_order(String.valueOf(on));
                    messageBox.setText("Order Number : " + String.valueOf(on) + ". Delivery Price : " + String.valueOf(order.getDeliver_price())
                    + ". Product Prices : " + String.valueOf(order.getProduct_price()) +
                            ". Total : " + String.valueOf(order.getTotal_price())
                    );
                    dataBase.delete_cart_items(user.getCart().getId());
                }
                else
                    messageBox.setText("Failed to buy!");
                messageBox.exec();
            }
        });
        adminPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AdminPanel adminPanel = new AdminPanel(dataBase);
                adminPanel.pack();
                adminPanel.setVisible(true);
            }
        });
        storeOwnerPanelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StoreOwner storeOwner = new StoreOwner(dataBase, username.getText());
                storeOwner.pack();
                storeOwner.setVisible(true);
            }
        });
        update();

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                USER user = dataBase.get_user(id);
                wallet.setText(String.valueOf(user.getPayment().getAmount()));
            }
        });
        removeFromCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cid = dataBase.get_user(username.getText()).getCart().getId();
                String p = pid.getText();
                dataBase.delete_cart_item(cid, p);
            }
        });
    }
    private static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        Vector<String> columnNames = new Vector<>();
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnLabel(column));
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
    private void update(){
        ResultSet resultSet = dataBase.getStores(username.getText());
        try {
            snames.removeAllItems();
            while (resultSet.next()){
                snames.addItem(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void max_viewed(){
        int max = 0;
        int maxindex = -1;
        for (int i = 0; i < table.getRowCount(); i++) {
            int tmp = Integer.valueOf(table.getValueAt(i, 4).toString());
            if (tmp > max){
                max = tmp;
                maxindex = i;
            }
        }
        if (maxindex != -1){
            mostp.setText(table.getValueAt(maxindex, 1).toString());
        }
        else {
            mostp.setText(null);
        }
    }
}