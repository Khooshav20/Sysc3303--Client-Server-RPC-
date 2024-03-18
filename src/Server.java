package src;

import java.net.DatagramPacket;

/**
 * @author Bundhoo Khooshav
 * <p>
 * This client represents a server. A user will try to send information
 * to
 * this server from a client, and when a packet is received the server
 * will send a response back through the intermediate host.
 */
public class Server extends Host {

    public Server() {
        super(Constants.SERVER_INTERMEDIATE_PORT);
    }

    public static void main(String[] args) {
        Server server = new Server(); // Creating an instance of Server class
        server.start();
    }

    @Override
    public String toString() {
        return "Server";
    }

    public boolean isPacketValid(DatagramPacket packet) {
        byte[] data = packet.getData(); // Getting the data from the packet
        int len = packet.getLength(); // Getting the length of the packet
        int i = 0;

        // last byte is 0
        if (data[len - 1] != 0) { // Checking if the last byte is 0
            return false; // Returning false if the last byte is not 0
        }

        // first byte[0] is 0
        if (data[i] != 0) { // Checking if the first byte is 0
            return false; // Returning false if the first byte is not 0
        }
        i++;

        // second byte[1] is either 1 or 2
        if (data[i] != 1 && data[i] != 2) { // Checking if the second byte is 1 or 2
            return false; // Returning false if the second byte is neither 1 nor 2
        }
        i++;

        // filename is present
        while (data[i] != 0) { // Looping until reaching a null byte which indicates end of filename
            i++;
            if (i >= len) { // Checking if the index exceeds the length of the packet
                return false; // Returning false if index exceeds the length of the packet
            }
        }
        i++;

        // mode is present
        while (data[i] != 0) { // Looping until reaching a null byte which indicates end of mode
            i++;
            if (i >= len) { // Checking if the index exceeds the length of the packet
                return false; // Returning false if index exceeds the length of the packet
            }
        }

        return true; // Returning true if all validation checks pass
    }

    @Override
    protected void process(DatagramPacket packet) {
        if (packet.getLength() == 0) {
            return;
        }

        if (!isPacketValid(packet)) { // Checking if the received packet is valid
            throw new RuntimeException("Invalid packet received. Exiting.");
        }

        byte[] receivedData = packet.getData(); // Getting data from the received packet
        byte[] sendData;

        // Read request
        if (receivedData[1] == 1) { // Checking if it's a read request
            sendData = new byte[]{0, 3, 0, 1}; // Creating data for response if ReadRequest
        } else { // write request
            sendData = new byte[]{0, 4, 0, 0}; // Creating data for response if WriteRequest
        }

        send(sendData);
    }

    /**
     * Starts the Server by continuously receiving packets.
     */
    public void start() {
        while (true) { // Infinite loop for continuously receiving packets
            Constants.sleep();
            receive();
        }
    }
}