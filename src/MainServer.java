import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import network.GameServer;
import view.server.ServerFrame;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

class MainServer {
    private static GameServer gameServer;

    public static void main(String[] args) {
        System.out.println("Starting server...");
        launchGameServer();
        SwingUtilities.invokeLater(MainServer::launchServerFrame);
    }

    private static void launchGameServer() {
        gameServer = new GameServer();
        gameServer.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                gameServer.receivedListener(connection, object);
            }

            @Override
            public void connected(Connection connection) {
                gameServer.connectedListener(connection);
            }

            @Override
            public void disconnected(Connection connection) {
                gameServer.disconnectedListener(connection);
            }
        });
    }

    private static void launchServerFrame() {
        ServerFrame serverFrame = new ServerFrame();
        serverFrame.addWindowListener(new WindowAdapter() {
            public void windowClosed (WindowEvent evt) {
                gameServer.stop();
                System.out.println("Server closed.");
            }
        });

        System.out.println("Server successfully started !");
    }
}