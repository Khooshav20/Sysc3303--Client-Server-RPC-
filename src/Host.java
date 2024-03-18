package src;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/**
 * The Host class represents a host that sends and receives datagrams.
 */
public abstract class Host {
    protected DatagramSocket sendReceiveSocket;
    protected int outwardPort;

    /**
     * Constructs a Host instance with the specified outward port.
     *
     * @param outwardPort The port for outgoing communication.
     */
    public Host(int outwardPort) {
        this.outwardPort = outwardPort;

        try {
            sendReceiveSocket = new DatagramSocket(); // Initialize the DatagramSocket for sending and receiving datagrams
        } catch (SocketException e) {
            System.out.println("Socket creation failed due to an error: ");
            e.printStackTrace();
            System.exit(1); // Exiting the program if an exception occurs
        }
    }

    /**
     * Processes the received DatagramPacket.
     *
     * @param packet The DatagramPacket to process.
     */
    protected void process(DatagramPacket packet) {
        // Do nothing
    }

    /**
     * Sends data in a DatagramPacket to a specified host and port.
     *
     * @param data The byte array to be sent.
     */
    protected void send(byte[] data) {
        byte[] buffer = new byte[100];
        DatagramPacket sendPacket = null;
        DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);

        try {
            sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), outwardPort);
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.exit(1); // Exiting the program if an exception occurs
        }

        rpc_send(sendPacket, receivePacket); // Perform the RPC (Remote Procedure Call) send operation
        process(receivePacket); // Process the received packet
    }

    /**
     * Initiates a receiving operation by sending an empty packet.
     */
    protected void receive() {
        send(new byte[0]); // Send an empty packet to receive a response (ACK)
    }

    protected final void printPacketInfo(DatagramPacket packet, boolean isSendPacket) {
        int len = packet.getLength();
        System.out.printf("%s: %s packet:%n", this, isSendPacket ? "Sending" : "Received"); // Print whether the packet is being sent or received
        System.out.printf("%s: %s:%d%n", isSendPacket ? "To" : "From", packet.getAddress(), packet.getPort()); // Print the destination or source address and port
        System.out.printf("Length: %d%n", len); // Print the length of the packet

        if (len > 0) {
            byte[] data = packet.getData();
            byte[] trimmedData = Arrays.copyOf(data, len);
            System.out.printf("Containing String: %s%n", new String(trimmedData)); // Print the data in string format
            System.out.printf("Containing Bytes: %s%n", Arrays.toString(trimmedData)); // Print the data in byte array format
        }
    }

    /**
     * Performs the RPC (Remote Procedure Call) send operation.
     *
     * @param out The DatagramPacket to send.
     * @param in  The DatagramPacket to receive.
     */
    protected final void rpc_send(DatagramPacket out, DatagramPacket in) {
        try {
            System.out.println("\n*****************************************************************");
            printPacketInfo(out, true); // Print information about the outgoing packet
            Constants.sleep(); // Delay for simulation
            sendReceiveSocket.send(out); // Send the outgoing packet
            sendReceiveSocket.receive(in); // Receive the response packet
            System.out.println("-----------------------------------------------------------------");
            printPacketInfo(in, false); // Print information about the incoming packet
        } catch (IOException e) {
            System.out.println("Socket timed out: "); // Print error message for socket timeout
            e.printStackTrace(); // Print the stack trace of the exception
            System.exit(1); // Exiting the program if an exception occurs
        }
        }
}

