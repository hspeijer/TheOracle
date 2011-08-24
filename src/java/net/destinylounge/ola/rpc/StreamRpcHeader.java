package net.destinylounge.ola.rpc;

import sun.tools.tree.ThisExpression;

/**
 * Created by IntelliJ IDEA.
 * User: hspeijer
 * Date: 8/22/11
 * Time: 5:17 PM
 */
public class StreamRpcHeader {
    public static int VERSION_MASK = 0xf0000000;
    public static int SIZE_MASK = 0x0fffffff;

    public int version;
    public int size;

      /*
       * The first 4 bytes are the header which contains the RPC protocol version
       * (this is separate from the protobuf version) and the size of the protobuf.
       */
    public StreamRpcHeader(int verison, int size) {
        this.version = version;
        this.size = size;
    }

    public static byte [] encodeHeader(int version, int size) {
        int header = 0;

        header = (version << 28) & VERSION_MASK;
        header |= size & SIZE_MASK;
        
        return new byte[] {
                        (byte)(header >>> 24),
                        (byte)(header >>> 16),
                        (byte)(header >>> 8),
                        (byte)header};
    }

    static StreamRpcHeader decodeHeader(byte [] bytes) {
        int header = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            header += (bytes[i] & 0x000000FF) << shift;
        }
        int version = (header & VERSION_MASK) >> 28;
        int size = header & SIZE_MASK;
        return new StreamRpcHeader(version, size);
    }
}
