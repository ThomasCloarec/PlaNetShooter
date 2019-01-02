import network.GameServer;
import view.server.ServerFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class MainServer {
    public static void main(String[] args) throws IOException {
        GameServer gameServer = new GameServer();

        ServerFrame serverFrame = new ServerFrame();
        serverFrame.addWindowListener(new WindowAdapter() {
            public void windowClosed (WindowEvent evt) {
                gameServer.stop();
            }
        });
    }
}