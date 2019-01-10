package network;

import com.esotericsoftware.kryonet.Client;
import model.characters.PlayableCharacter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameClient extends Client {
    private Network.RegisterName registerName;
    private Network.RegisterNameList registerNameList;

    public GameClient(String IPHost) throws IOException {
        super();
        this.start();
        Network.register(this);
        this.connect(5000, IPHost, Network.getTcpPort(), Network.getUdpPort());
    }

    public void connectedListener(String name) {
        registerName = new Network.RegisterName();

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
        if (object instanceof Network.RegisterNameList) {
            registerNameList = (Network.RegisterNameList)object;
        }
        if (object instanceof PlayableCharacter) {
            PlayableCharacter playableCharacter = (PlayableCharacter) object;
            System.out.println(playableCharacter);
        }
    }

    public void sendPlayerInformation(PlayableCharacter character) {
        this.sendUDP(character);
    }

    public Network.RegisterNameList getRegisterNameList() {
        return registerNameList;
    }
}