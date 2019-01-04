package network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class GameServer extends Server {
    public GameServer() throws IOException {
        super();
        this.start();
        this.bind(Network.tcpPort, Network.udpPort);

        Network.register(this);
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
            System.out.println("\"" +registerName.name+ "\" is connected !");
        }
    }

    public void disconnectedListener(Connection connection) {
        GameConnection gameConnection = (GameConnection) connection;
        if (gameConnection.name != null) {
            Network.RemoveName removeName = new Network.RemoveName();
            removeName.name = gameConnection.name;
            this.sendToAllTCP(removeName);
            System.out.println("\"" +removeName.name+ "\" is disconnected !");
        }
    }

    @Override
    protected Connection newConnection() {
        return new GameConnection();
    }

    static class GameConnection extends Connection {
        String name;
    }
}