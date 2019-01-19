package network;

import com.esotericsoftware.kryonet.Client;
import model.characters.PlayableCharacter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameClient extends Client {
    public Network.RegisterNameList registerNameList;
    private final List<PlayableCharacter> otherPlayers = new ArrayList<>();


    public GameClient(String IPHost) throws IOException {
        super();
        Network.register(this);
        this.start();
        this.connect(5000, IPHost, Network.getTcpPort(), Network.getUdpPort());
    }

    public void connectedListener(String name) {
        Network.RegisterName registerName = new Network.RegisterName();

        registerName.name = name;

        System.out.println("\"" + registerName.name+ "\" (You) is connected !");
        this.sendTCP(registerName);
    }

    public void receivedListener(Object object) {
        if (object instanceof Network.RegisterName) {
            Network.RegisterName registerName = (Network.RegisterName)object;
            System.out.println("\"" +registerName.name+ "\" is connected !");
            registerNameList.getList().add(registerName.name);
        }
        if (object instanceof Network.RemoveName) {
            Network.RemoveName removeName = (Network.RemoveName)object;
            System.out.println("\"" +removeName.name+ "\" is disconnected !");
            otherPlayers.remove(registerNameList.getList().indexOf(removeName.name));
            registerNameList.getList().remove(removeName.name);
        }
        if (object instanceof Network.RegisterNameList) {
            registerNameList = (Network.RegisterNameList)object;
        }
        if (object instanceof PlayableCharacter) {
            PlayableCharacter playableCharacter = (PlayableCharacter) object;
            if (registerNameList.getList().contains(playableCharacter.getName())) {
                if (otherPlayers.size() > registerNameList.getList().indexOf(playableCharacter.getName()))
                    otherPlayers.set(registerNameList.getList().indexOf(playableCharacter.getName()), playableCharacter);
                else
                    otherPlayers.add(playableCharacter);
            }
        }
    }

    public void sendPlayerInformation(PlayableCharacter character) {
        this.sendUDP(character);
    }

    public Network.RegisterNameList getRegisterNameList() {
        return registerNameList;
    }

    public List<PlayableCharacter> getOtherPlayers() {
        return otherPlayers;
    }
}