package chatServer;
import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.ArrayList;

import miniRSA.MiniRSA;

public class ChatClient {
	public static void main(String[] args) throws UnknownHostException {
		try {
			Socket client = new Socket(InetAddress.getByName("localhost"), 9090);
			DataOutputStream socketOut = new DataOutputStream(client.getOutputStream());
			DataInputStream socketIn = new DataInputStream(client.getInputStream());
			//DataInputStream console = new DataInputStream(System.in);
			System.out.println("Connected to " + InetAddress.getByName("localhost") + ". Enter text:");
			
			boolean done = false;
			String line = "";
			
			BigInteger e, c;
			String[] input = new String[2];
			System.out.println("Please enter the public key (e, c), first e, then c");
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			input = in.readLine().split(" ");
			e = new BigInteger(input[0]);
			c = new BigInteger(input[1]);
			System.out.println("Please enter a sentence to encrypt");
			
			while (!done) {	
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
