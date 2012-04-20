package chatServer;
import java.math.BigInteger;
import java.net.*;
import java.util.ArrayList;
import java.io.*;

import miniRSA.MiniRSA;

public class ChatClient implements Runnable {  
	private static Socket socket = null;
	private static BigInteger e, c, d, n, srv_e, srv_c;
	private static Thread  writingTd = null;

	ChatClient(Socket s) {
		socket = s;
	}

	public void chat() {
		try	{
	        writingTd = new Thread(this); 
	        writingTd.start();
	        
	        System.out.println("Reading from client...");
	        DataInputStream  streamIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));

	        String fromServer = "";
	        String[] clientPubKey = null;
	        //while (!in.ready()) {}
			fromServer = streamIn.readLine();			
			clientPubKey = fromServer.split("#");
			srv_e = new BigInteger(clientPubKey[0]);
			srv_c = new BigInteger(clientPubKey[1]);
			System.out.println("Received client public key(e, c): \n" + srv_e + " " + srv_c);
			
			
	        fromServer = "";
			while ((fromServer = streamIn.readUTF()) != null) {
				System.out.println("Received: \n" + fromServer);
				if (fromServer.equals(".bye"))
					break;
				MiniRSA.decryptPrint(fromServer, d, n); 
				System.out.println("DECRYPTED to " + fromServer);
			}
			streamIn.close();
			socket.close();
		}
		catch (IOException e) {
			System.err.println("Unable to read from client!"); 
		}
	}
	
	@Override
	public void run() {
		try {
			System.out.println("Writing to server...");
			DataOutputStream streamOut = new DataOutputStream(socket.getOutputStream());
			streamOut.writeBytes(e.toString() + "#" + c.toString() + '\n');
			
			while(srv_e == null) {
				//waiting client send his public key to me
			}
			
			boolean done = false;
			String toServer = "";
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while (!done) {	
				System.out.println("type, enter .bye to quit");
				toServer = in.readLine();
				System.out.print("ENCRYPTED " + toServer);
				if (!toServer.equalsIgnoreCase(".bye")) {
					ArrayList<BigInteger> encryptedNumList = MiniRSA.encrypt(toServer, srv_c, srv_e);
					toServer = "";
					for (int i = 0; i < encryptedNumList.size(); i++) {
						toServer += encryptedNumList.get(i).toString();
						toServer += " ";
					}
					System.out.println(" to " + toServer);
				}
				else done = true;
				streamOut.writeBytes(toServer + '\n');
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

		Socket skt = new Socket(InetAddress.getByName("localhost"), port);
		System.out.println("Accepted by server: " + skt);
		
		ChatServer cc = new ChatServer(skt);
		cc.chat();
		
		skt.close();
	}

	
		
}




















