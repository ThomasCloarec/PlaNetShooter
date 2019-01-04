package network;

import com.esotericsoftware.kryonet.Client;

import java.io.IOException;

public class GameClient extends Client {
    private Network.RegisterNameList registerNameList;

    public GameClient(String IPHost) throws IOException {
        super();
        // NEVER put register() after connect() !
        // wasted two hours of my life because of this x)
        Network.register(this);
        this.start();
        this.connect(5000, IPHost, Network.tcpPort, Network.udpPort);
    }

    public void connectedListener(String name) {
        Network.RegisterName registerName = new Network.RegisterName();

        registerName.name = name;

        System.out.println("\"" +registerName.name+ "\" (You) is connected !");
        this.sendTCP(registerName);
    }

    public void receivedListener(Object object) {
        if (object instanceof Network.RegisterName) {
            Network.RegisterName registerName = (Network.RegisterName)object;
            System.out.println("\"" +registerName.name+ "\" is connected !");
        }
        if (object instanceof Network.RemoveName) {
            Network.RemoveName removeName = (Network.RemoveName)object;
            System.out.println("\"" +removeName.name+ "\" is disconnected !");
        }
        if(object instanceof Network.RegisterNameList) {
            System.out.println("RECEIVED");
            registerNameList = (Network.RegisterNameList)object;
        }
    }

    public Network.RegisterNameList getRegisterNameList() {
        return registerNameList;
    }
}