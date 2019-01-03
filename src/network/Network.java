package network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

class Network {
    static final int tcpPort = 30082;
    static final int udpPort = 30083;

    static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(RegisterName.class);
    }

    static class RegisterName {
        String name;
    }
}