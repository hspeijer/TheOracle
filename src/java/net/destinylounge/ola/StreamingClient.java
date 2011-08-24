package net.destinylounge.ola;


import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

import net.destinylounge.ola.rpc.StreamRpcChannel;
import ola.proto.Ola;

/**
 * Created by IntelliJ IDEA.
 * User: hspeijer
 * Date: 8/21/11
 * Time: 11:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class StreamingClient {

    public static int OLA_DEFAULT_PORT = 9090;

    private Socket socket;
    private Ola.OlaServerService service;
    private StreamRpcChannel channel;

    public boolean setup() {
        return this.setup("127.0.0.1");
    }

    public boolean setup(String serverIp) {
        try {
            socket = new Socket(serverIp, OLA_DEFAULT_PORT);

            socket.connect(socket.getRemoteSocketAddress());

            channel = new StreamRpcChannel(socket);
            service = Ola.OlaServerService.newStub(channel);

            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void stop() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public boolean sendDmx(int universe, DmxBuffer buffer) {
        Ola.DmxData.Builder request = Ola.DmxData.newBuilder();
          request.setUniverse(universe);
          request.setData(buffer.getData());
          service.streamDmxData(null, request.build(), null);

        return true;  //To change body of created methods use File | Settings | File Templates.
    }
}
