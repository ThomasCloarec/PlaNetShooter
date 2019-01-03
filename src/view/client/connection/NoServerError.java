package view.client.connection;

import javax.swing.*;

public class NoServerError extends JOptionPane {
    public NoServerError() {
        super();
        showMessageDialog(null, "Please launch a game server before !", "No server found", JOptionPane.ERROR_MESSAGE);
    }
}
