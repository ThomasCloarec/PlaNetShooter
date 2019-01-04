import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import network.GameServer;
import view.server.ServerFrame;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

class MainServer {
    public static void main(String[] args) throws IOException {
        GameServer gameServer = new GameServer();
        gameServer.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                gameServer.receivedListener(connection, object);
            }

            @Override
            public void disconnected(Connection connection) {
                gameServer.disconnectedListener(connection);
            }
        });

        SwingUtilities.invokeLater(() -> {
            ServerFrame serverFrame = new ServerFrame();
            serverFrame.addWindowListener(new WindowAdapter() {
                public void windowClosed (WindowEvent evt) {
                    gameServer.stop();
                    System.out.println("Server closed.");
                }
            });
        });
    }
}