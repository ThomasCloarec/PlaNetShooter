package view.client.connection;

import javax.swing.*;

public class ServerFullError extends JOptionPane {
    public ServerFullError() {
        super();
        showMessageDialog(null, "This game server is full ! (10 players max)\n<html><div width='200px' align='center'><i>why not create your own server ? ;)</i></div></html>", "Server full", JOptionPane.ERROR_MESSAGE);
    }
}