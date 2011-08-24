package net.destinylounge.ola;

import com.google.protobuf.ByteString;

/**
 * Created by IntelliJ IDEA.
 * User: hspeijer
 * Date: 8/21/11
 * Time: 11:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class DmxBuffer {
    public static final int DMX_UNIVERSE_SIZE = 512;

    private byte [] buffer = new byte[DMX_UNIVERSE_SIZE];

    public void blackout() {
        for(int i = 0; i < DMX_UNIVERSE_SIZE; i++) {
            buffer[i] = 0;
        }
    }

    public void setChannel(int channel, byte value) {
        buffer[channel] = value;
    }

    public ByteString getData() {
        return ByteString.copyFrom(buffer);
    }
}
