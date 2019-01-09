package network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import model.characters.PlayableCharacter;
import view.server.PortAlreadyUsedError;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GameServer extends Server {
    private final Network.RegisterNameList registerNameList = new Network.RegisterNameList();

    public GameServer() {
        super();
        this.start();
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

            registerNameList.getList().add(registerName.name);
        }
        if (object instanceof ArrayList) {
            List arrayList = (ArrayList)object;
            if (arrayList.size() == 2)
                if (arrayList.get(0) instanceof Network.RegisterName && arrayList.get(1) instanceof PlayableCharacter)
                    this.sendToAllExceptUDP(gameConnection.getID(), arrayList);
        }
    }

    public void disconnectedListener(Connection connection) {
        GameConnection gameConnection = (GameConnection) connection;
        if (gameConnection.name != null) {
            Network.RemoveName removeName = new Network.RemoveName();
            removeName.name = gameConnection.name;
            this.sendToAllTCP(removeName);
            System.out.println("\"" +removeName.name+ "\" is disconnected ! [ID : " +gameConnection.getID()+ "]");

            registerNameList.getList().remove(removeName.name);
        }
    }

    public void connectedListener(Connection connection) {
        this.sendToTCP(connection.getID(), registerNameList);
    }

    @Override
    protected Connection newConnection() {
        return new GameConnection();
    }

    class GameConnection extends Connection {
        String name;
    }
}