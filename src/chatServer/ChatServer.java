package chatServer;
import java.math.BigInteger;
import java.net.*;
import java.io.*;

import miniRSA.MiniRSA;

public class ChatServer {  
	private Socket          socket   = null;
	private ServerSocket    server   = null;
	private DataInputStream streamIn =  null;
	static int yourPort = -1;
	static int otherPort = -1;
	static ChatClient client;
	String line;
	private static BigInteger d, n, e, c;

	public ChatServer(int port) {
		try	{
			System.out.println("Binding to port " + port + ", please wait  ...");
			//server = new ServerSocket(port);  
			server = new ServerSocket(port, 0, InetAddress.getByName("localhost"));
			System.out.println("Server started: " + server);
			System.out.println("Waiting for a client ..."); 
			
			if (otherPort != -1) {
				client.start();
			}
						
			socket = server.accept();
			System.out.println("Client accepted: " + socket);
			open();
			
			//
			if (otherPort == -1) {
				line = streamIn.readLine();
				System.out.println("Server received other server's port: \n" + line);
				otherPort = Integer.parseInt(line);
				client = new ChatClient(otherPort, e, c);
				client.start();
			}

			boolean done = false;
			while (!done) { 
				try {
					line = streamIn.readLine();
					System.out.println("Server received: \n" + line);
					if (!line.equalsIgnoreCase(".bye")) {
						MiniRSA.decryptPrint(line, d, n); 
					}
					done = line.equals(".bye");
				}
				catch(IOException ioe) {
					done = true;
				}
			}
			
			close();
		}
		catch(IOException ioe) {
			System.out.println(ioe); 
		}
	}
	public void open() throws IOException {
		streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
	}
	public void close() throws IOException	{
		if (socket != null)    socket.close();
		if (streamIn != null)  streamIn.close();
	}
	public static void main(String args[]) {  
		if (args.length != 6 && args.length != 4 && args.length != 5) {
			System.out.println("Chat Server-1 Usage: your_port public_key_e public_key_c private_key_d private_key_c");
			System.out.println("Chat Server-2 Usage: your_port other_port public_key_e public_key_c private_key_d private_key_c");
			System.out.println("Cracker Server Usage: server_port client_port public_key_e public_key_c");
			return;
		}
		//first set up the server, don't know other's server port
		if (args.length == 5) {
			yourPort = Integer.parseInt(args[0]);
			e = new BigInteger(args[1]);
			c = new BigInteger(args[2]);
			d = new BigInteger(args[3]);
			n = new BigInteger(args[4]);
			ChatServer cs = new ChatServer(yourPort);	
		}
		//second set up the server, known other's server port
		if (args.length == 6) {
			yourPort = Integer.parseInt(args[0]);
			otherPort = Integer.parseInt(args[1]);
			e = new BigInteger(args[2]);
			c = new BigInteger(args[3]);
			d = new BigInteger(args[4]);
			n = new BigInteger(args[5]);
			client = new ChatClient(yourPort, otherPort, e, c);
			ChatServer cs = new ChatServer(yourPort);	
		}
		else if (args.length == 4) {
			int serverPort = Integer.parseInt(args[0]);
			int clientPort = Integer.parseInt(args[1]);
			e = new BigInteger(args[2]);
			c = new BigInteger(args[3]);
			d = MiniRSA.crack(e, c);
			n = new BigInteger(args[3]);
			client = new ChatClient(clientPort, e, c);
			ChatServer cs = new ChatServer(serverPort);	
			
		}
		
		
	}
}









