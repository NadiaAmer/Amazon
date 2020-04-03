package GUI;

import Amazon.Cart;
import Amazon.DataBase;
import Amazon.Payment;
import Amazon.USER;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SignUp extends JDialog {
    private JPanel contentPane;
    private JButton buttonSignUp;
    private JButton buttonCancel;
    private JTextField username;
    private JTextField Pass;
    private JTextField Address;
    private JTextField Phone;
    private JTextField Email;
    private JSpinner Year;
    private JSpinner Month;
    private JSpinner Day;
    private JTextField name;
    private JCheckBox storeOwnerCheckBox;
    private DataBase dataBase;
    public SignUp(DataBase dataBase) {
        setSize(200, 200);
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - this.getWidth()) / 2;
        final int y = (screenSize.height - this.getHeight()) / 2;
        setLocation(x, y);
        this.dataBase = dataBase;
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonSignUp);

        SpinnerModel year, month, day;
        year = new SpinnerNumberModel(1900, 1900, 2017, 1);
        month = new SpinnerNumberModel(1, 1, 12, 1);
        day = new SpinnerNumberModel(1, 1, 32, 1);
        Year.setModel(year);
        Month.setModel(month);
        Day.setModel(day);
        buttonSignUp.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSignUp();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

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
    }

    private void clearAll(){
        username.setText(null);
        name.setText(null);
        Phone.setText(null);
        Email.setText(null);
        Pass.setText(null);
        Address.setText(null);
    }
    private void onSignUp() {
        USER user = new USER();
        user.setID(username.getText());
        user.setName(name.getText());
        user.setNumber(Phone.getText());
        user.setEmail(Email.getText());
        user.setBirthDate((int)Year.getValue(), (int)Month.getValue(), (int)Day.getValue());
        user.setPassword(Pass.getText());
        user.setAddress(Address.getText());
        Cart cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);
        user.setPayment(new Payment("c", 0));
        String type = "n";
        if (storeOwnerCheckBox.isSelected())
            type = "s";
        boolean done = dataBase.save_user(user, type);
        MessageBox messageBox = new MessageBox();
        if (done){
            messageBox.setText("Done.");
            messageBox.exec();
            clearAll();
        }
        else {
            messageBox.setText("Something went wrong!");
            messageBox.exec();
        }
        messageBox.exec();
    }
    private void onCancel() {
        dataBase.close();
        dispose();
    }

}
