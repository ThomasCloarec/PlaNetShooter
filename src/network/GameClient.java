package network;

import com.esotericsoftware.kryonet.Client;

import java.io.IOException;

public class GameClient extends Client {
    public GameClient(String IPHost) throws IOException {
        super();
        this.start();
        this.connect(5000, IPHost, Network.tcpPort, Network.udpPort);
        Network.register(this);

        this.connectedListener();

    }

    private void connectedListener() {
        Network.RegisterName registerName = new Network.RegisterName();

        double a = Math.random();
        if (a<0.25)
            registerName.name = "Jean";
        else if (a<0.5)
            registerName.name = "Helene";
        else if (a<0.75)
            registerName.name = "Paul";
        else
            registerName.name = "Monique";

        System.out.println(registerName.name);
        this.sendTCP(registerName);
    }

    public void receivedListener(Object object) {
        if (object instanceof Network.RegisterName) {
            Network.RegisterName registerName = (Network.RegisterName)object;
            System.out.println(registerName.name+ " connected !");
        }
    }
}