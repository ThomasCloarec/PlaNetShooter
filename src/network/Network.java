package network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;

import java.util.ArrayList;
import java.util.List;

public class Network {
    static final int tcpPort = 30082;
    public static final int udpPort = 30083;

    static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(RegisterName.class);
        kryo.register(RemoveName.class);
        kryo.register(RegisterNameList.class);
        kryo.register(java.util.ArrayList.class);
    }

    static class RegisterName {
        String name;
    }

    static class RemoveName {
        String name;
    }

    public static class RegisterNameList {
        public final List<String> list = new ArrayList<>();
    }
}