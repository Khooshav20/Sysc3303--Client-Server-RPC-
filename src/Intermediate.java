package src;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

/**
 * @author Bundhoo Khooshav
 * <p>
 * This class represents an intermediate host that acts as an
 * intermediary between
 * a client and a server. It receives packets from a client, forwards
 * them to
 * a server, receives response packets from the server, and forwards
 * them back
 * to the client.
 */
public class Intermediate {
    DatagramSocket clientSocket, serverSocket; // Declaration of DatagramSocket objects for receiving and sending/receiving packets
    DatagramPacket fromClient, fromServer;

    public Intermediate() {
        try {
            clientSocket = new DatagramSocket(Constants.CLIENT_INTERMEDIATE_PORT);
            serverSocket = new DatagramSocket(Constants.SERVER_INTERMEDIATE_PORT);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1); // Exiting the program if an exception occurs
        }
    }

    /**
     * Main method to run the Intermediate host.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        Intermediate host = new Intermediate(); // Creating an instance of IntermediateHost class
        host.start(); // Starting the intermediate host
    }

    /**
     * Overrides the toString method to return "Intermediate Host".
     *
     * @return A string representation of the Intermediate host.
     */
    @Override
    public String toString() {
        return "Intermediate Host";
    }

    /**
     * Prints information about the DatagramPacket.
     *
     * @param packet      The DatagramPacket to print information about.
     * @param isSendPacket True if the packet is a send packet, false if it's a receive packet.
     */
    private void printPacketInfo(DatagramPacket packet, boolean isSendPacket) {
        int len = packet.getLength();
        System.out.println("\n*****************************************************************");
        System.out.printf("%s: %s packet:%n", this, isSendPacket ? "Sending" : "Received");
        System.out.printf("%s: %s:%d%n", isSendPacket ? "To" : "From", packet.getAddress(), packet.getPort());
        System.out.printf("Length: %d%n", len);

        if (len > 0) {
            byte[] data = packet.getData();
            byte[] trimmedData = Arrays.copyOf(data, len);
            System.out.printf("Containing String: %s%n", new String(trimmedData));
            System.out.printf("Containing Bytes: %s%n", Arrays.toString(trimmedData));
        }
    }

    /**
     * Sends a reply DatagramPacket.
     *
     * @param packet The DatagramPacket to reply to.
     */
    private void reply(DatagramPacket packet) {
        printPacketInfo(packet, true);
        Constants.sleep();

        try {
            DatagramSocket replySocket = new DatagramSocket();
            replySocket.send(packet);
            replySocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1); // Exiting the program if an exception occurs
        }
    }

    /**
     * Processes the received DatagramPacket.
     *
     * @param packet   The received DatagramPacket.
     * @param isServer True if the packet is from the server, false if it's from the client.
     */
    private synchronized void process(DatagramPacket packet, boolean isServer) {
        if (packet.getLength() > 0) {
            if (isServer) {
                fromServer = packet;
            } else {
                fromClient = packet;
            }
            DatagramPacket emptyPacket = new DatagramPacket(new byte[0], 0, packet.getAddress(), packet.getPort());
            reply(emptyPacket);
            notifyAll();
        } else {
            while ((isServer && fromClient == null) || (!isServer && fromServer == null)) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            DatagramPacket replyPacket = new DatagramPacket((isServer ? fromClient : fromServer).getData(), (isServer ? fromClient : fromServer).getLength(), packet.getAddress(), packet.getPort());
            reply(replyPacket);

            if (isServer) {
                fromClient = null;
            } else {
                fromServer = null;
            }
        }
    }

    /**
     * Receives DatagramPackets from the specified socket.
     *
     * @param socket   The DatagramSocket to receive packets from.
     * @param isServer True if the packets are from the server, false if they're from the client.
     */

    private void receive(DatagramSocket socket, boolean isServer) {
        byte[] buffer = new byte[100];
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

        try {
            socket.receive(receivePacket);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        printPacketInfo(receivePacket, false);
        process(receivePacket, isServer);
        Constants.sleep();
    }

    /**
     * Starts the Intermediate host by listening for packets from both client and server.
     */
    public void start() {
        new Thread(() -> {
            while (true) {
                receive(clientSocket, false);
            }
        }).start();

        new Thread(() -> {
            while (true) {
                receive(serverSocket, true);
            }
        }).start();
    }
}
