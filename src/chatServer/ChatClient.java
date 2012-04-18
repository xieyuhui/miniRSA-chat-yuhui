package chatServer;
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.ArrayList;

import miniRSA.MiniRSA;

public class ChatClient extends java.lang.Thread {
	static int yourPort = -1;
	static int otherPort = -1;
	static BigInteger e, c;
	
	ChatClient(int p1, int p2, BigInteger _e, BigInteger _c) {
		yourPort = p1;
		otherPort = p2;
		e = _e;
		c = _c;		
	}
	ChatClient(int p, BigInteger _e, BigInteger _c) {
		otherPort = p;
		e = _e;
		c = _c;		
	}
	
	@Override
	public void run() {	
		try {
			connect();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public static void connect() throws UnknownHostException {
		try {
			Socket client = new Socket(InetAddress.getByName("localhost"), otherPort);
			DataOutputStream socketOut = new DataOutputStream(client.getOutputStream());
			DataInputStream socketIn = new DataInputStream(client.getInputStream());
			//DataInputStream console = new DataInputStream(System.in);
			System.out.println("Connected to " + InetAddress.getByName("localhost") + ". Enter text:");
			
			//write your port to other server
			if (yourPort != -1) {
				socketOut.writeBytes(Integer.toString(yourPort));
			}
		
			boolean done = false;
			String line = "";
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			while (!done) {	
				System.out.println("type, enter .bye to quit");
				line = in.readLine();
				System.out.print("ENCRYPTED " + line);
				if (!line.equalsIgnoreCase(".bye")) {
					ArrayList<BigInteger> encryptedNumList = MiniRSA.encrypt(line, c, e);
					line = "";
					for (int i = 0; i < encryptedNumList.size(); i++) {
						line += encryptedNumList.get(i).toString();
						line += " ";
					}
					System.out.println(" to " + line);
				}
				else done = true;
				socketOut.writeBytes(line + '\n');
			}

			socketOut.close(); 
			socketIn.close(); 
			client.close();
		} 
		catch (UnknownHostException e) {
			System.err.println(InetAddress.getByName("localhost") + " unknown host."); } 
		catch (IOException e) {
			System.err.println("I/O error with " + InetAddress.getByName("localhost")); }
	}
}
