# Sysc3303--Client-Server-RPC-

Sysc3303- Assignment 4
Bundhoo Khooshav Nikhil - 101132063


This project implements a communication system using Datagram sockets in Java. It consists of several classes that represent different components of the communication system, including a client, a server, an intermediate host, and a host superclass. The system facilitates the exchange of datagrams between these components.

Communication Mechanism

The project utilizes Datagram sockets for communication between the client, server, and intermediate host. Additionally, it employs RPC (Remote Procedure Call) for managing the communication process between the client and intermediate host. This allows the client to send packets to the intermediate host and wait for direct responses confirming the reception of the packets. Subsequently, the intermediate host forwards the packets to the server upon receiving a request, and after receiving the acknowledgment packet from the server, it responds to the client, ensuring a smooth communication flow.

Classes

- Client: The Client class sends 11 packets to the intermediate host, alternating between read and write files five times each, followed by an invalid packet. It waits for direct responses from the intermediate host confirming the receipt of the packets, and then requests the server acknowledgment packets from the intermediate host for further processing.

- Server: The Server class sends a request to receive packets of data from the intermediate host. Upon receiving valid packets, it unpacks and prints the data. If it encounters an invalid packet, it throws an exception and exits.

- Intermediate: The Intermediate class runs indefinitely, waiting to receive packets from the client. Upon receiving a packet, it prints the packet information and sends a response to the client confirming the receipt. It then forwards the data to the server upon receiving a request. After receiving the acknowledgment packet from the server, it responds to the client, ensuring a seamless communication flow.

- Host: The Host class is an abstract superclass for hosts involved in communication. It provides common functionality for sending and receiving datagrams.

- Constants: The Constants class contains constant values used throughout the project, such as port numbers and sleep durations.
Usage

To use the project, follow these steps:

Compile the Java files in the src/ directory.
Run Intermediate.java to start the intermediate host, ensuring it is ready to receive data and requests.
Run Client.java or Server.java in any order to start the client or server.

Note: Ensure Intermediate.java is run first to handle communication setup properly. Additionally, manually shut down Intermediate.java between runs to start a fresh run.


Questions:

Qu1
Your suggestion to use two separate threads for the Intermediate task addresses a crucial issue encountered during implementation. By employing multiple threads, the Intermediate can effectively handle concurrent communication with both the client and server without risking misinterpretation of packets. Each thread can independently listen to its designated receive port (one for the client and one for the server), ensuring that the Intermediate correctly processes packets from each source. This approach prevents potential conflicts and eliminates the possibility of the Intermediate misinterpreting requests or responses, thereby enhancing the reliability and robustness of the communication system.


Qu2

In the Intermediate task, the necessity of using synchronized methods is questioned. It's noted that an alternative solution was successfully implemented without relying on synchronization. By utilizing separate receive ports for the client and server and having separate receibe sockets in the intermediate, potential conflicts over shared resources are avoided. Each component operates independently, and there is no risk of interference between communication channels. This streamlined approach simplifies implementation and ensures smooth communication between the components without the need for synchronization. Client and server will wait until the intermediate responds to them with the data that is relevant to them, before continuing to perform their tasks.

