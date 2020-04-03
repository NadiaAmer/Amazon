package GUI;

import Amazon.DataBase;
import Amazon.Store;
import Amazon.USER;

import javax.swing.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StoreOwner extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JTextField sname;
    private JTextField sadd;
    private JButton addButton;
    private JTextField pid;
    private JComboBox snames;
    private JButton addButton1;
    private JTextField pname;
    private JTextField brandname;
    private JButton suggestButton;
    private JCheckBox onlineCheckBox;
    private JTextField n_users;
    private JTextField n_buy;
    private JTextField max_order;
    private JTextField max_brand;
    private DataBase dataBase;
    private String sid;
    public StoreOwner(DataBase dataBase, String uid) {
        sid = uid;
        this.dataBase = dataBase;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        update();
        n_buy.setText(dataBase.n_users(uid));
        n_users.setText(dataBase.n_shows(uid));
        max_order.setText(dataBase.max_order());
        max_brand.setText(dataBase.max_brand());
        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name, add;
                name = sname.getText();
                add = sadd.getText();
                Store store = new Store();
                store.setName(name);
                USER user = dataBase.get_user(uid);
                store.setOwner(user);
                store.setAdd(add);
                if (onlineCheckBox.isSelected())
                    store.setType("Online");
                else
                    store.setType("Normal");
                boolean done = dataBase.add_store(store);
                MessageBox messageBox = new MessageBox();
                if (done)
                    messageBox.setText("Added Successfully.");
                else
                    messageBox.setText("Failed to add!");
                messageBox.exec();
                update();
            }
        });
        addButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = pid.getText(), sid = dataBase.get_store_id(snames.getSelectedItem().toString());
                boolean done = dataBase.add_to_store(id, sid);
                MessageBox messageBox = new MessageBox();
                if (done)
                    messageBox.setText("Added Successfully.");
                else
                    messageBox.setText("Failed to add!");
                messageBox.exec();
            }
        });
        suggestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String bname, pName;
                bname = brandname.getText();
                pName = pname.getText();
                boolean done = dataBase.suggest(pName, bname);
                MessageBox messageBox = new MessageBox();
                if (done)
                    messageBox.setText("Suggested Successfully.");
                else
                    messageBox.setText("Failed to suggest!");
                messageBox.exec();
            }
        });
        onlineCheckBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (onlineCheckBox.isSelected()) {
                    sadd.setEnabled(false);
                    sadd.setText(null);
                }
                else
                    sadd.setEnabled(true);
            }
        });
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
    @SuppressWarnings("Duplicates")
    private void update(){
        ResultSet resultSet = dataBase.getStores(sid);
        try {
            snames.removeAllItems();
            while (resultSet.next()){
                snames.addItem(resultSet.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
