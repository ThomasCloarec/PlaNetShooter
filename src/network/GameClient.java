package network;

import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Client;

import java.io.IOException;

public class GameClient {
    public GameClient(String IPHost) throws IOException {
        Client client = new Client();
        client.start();
        client.connect(5000, IPHost, Network.tcpPort, Network.udpPort);

        Network.register(client);

        client.addListener(new Listener() {
        });
    }
}