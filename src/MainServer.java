import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import network.GameServer;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

class MainServer {
    private static GameServer gameServer;

    public static void main(String[] args) {
        Log.set(Log.LEVEL_NONE);
        System.out.println("Starting server...");
        launchGameServer();

        String ip = null;

        try {
            DatagramSocket socket = new DatagramSocket();
            socket.connect(InetAddress.getByName("8.8.8.8"), 42873);
            ip = socket.getLocalAddress().getHostAddress();
            if (ip.equals("0.0.0.0"))
                ip = "localhost";
        }
        catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }

        System.out.println(ip);
        //SwingUtilities.invokeLater(MainServer::launchServerFrame);
    }

    private static void launchGameServer() {
        gameServer = new GameServer();
        gameServer.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                gameServer.receivedListener(connection, object);
            }

            @Override
            public void connected(Connection connection) {
                gameServer.connectedListener(connection);
            }

            @Override
            public void disconnected(Connection connection) {
                gameServer.disconnectedListener(connection);
            }
        });
    }

    /*private static void launchServerFrame() {
        ServerFrame serverFrame = new ServerFrame();
        serverFrame.addWindowListener(new WindowAdapter() {
            public void windowClosed (WindowEvent evt) {
                gameServer.stop();
                System.out.println("Server closed");
            }
        });

        System.out.println("Server successfully started !");
    }*/
}