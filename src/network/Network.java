package network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import model.characters.PlayableCharacter;

import java.util.ArrayList;
import java.util.List;

public class Network {
    private static final int TCP_PORT = 30082;
    private static final int UDP_PORT = 30083;

    static void register(EndPoint endPoint) {
        Kryo kryo = endPoint.getKryo();
        kryo.register(RegisterName.class);
        kryo.register(RemoveName.class);
        kryo.register(RegisterNameList.class);
        kryo.register(java.util.ArrayList.class);
        kryo.register(PlayableCharacter.class);
    }

    static class RegisterName {
        String name;
    }

    static class RemoveName {
        String name;
    }

    public static class RegisterNameList {
        private final List<String> list = new ArrayList<>();

        public List<String> getList() {
            return list;
        }
    }

    static int getTcpPort() {
        return TCP_PORT;
    }

    public static int getUdpPort() {
        return UDP_PORT;
    }
}