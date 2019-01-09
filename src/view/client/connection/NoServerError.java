package view.client.connection;

import javax.swing.*;

public class NoServerError extends JOptionPane {
    public NoServerError() {
        super();
        showMessageDialog(null, "Please launch a game server before !\n<html><div width='200px' align='center'><i>run \"Server-PlaNetShooter.jar\"</i></div></html>", "No server found", JOptionPane.ERROR_MESSAGE);
    }
}