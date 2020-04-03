package GUI;

import Amazon.DataBase;
import Amazon.USER;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartUp extends JDialog {
    private JPanel contentPane;
    private JButton buttonsignin;
    private JButton signup;
    private JButton cancelButton;
    private JTextField username;
    private JTextField pass;
    private DataBase dataBase;
    public StartUp(DataBase Data) {
        dataBase = Data;
        setContentPane(contentPane);
        setModal(true);
        setSize(200, 200);
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - this.getWidth()) / 2;
        final int y = (screenSize.height - this.getHeight()) / 2;
        setLocation(x, y);
        getRootPane().setDefaultButton(buttonsignin);
        buttonsignin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSignIn();
            }
        });

        signup.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onSignUp();
            }
        });
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
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });
    }

    private void onSignIn() {
        boolean found = dataBase.search(username.getText(), pass.getText());
        if (found){
            USER user = dataBase.get_user(username.getText());
            MainWindow mainWindow = new MainWindow(dataBase, username.getText(), dataBase.getName(username.getText()), user.getType());
            mainWindow.setLocationRelativeTo(null);
            mainWindow.pack();
            mainWindow.setVisible(true);
            this.dispose();
        }
        else {
            MessageBox mb = new MessageBox();
            mb.setLocationRelativeTo(null);
            mb.setText("Wrong username or password");
            mb.pack();
            mb.setVisible(true);
        }
    }

    private void onSignUp() {
        // add your code here if necessary
        SignUp signUp = new SignUp(dataBase);
        signUp.setLocationRelativeTo(null);
        signUp.pack();
        signUp.setVisible(true);
    }

    private void onCancel() {
        dataBase.close();
        dispose();
    }

}
