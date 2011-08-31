package net.destinylounge.ola;

/**
 * Created by IntelliJ IDEA.
 * User: hspeijer
 * Date: 8/21/11
 * Time: 11:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class StreamClientTest {
    public static void main(String [] args) {
        int universe = 1;  // universe to use for sending data

        // Create a new DmxBuffer to hold the data
        DmxBuffer buffer = new DmxBuffer();
        // set all channels to 0
        buffer.blackout();

        // create a new client and set the Error Closure
        StreamingClient olaClient = new StreamingClient();

        // Setup the client, this connects to the server
        if (!olaClient.setup("192.168.1.1")) {
            System.out.println("Setup failed");
            System.exit(1);
        }

        // send the data to the ola server
        for (byte i = 0; i < 100; i++) {
          buffer.setChannel(0, i);
          buffer.setChannel(1, i);

            if (!olaClient.sendDmx(universe, buffer)) {
                System.out.println( "Send DMX failed");
                System.exit(1);
            }
            try {
                Thread.sleep(20);   // sleep for 20ms between updates
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }

        // close the connection
        olaClient.stop();
    }
}
