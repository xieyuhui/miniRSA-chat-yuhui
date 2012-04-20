package chatServer;

import java.io.*;
import java.net.*;

class Client {
	public static void main(String args[]) {
		try {
			Socket skt = new Socket(InetAddress.getByName("localhost"), 54321);
			BufferedReader in = new BufferedReader(new InputStreamReader(skt.getInputStream()));
			
			DataOutputStream streamOut = new DataOutputStream(skt.getOutputStream());
			streamOut.writeBytes("test\n");
			
			System.out.print("Received string: '");

			while (!in.ready()) {}
			System.out.println(in.readLine()); // Read one line and output it

			System.out.print("'\n");
			in.close();
			streamOut.close();
		}
		catch(Exception e) {
			System.out.print("Whoops! It didn't work!\n");
		}
	}
}