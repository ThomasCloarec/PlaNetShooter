package view.client.connection;

import javax.swing.*;

public class ServerFullError extends JOptionPane {
    public ServerFullError() {
        super();
        showMessageDialog(null, "The game server is full ! (10 players max)", "Server full", JOptionPane.ERROR_MESSAGE);
    }
}