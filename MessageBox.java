package GUI;

import javax.swing.*;
import java.awt.*;

public class MessageBox extends JDialog {
    private JPanel contentPane;
    private JLabel label;

    public MessageBox() {
        setContentPane(contentPane);
        setModal(true);
        setSize(200, 200);
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final Dimension screenSize = toolkit.getScreenSize();
        final int x = (screenSize.width - this.getWidth()) / 2;
        final int y = (screenSize.height - this.getHeight()) / 2;
        setLocation(x, y);
    }
    public void setText(String  text){
        label.setText(text);
    }
    public void exec(){
        pack();
        setVisible(true);
    }
}
