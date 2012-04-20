package chatServer;
import java.math.BigInteger;
import java.net.*;
import java.util.ArrayList;
import java.io.*;

import miniRSA.MiniRSA;

public class ChatServer implements Runnable {  
	private static Socket socket = null;
	private static BigInteger e, c, d, n, cli_e, cli_c;
	private static Thread  writingTd = null;

	ChatServer(Socket s) {
		socket = s;
	}

	public void chat() {
		try	{	        
	        System.out.println("Reading from client...");
			//first get client public key(e, c)
	        
	        DataInputStream  streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
	        
			String fromClient = "";
			String[] clientPubKey = null;
			//while (!in.ready()) {}
			fromClient = streamIn.readLine();	
			clientPubKey = fromClient.split("#");
			cli_e = new BigInteger(clientPubKey[0]);
			cli_c = new BigInteger(clientPubKey[1]);
			System.out.println("Received client public key(e, c): \n" + cli_e + " " + cli_c);
			
			writingTd = new Thread(this); 
	        writingTd.start();

	        fromClient = "";
			while ((fromClient = streamIn.readUTF()) != null) {
				System.out.println("Received: \n" + fromClient);
				if (fromClient.equals(".bye"))
					break;
				MiniRSA.decryptPrint(fromClient, d, n); 
				System.out.println("DECRYPTED to " + fromClient);
			}
			streamIn.close();
		}
		catch (IOException e) {
			System.err.println("Unable to read from client!"); 
		}
	}
	
	@Override
	public void run() {
		try {			
			System.out.println("Writing to client...");
			DataOutputStream streamOut = new DataOutputStream(socket.getOutputStream());
//			System.out.println("e#c = " + e.toString() + "#" + c.toString());
			streamOut.writeBytes(e.toString() + "#" + c.toString() + '\n');
            
			
//			while(cli_e == null) {
//				//waiting client send his public key to me
//			}
			
			boolean done = false;
			String toClient = "";
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while (!done) {	
				System.out.println("type, enter .bye to quit");
				toClient = in.readLine();
				System.out.print("ENCRYPTED " + toClient);
				if (!toClient.equalsIgnoreCase(".bye")) {
					ArrayList<BigInteger> encryptedNumList = MiniRSA.encrypt(toClient, cli_c, cli_e);
					toClient = "";
					for (int i = 0; i < encryptedNumList.size(); i++) {
						toClient += encryptedNumList.get(i).toString();
						toClient += " ";
					}
					System.out.println(" to " + toClient);
				}
				else done = true;
				streamOut.writeBytes(toClient + '\n');
			}
			streamOut.close(); 
		} 
		catch (IOException e) {
			System.err.println("Unable to write to " + socket); }

	}

	public static void main(String args[]) throws IOException {  
		if (args.length != 5) {
			System.out.println("Server Usage: port# public_key_e public_key_c private_key_d private_key_c");
			return;
		}
		int port = Integer.parseInt(args[0]);
		e = new BigInteger(args[1]);
		c = new BigInteger(args[2]);
		d = new BigInteger(args[3]);
		n = new BigInteger(args[4]);
		System.out.println("Binding to port " + port + ", please wait  ...");
		ServerSocket server = new ServerSocket(port);
		System.out.println("Server started: " + server);
		System.out.println("Waiting for a client ..."); 
		Socket skt = server.accept();
		System.out.println("Client accepted: " + skt);
		
		ChatServer cs = new ChatServer(skt);
		cs.chat();
		skt.close();
		server.close();
	}

	
		
}










