package chatServer;
import java.math.BigInteger;
import java.net.*;
import java.io.*;

import miniRSA.MiniRSA;

public class ChatServer {  
	private Socket          socket   = null;
	private ServerSocket    server   = null;
	private DataInputStream streamIn =  null;
	static ChatClient client;
	private static BigInteger d, n, e, c;

	public ChatServer(int port) {
		try	{
			System.out.println("Binding to port " + port + ", please wait  ...");
			//server = new ServerSocket(port);  
			server = new ServerSocket(port, 0, InetAddress.getByName("localhost"));
			System.out.println("Server started: " + server);
			System.out.println("Waiting for a client ..."); 
			client.start();
			socket = server.accept();
			System.out.println("Client accepted: " + socket);
			open();
			
			boolean done = false;
			while (!done) { 
				try {
					String line = streamIn.readLine();
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
		if (args.length != 6 && args.length != 4) {
			System.out.println("Chat Server Usage: server_port client_port public_key_e public_key_c private_key_d private_key_c");
			System.out.println("Cracker Server Usage: server_port client_port public_key_e public_key_c");
			return;
		}
		if (args.length == 6) {
			int serverPort = Integer.parseInt(args[0]);
			int clientPort = Integer.parseInt(args[1]);
			e = new BigInteger(args[2]);
			c = new BigInteger(args[3]);
			d = new BigInteger(args[4]);
			n = new BigInteger(args[5]);
			client = new ChatClient(clientPort, e, c);
			ChatServer cs = new ChatServer(serverPort);	
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









