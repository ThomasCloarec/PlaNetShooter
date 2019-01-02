package view.server;

import javax.swing.*;

public class ServerFrame extends JFrame {
    public ServerFrame() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.getContentPane().add(new JLabel("Close to stop the chat server."));
        this.setSize(215, 100);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
