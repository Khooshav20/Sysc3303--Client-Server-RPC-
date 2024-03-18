package src;

import java.util.Random;

/**
 * @author Bundhoo Khooshav
 * <p>
 * This class represents a client that a user could use to interface
 * with a server. It takes files and sends them to a host which
 * interfaces with
 * server using datagram sockets.
 */
public class Client extends Host {

    public Client() {
        super(Constants.CLIENT_INTERMEDIATE_PORT);
    }

    /**
     * Main method to run the Client.
     */
    public static void main(String[] args) {
        Client c = new Client(); // Creating an instance of Client class
        c.start(); // Starting the client
    }

    @Override
    public String toString() {
        return "Client";
    }

    /**
     * Create packet data to be sent in a datagram.
     *
     * @param filename      Name of the file to include in the packet.
     * @param mode          Mode to include in the packet.
     * @param isReadRequest True if it's a read request, false if it's a write
     *                      request.
     * @return Byte array representing the packet data.
     */
    private byte[] createPacketData(String filename, String mode, boolean isReadRequest) {
        byte[] filenameBytes = filename.getBytes(); // Getting bytes of filename
        byte[] modeBytes = mode.getBytes(); // Getting bytes of mode
        byte[] packetData = new byte[2 + filenameBytes.length + 1 + modeBytes.length + 1]; // Creating byte array to hold
        // packet data
        packetData[0] = 0;
        packetData[1] = (byte) (isReadRequest ? 1 : 2); // Second byte indicating read (1) or write (2) request

        // Setting filename bytes in packet data
        System.arraycopy(filenameBytes, 0, packetData, 2, filenameBytes.length);

        packetData[filenameBytes.length + 2] = 0; // Setting null byte after filename

        for (int i = 0; i < modeBytes.length; i++) {
            packetData[i + filenameBytes.length + 3] = modeBytes[i]; // Setting mode bytes in packet data
        }

        packetData[filenameBytes.length + modeBytes.length + 3] = 0; // Setting null byte after mode

        return packetData; // Returning the packet data
    }

    /**
     * Start the client .
     */
    public void start() {
        Random random = new Random(); // Creating an instance of Random class for generating random modes

        for (int i = 1; i <= 10; i++) { // Loop for sending multiple packets
            boolean isReadRequest = i % 2 != 0; // Alternating between read and write requests
            String mode = random.nextBoolean() ? "netascii" : "octet"; // Generating mode randomly
            String filename = "test" + i + ".txt"; // Creating filename

            byte[] packetData = createPacketData(filename, mode, isReadRequest); // Creating packet data
            Constants.sleep();
            send(packetData);
            Constants.sleep();
            receive();
        }

        byte[] packetData = createPacketData("test.txt", "netascii", true);
        packetData[0] = 1; // force an invalid request
        send(packetData);
        Constants.sleep();
        receive();

        sendReceiveSocket.close();
    }
}