package network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class GameServer extends Server {
    private static Network.RegisterNameList registerNameList = new Network.RegisterNameList();

    public GameServer() throws IOException {
        super();
        Network.register(this);
        this.start();
        // TODO : Show message when ports are already used
        this.bind(Network.tcpPort, Network.udpPort);
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
            System.out.println("\"" + registerName.name + "\" is connected !");

            registerNameList.list.add(registerName.name);


        }
    }

    public void disconnectedListener(Connection connection) {
        GameConnection gameConnection = (GameConnection) connection;
        if (gameConnection.name != null) {
            Network.RemoveName removeName = new Network.RemoveName();
            removeName.name = gameConnection.name;
            this.sendToAllTCP(removeName);
            System.out.println("\"" +removeName.name+ "\" is disconnected !");

            registerNameList.list.remove(removeName.name);
        }
    }

    public void connectedListener(Connection connection) {
        System.out.println("SEND TO " +connection.getID());
        System.out.println(registerNameList.list);
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