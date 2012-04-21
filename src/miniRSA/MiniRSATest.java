package miniRSA;

import java.math.BigInteger;

import miniRSA.MiniRSA;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class MiniRSATest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testGCD() {
		MiniRSA r = new MiniRSA();
		long a = 16;
		long b = 5;
		assertEquals(r.GCD(a, b), 1);
	}
	
//	@Test
//	public void testModulo() {
//		MiniRSA r = new MiniRSA();
//		System.out.println(r.modulo(981, 937, 2537));
//	}
//	
	@Test
	public void testMod_inverse() {
		MiniRSA r = new MiniRSA();
		System.out.println(r.modInverse(BigInteger.valueOf(17), BigInteger.valueOf(57)));
	}
	
//	@Test
//	public void testMillerRabin() {
//		MiniRSA r = new MiniRSA();
//
//	}
	

}
