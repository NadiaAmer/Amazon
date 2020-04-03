package GUI;

import Amazon.DataBase;
import Amazon.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

public class AdminPanel extends JDialog {
    private JPanel contentPane;
    private JTextField brand_add;
    private JButton addBrand;
    private JTextField pname;
    private JTextField brand_name;
    private JSpinner price;
    private JButton add_product;
    private JTextField uid;
    private JButton makeAdminButton;
    private JSpinner balance;
    private JButton provideVocherCardButton;
    private JTable table;
    private JButton refreshButton;
    private JTextField catgeory;
    private JCheckBox onlineCheckBox;
    private DataBase dataBase;

    public AdminPanel(DataBase dataBase) {
        setSize(200, 200);
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - this.getWidth()) / 2;
        final int y = (screenSize.height - this.getHeight()) / 2;
        setLocation(x, y);
        setContentPane(contentPane);
        this.dataBase = dataBase;
        setModal(true);
        addBrand.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String brand = brand_add.getText();
                boolean done  = dataBase.add_brand(brand);
                MessageBox messageBox = new MessageBox();
                if (done)
                    messageBox.setText("Added Successfully.");
                else
                    messageBox.setText("Failed to add!");
                messageBox.exec();
            }
        });
        add_product.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = pname.getText();
                String brand = brand_name.getText();
                int Price = (int)price.getValue();
                Product product = new Product();
                product.setName(name);
                product.setBrand(brand);
                product.setPrice(Price);
                product.setCategory(catgeory.getText());
                if (onlineCheckBox.isSelected())
                    product.setType("Online");
                else
                    product.setType("Normal");
                int done = dataBase.add_product(product);
                MessageBox messageBox = new MessageBox();
                if (done != -1)
                    messageBox.setText("Added Successfully.");
                else
                    messageBox.setText("Failed to add!");
                messageBox.exec();

            }
        });
        makeAdminButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String UID = uid.getText();
                boolean done = dataBase.make_admin(UID);
                MessageBox messageBox = new MessageBox();
                if (done)
                    messageBox.setText("Added Successfully.");
                else
                    messageBox.setText("Failed to add!");
                messageBox.exec();
            }
        });
        provideVocherCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String UID = uid.getText();
                int value = (int)balance.getValue();
                boolean done = dataBase.set_voucher(UID, value);
                MessageBox messageBox = new MessageBox();
                if (done)
                    messageBox.setText("Added Successfully.");
                else
                    messageBox.setText("Failed to add!");
                messageBox.exec();
            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refresh();
            }
        });
        refresh();
    }
    private void refresh(){
        ResultSet rs = dataBase.get_suggested();
        try {
            table.setModel(buildTableModel(rs));
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
}
