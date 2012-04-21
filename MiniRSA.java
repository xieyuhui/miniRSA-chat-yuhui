package miniRSA;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class MiniRSA {
	private static final Random rnd = new Random();
	
	/**
	 * Returns the greatest common factor of a and b.
	 * @param a
	 * @param b
	 * @return
	 */
	long GCD(long a, long b) {
		long r = a % b;
		if (r == 0) {
			return b;
		}
		else {
			return GCD(b, r);
		}
	}
	
	/**
	 * Return a random coprime of the given number.
	 * @param x
	 * @return
	 */
	private static BigInteger coprime(BigInteger x) {
		BigInteger a = new BigInteger(x.bitLength(), rnd);
		while (!a.gcd(x).equals(BigInteger.ONE)) {
			a = new BigInteger(x.bitLength(), rnd);
		}
		return a;
	}

	/**
	 * 
	 * @return
	 */
	long[] extendedEuclid(long a, long b) {
	    //setup the first two rows of the table
	    long y1 = 1, z1 = 0, x1 = a, q1 = 1;                   //q is the multiplying factor
	    long y2 = 0, z2 = 1, x2 = b, q2 = x1 / x2;
	    //compute rows till found the GCD
	    while (true) {                
	        long y3 = y1 - q2 * y2;                            //bucause x3 = x1 % x2, x3 = x1 - q2 * x2, get y3 = y1 - q2 * y2
	        long z3 = z1 - q2 * z2;
	        long x3 = x1 - q2 * x2;                            //get this from table   
	        long q3 = x2 / x3;
	    
	        x1 = x2; x2 = x3;                                  //shift all variables down
	        y1 = y2; y2 = y3;
	        z1 = z2; z2 = z3;
	        q1 = q2; q2 = q3;
	        if (x1 % x2 == 0) {                                 //found the GCD
	        	long[] result = {x3, y3, z3};
	            return result;
	        }
	    }
	}

	/**
	 * Returns the multiplicative inverse of a in modulo m as a posi-tive value between zero and m-1.
	 * @param base
	 * @param m
	 * @return
	 */
	static BigInteger modInverse(BigInteger base, BigInteger m) {
		return base.modInverse(m);
	}
	
	/**
	 * x is a positive integer. Convert it to base two as a list of
	 *integers in reverse order. For example, int2baseTwo(6) = [0, 1, 1]
     */
	ArrayList<Long> int2baseTwo(long x) {
<<<<<<< HEAD:src/miniRSA/MiniRSA.java
		ArrayList<Long> twoBitList = new ArrayList<Long>();
		if (x == 0) {
			twoBitList.add((long) 0);
			return twoBitList;
		}
		while (x != 0) {
			twoBitList.add(x % 2);
			x >>= 1;                        //right shift one bit
		}
		return twoBitList;
=======
	    ArrayList<Long> twoBitList = new ArrayList<Long>();
	    if (x == 0) {
	    	twoBitList.add((long) 0);
	    	return twoBitList;
	    }
	    while (x != 0) {
	        twoBitList.add(x % 2);
	        x >>= 1;                        //right shift one bit
	    }
	    return twoBitList;
>>>>>>> github/master:MiniRSA.java
	}
	
	private static boolean millerRabinPass(BigInteger a, BigInteger n) {
    	BigInteger n_minus_one = n.subtract(BigInteger.ONE);
    	BigInteger d = n_minus_one;
    	int s = d.getLowestSetBit();
    	d = d.shiftRight(s);

        BigInteger a_to_power = a.modPow(d, n);
        if (a_to_power.equals(BigInteger.ONE)) return true;
        for (int i = 0; i < s-1; i++) {
            if (a_to_power.equals(n_minus_one)) return true;
            a_to_power = a_to_power.multiply(a_to_power).mod(n);
        }
        if (a_to_power.equals(n_minus_one)) return true;
        return false;
    }

	/**
	 * 
	 * @param n
	 * @return
	 */
	public static boolean millerRabin(BigInteger n) {
		for (int repeat = 0; repeat < 5; repeat++) {
			BigInteger a;
			do {
				a = new BigInteger(n.bitLength(), rnd);
			} while (a.equals(BigInteger.ZERO));
			if (!millerRabinPass(a, n)) {
				return false;
			}
		}
		return true;
	}
	
	
	/**
	 * Returns a^d mod n. Use the fast algorithm as explained in class. 
	 * Do not just create a loop and keep multiplying. This will cause the program
	 * to take too long to run. (I mean waaaaaaaay too long!)
	 * @param a
	 * @param b
	 * @param c
	 * @return
	 */
	static BigInteger modulo(BigInteger a, BigInteger b, BigInteger c) {
		return a.modPow(b, c);
	}

	/**
	 * m is a positive integer. A tuple (s,d) of integers is returned
	 * such that m = 2**s*d and d is odd. Bit operations make this job a breeze.
	 */
	long[] extractTwos(long m) {
	    long s = 0;
	    long d = m;
	    while (d % 2 == 0) {
	        d >>= 1;                                            //d = d>>1
	        s++;         
	    }
	    long[] result = {s, d};
	    return result;
	}
	
	/**
	 * Return a prime number between a and b.
	 * @param a
	 * @param b
	 * @return
	 */
	static BigInteger findAPrime(int min, int max) {
		int nbits = log2(max);
		int tries = 0;
		BigInteger p;
		do {
			p = new BigInteger(nbits, rnd);
			if (p.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) continue;
			if (p.mod(BigInteger.valueOf(3)).equals(BigInteger.ZERO)) continue;
			if (p.mod(BigInteger.valueOf(5)).equals(BigInteger.ZERO)) continue;
			if (p.mod(BigInteger.valueOf(7)).equals(BigInteger.ZERO)) continue;
			tries++;
			if (tries > (10 * log2(max) + 3)) return BigInteger.ZERO;         //fail to find the prime

		} while (!millerRabin(p) || 
				p.compareTo(BigInteger.valueOf(max)) > 0 ||
				p.compareTo(BigInteger.valueOf(min)) < 0);
		return p;
	}
	
	/**
	 * 
	 * @param n
	 * @return
	 */
	public static int log2(int n){
	    if(n <= 0) throw new IllegalArgumentException();
	    return 31 - Integer.numberOfLeadingZeros(n);
	}
	
	/**
	 * converts a string to a list of integers, one integer for each character in the string.
	 * The values are the corresponding ASCII values for the characters.
	 * @return
	 */
	private static ArrayList<Integer> stringToNumList(String s) {
		ArrayList<Integer> numList = new ArrayList<Integer>();
		for (int i = 0; i < s.length(); i++) {
			numList.add((int) (s.charAt(i)));
		}
		return numList;
	}
	
	public static ArrayList<BigInteger> encrypt(String message, BigInteger n, BigInteger e) {
		ArrayList<Integer> beforEncryptList = stringToNumList(message);
		ArrayList<BigInteger> afterEncryptList = new ArrayList<BigInteger>();
		for (int i = 0; i < beforEncryptList.size(); i++) {
			long x = beforEncryptList.get(i);
			afterEncryptList.add((modulo(BigInteger.valueOf(x), e, n)));
		}
		return afterEncryptList;
	}
<<<<<<< HEAD:src/miniRSA/MiniRSA.java

//	private static String decrypt (ArrayList<BigInteger> msgNumList, BigInteger n, BigInteger d) {
//		String msg = "";
//		for (int i = 0; i < msgNumList.size(); i++) {
//			int decryptedChar = modulo(msgNumList.get(i), d, n).intValue();
//			String aChar = new Character((char)decryptedChar).toString();
//			msg = msg + aChar;
//		}
//		return msg;
//	}

=======
	
	private static String decrypt (ArrayList<BigInteger> msgNumList, BigInteger n, BigInteger d) {
		String msg = "";
		for (int i = 0; i < msgNumList.size(); i++) {
			int decryptedChar = modulo(msgNumList.get(i), d, n).intValue();
	    	String aChar = new Character((char)decryptedChar).toString();
	        msg = msg + aChar;
	    }
		return msg;
	}
	
>>>>>>> github/master:MiniRSA.java
	/**
	 * Given n (n = p * q) and e, try to figure out the p and q.
	 * @param n: modulus
	 * @param e: public exponent
	 * @param c: cipher
	 * @return The cracked resulting string.
	 */
	private static BigInteger[] totient (BigInteger n, BigInteger e) {
		BigInteger[] crackArray = new BigInteger[3];
		BigInteger p = BigInteger.ONE;
		BigInteger q = BigInteger.ZERO;
		while (true) {
			p = p.add(BigInteger.ONE);
			if (n.mod(p).compareTo(BigInteger.ZERO) == 0) {
				q = n.divide(p);
				if (p.multiply(q).compareTo(n) == 0)
					break;
			}
		}
		BigInteger totient = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
		//  System.out.println("p="+p+"\n"+"q="+q+"\n"+"totient="+totient);
		
		crackArray[0] = p;
		crackArray[1] = q;
		crackArray[2] = totient;
		return crackArray;
	}
	
	
	/**
	 * Asks user to input p and q to generate the public key.
	 * @throws IOException
	 */
	private static void generateKey() throws IOException {
		int min, max;
		BigInteger p, q, c, m, e, d;
		String[] input = new String[2];
		System.out.println("Enter the range of the prime you want to generate. e.g. 100 1000." +
				"Make sure the higher bound is larger than lower bound.");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		input = in.readLine().split(" ");
		min = Integer.parseInt(input[0]);
		max = Integer.parseInt(input[1]);
		while (min >= max) {
			System.out.println("First number must smaller than the second one. Please try again.");
			input = in.readLine().split(" ");
			min = Integer.parseInt(input[0]);
			max = Integer.parseInt(input[1]);
		}
		p = findAPrime(min, max);
		if (p.equals(BigInteger.ZERO)) {
			System.out.println("Oops, did find the prime. Please try larger range.");
		}
		System.out.println("Enter the range of the prime you want to generate. e.g. 100 1000");
		in = new BufferedReader(new InputStreamReader(System.in));
		input = in.readLine().split(" ");
		min = Integer.parseInt(input[0]);
		max = Integer.parseInt(input[1]);
		q = findAPrime(min, max);

		c = p.multiply(q);
		m = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
		e = coprime(m);
		d = modInverse(e, m);

		System.out.print("First prime = " + p);
		System.out.print(", Second prime = " + q);
		System.out.print(", c = " + c);
		System.out.print(", m = " + m);
		System.out.print(", e = " + e);
		System.out.print(", d = " + d);
		System.out.print(", Public Key = (" + e + ", " + c + ")");
		System.out.println(", Private Key = (" + d + ", " + c + ")");
	}
	
	/**
	 * Ask user to input the public key (e, c) and message, print out the encrypt result.
	 * @throws IOException
	 */
	public static void encryptPrint() throws IOException {
		BigInteger e, c;
		String[] input = new String[2];
		System.out.println("Please enter the public key (e, c), first e, then c");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		input = in.readLine().split(" ");
		e = new BigInteger(input[0]);
		c = new BigInteger(input[1]);
		System.out.println("Please enter a sentence to encrypt");
		Scanner inscanner = new Scanner(System.in);
		String msg = inscanner.nextLine();
		ArrayList<BigInteger> encryptedNumList = encrypt(msg, c, e);
		for (int i = 0; i < encryptedNumList.size(); i++) {
			System.out.println(encryptedNumList.get(i));
		}
	}
	
	/**
	 * Ask user to input the private key (d, c) and message, print out the decrypt result.
	 * @throws IOException
	 */
	public static void decryptPrint() throws IOException {
		BigInteger d, c, m, n;
		String[] input = new String[2];
		System.out.println("Please enter the private key (d, c), first d, then c");
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		input = in.readLine().split(" ");
		d = new BigInteger(input[0]);
		n = new BigInteger(input[1]);
		while (true) {
			System.out.println("Enter next char cipher value as an int, or quit to exit");
			Scanner inscanner = new Scanner(System.in);
			String s = inscanner.next();
			if (s.equals("quit")) {
				System.out.println("Done");
				break;
			}
			c = new BigInteger(s);
			m = c.modPow(d, n);
			System.out.println((char)m.intValue() + " " + m);
		}
	}
	
	
	public static void decryptPrint(String str, BigInteger d, BigInteger n) throws IOException {
		BigInteger m, c;	
		String[] cipher = str.split(" ");
		for (int i = 0; i < cipher.length; i++) {
			c = new BigInteger(cipher[i]);
			m = c.modPow(d, n);
			System.out.println((char)m.intValue() + " " + m);
		}
	}
	
	public static BigInteger crack(BigInteger e, BigInteger n) {
		BigInteger p, q, d;
		BigInteger[] totient = totient(n, e);
		p = totient[0];
		q = totient[1];
		d = e.modInverse(totient[2]);
		
		System.out.println("Cracking result:");
		System.out.println("a was " + p + " b was " + q);
		System.out.println("The totient was " + totient[2]);
		System.out.println("D was found out to be " + d);
		System.out.println();
		return d;
	}

	/**
	 * Asks user to input the public key and cipher text.
	 * @throws IOException
	 */
	private static void printCrack(BigInteger e, BigInteger n) throws IOException {
		BigInteger p, q, d;
		BigInteger[] totient = totient(n, e);
		p = totient[0];
		q = totient[1];
		d = e.modInverse(totient[2]);
		
		System.out.println("Enter the c that goes with the public key");
		System.out.println("a was " + p + " b was " + q);
		System.out.println("The totient was " + totient[2]);
		System.out.println("D was found out to be " + d);
	}
	
	public static void main(String[] args) throws IOException {
		if (args.length != 2) {
			System.out.println("Server Usage: public_e public_c");
			return;
		}
		BigInteger e = new BigInteger(args[0]);
		BigInteger c = new BigInteger(args[1]);
		printCrack(e, c);
	}
}


