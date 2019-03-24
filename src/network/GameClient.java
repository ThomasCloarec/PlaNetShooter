package network;

import com.esotericsoftware.kryonet.Client;
import model.characters.PlayableCharacter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameClient extends Client {
    private Network.RegisterList registerList;
    private final List<PlayableCharacter> otherPlayers = new ArrayList<>();

    public GameClient(String IPHost) throws IOException {
        super(32288,16144);
        new Thread(this).start();
        Network.register(this);
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
            registerList.getNameList().add(registerName.name);
        }
        if (object instanceof Network.RemoveName) {
            Network.RemoveName removeName = (Network.RemoveName)object;
            System.out.println("\"" +removeName.name+ "\" is disconnected !");
            otherPlayers.remove(registerList.getNameList().indexOf(removeName.name));
            registerList.getNameList().remove(removeName.name);
        }
        if (object instanceof Network.RegisterList) {
            registerList = (Network.RegisterList)object;
        }
        if (object instanceof PlayableCharacter) {
            PlayableCharacter playableCharacter = (PlayableCharacter) object;

            if (playableCharacter.getName() != null && registerList != null) {
                if (registerList.getNameList().contains(playableCharacter.getName())) {
                    if (otherPlayers.size() > registerList.getNameList().indexOf(playableCharacter.getName()))
                        otherPlayers.set(registerList.getNameList().indexOf(playableCharacter.getName()), playableCharacter);
                    else
                        otherPlayers.add(playableCharacter);
                }
            }
        }
    }

    public void sendPlayerInformation(PlayableCharacter character) {
        this.sendUDP(character);
    }

    public void sendHit(Network.Hit hit) {
        this.sendTCP(hit);
    }

    public Network.RegisterList getRegisterList() {
        return registerList;
    }

    public List<PlayableCharacter> getOtherPlayers() {
        return otherPlayers;
    }
}