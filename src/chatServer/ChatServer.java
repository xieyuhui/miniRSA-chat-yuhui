package chatServer;
import java.math.BigInteger;
import java.net.*;
import java.io.*;

import miniRSA.MiniRSA;

public class ChatServer {  
	private Socket          socket   = null;
	private ServerSocket    server   = null;
	private DataInputStream streamIn =  null;

	public ChatServer(int port) {
		try	{
			System.out.println("Binding to port " + port + ", please wait  ...");
			//server = new ServerSocket(port);  
			server = new ServerSocket(9090, 0, InetAddress.getByName("localhost"));
			System.out.println("Server started: " + server);
			System.out.println("Waiting for a client ..."); 
			socket = server.accept();
			System.out.println("Client accepted: " + socket);
			open();
			
			String[] input = new String[2];
			System.out.println("Please enter the private key (d, c), first d, then c");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			input = in.readLine().split(" ");
			BigInteger d = new BigInteger(input[0]);
			BigInteger n = new BigInteger(input[1]);
			boolean done = false;
			while (!done) { 
				try {
					String line = streamIn.readLine();
					System.out.println("Received: \n" + line);
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
	public static void main(String args[])	{  
		ChatServer server = null;
		if (args.length != 1)
			System.out.println("Usage: java ChatServer port");
		else
			server = new ChatServer(Integer.parseInt(args[0]));
	}
}









