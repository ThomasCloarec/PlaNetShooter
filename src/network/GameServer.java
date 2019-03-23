package network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import model.characters.PlayableCharacter;
import view.server.PortAlreadyUsedError;

import java.io.IOException;

public class GameServer extends Server {
    private final Network.RegisterList registerList = new Network.RegisterList();

    public GameServer() {
        super();
        new Thread(this).start();
        Network.register(this);
        try {
            this.bind(Network.getTcpPort(), Network.getUdpPort());
        }
        catch (IOException e) {
            new PortAlreadyUsedError();
            System.exit(1);
        }
    }

    public void receivedListener(Connection connection, Object object) {
        GameConnection gameConnection = (GameConnection)connection;
        if (object instanceof Network.RegisterName) {
            if (gameConnection.name != null) {
                return;
            }

            String name = ((Network.RegisterName)object).name;

            if (name == null)
                return;
            name = name.trim();
            if (name.length() == 0)
                return;

            gameConnection.name = name;
            Network.RegisterName registerName = new Network.RegisterName();
            registerName.name = name;
            this.sendToAllExceptTCP(gameConnection.getID(), registerName);
            System.out.println("\"" + registerName.name + "\" is connected ! [ID : " +gameConnection.getID()+ "]");

            registerList.getNameList().add(registerName.name);
            registerList.getConnectionIDList().add(gameConnection.getID());
        }
        if (object instanceof PlayableCharacter) {
            PlayableCharacter playableCharacter = (PlayableCharacter)object;
            this.sendToAllExceptUDP(gameConnection.getID(), playableCharacter);
        }
        if (object instanceof Network.Hit) {
            Network.Hit hit = (Network.Hit) object;
            this.sendToTCP(registerList.getConnectionIDList().get(registerList.getNameList().indexOf(hit.getVictimName())), hit);
        }
    }

    public void disconnectedListener(Connection connection) {
        GameConnection gameConnection = (GameConnection) connection;
        if (gameConnection.name != null) {
            Network.RemoveName removeName = new Network.RemoveName();
            removeName.name = gameConnection.name;
            this.sendToAllTCP(removeName);
            System.out.println("\"" +removeName.name+ "\" is disconnected ! [ID : " +gameConnection.getID()+ "]");

            registerList.getConnectionIDList().remove(registerList.getNameList().indexOf(removeName.name));
            registerList.getNameList().remove(removeName.name);
        }
    }

    public void connectedListener(Connection connection) {
        this.sendToTCP(connection.getID(), registerList);
    }

    @Override
    protected Connection newConnection() {
        return new GameConnection();
    }

    class GameConnection extends Connection {
        String name;
    }
}