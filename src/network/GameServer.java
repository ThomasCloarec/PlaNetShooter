package network;

import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class GameServer {
    private Server server = new Server();
    public GameServer() throws IOException {
        server.start();
        server.bind(Network.tcpPort, Network.udpPort);

        Network.register(server);

        server.addListener(new Listener() {
        });
    }

    public void stop() {
        server.stop();
    }
}