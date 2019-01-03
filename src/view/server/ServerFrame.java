package view.server;

import javax.swing.*;

import java.awt.*;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import static javax.swing.SwingConstants.CENTER;

public class ServerFrame extends JFrame {
    private String ip = null;

    public ServerFrame() {
        super();

        try {
            DatagramSocket socket = new DatagramSocket();
            socket.connect(InetAddress.getByName("8.8.8.8"), 42873);
            ip = socket.getLocalAddress().getHostAddress();
        }
        catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(this::createFrame);
    }

    private void createFrame() {
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setSize(250, 100);
        this.setLocationRelativeTo(null);
        this.setLayout(new GridLayout(2,1));

        JLabel closeInformation = new JLabel("Close to stop the chat server.");
        closeInformation.setHorizontalAlignment(CENTER);
        this.getContentPane().add(closeInformation);

        if (ip != null) {
            JLabel hostInformation = new JLabel("Host : " + ip);
            hostInformation.setHorizontalAlignment(CENTER);
            this.getContentPane().add(hostInformation);
        }

        this.setVisible(true);
    }
}