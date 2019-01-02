package network;

import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class GameServer extends Server {
    public GameServer() throws IOException {
        super();
        this.start();
        this.bind(Network.tcpPort, Network.udpPort);

        Network.register(this);

        this.addListener(new Listener() {
        });
    }
}