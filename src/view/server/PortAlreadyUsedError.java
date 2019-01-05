package view.server;

import javax.swing.*;

public class PortAlreadyUsedError extends JOptionPane {
    public PortAlreadyUsedError() {
        super();
        showMessageDialog(null, "A game server is already running on your computer !", "Port already used", JOptionPane.ERROR_MESSAGE);
    }
}