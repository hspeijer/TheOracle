package net.destinylounge.ola.rpc;

import com.google.protobuf.*;
import ola.proto.Ola;
import ola.rpc.Rpc;

import java.awt.image.ImagingOpException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by IntelliJ IDEA.
 * User: hspeijer
 * Date: 8/22/11
 * Time: 10:16 AM
 */
public class StreamRpcChannel implements RpcChannel {
    private Socket socket;
    private int messageId = 0;

    public static final int PROTOCOL_VERSION = 1;

    public StreamRpcChannel(Socket socket) {
        this.socket = socket;
    }

    public void callMethod(Descriptors.MethodDescriptor method, RpcController rpcController, Message request, Message prototype, RpcCallback<Message> done) {
        ByteString output;
        Rpc.RpcMessage.Builder message = Rpc.RpcMessage.newBuilder();
        boolean isStreaming = false;

        // Streaming methods are those with a reply set to STREAMING_NO_RESPONSE and
        // no controller, request or closure provided
        if (method.getOutputType().getName().equals("STREAMING_NO_RESPONSE")) {
          if (rpcController  != null || done != null) {
              System.out.println("Calling streaming method " + method.getName() +
              " but a controller, reply or closure in non-NULL");
            return;
          }
          isStreaming = true;
        }

        message.setType(isStreaming ? Rpc.Type.STREAM_REQUEST : Rpc.Type.REQUEST);
        message.setId(messageId++);
        message.setName(method.getName());

//        request. SerializeToString(&output);
        output = request.toByteString();
        message.setBuffer(output);
        sendMsg(message.build());

        if (isStreaming)
          return;

    }

    private void sendMsg(Rpc.RpcMessage message) {
        try {
            OutputStream stream = socket.getOutputStream();

            ByteString payload = message.toByteString();

            stream.write(StreamRpcHeader.encodeHeader(PROTOCOL_VERSION, payload.size()));
            stream.write(payload.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
