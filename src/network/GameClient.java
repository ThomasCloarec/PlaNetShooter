package network;

import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Client;

import java.io.IOException;

public class GameClient extends Client {
    public GameClient(String IPHost) throws IOException {
        super();
        this.start();
        this.connect(5000, IPHost, Network.tcpPort, Network.udpPort);

        Network.register(this);

        this.addListener(new Listener() {
        });
    }
}