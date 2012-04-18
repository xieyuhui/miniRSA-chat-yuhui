import rsa
import unittest

class Test_RSA(unittest.TestCase):
    def test_rsa_GCD(self):
        self.assertEqual(rsa.GCD(100, 10), 10)
        self.assertEqual(rsa.GCD(10, 100), 10)
        self.assertEqual(rsa.GCD(100, 13), 1)
        self.assertEqual(rsa.GCD(100, 100), 100)
        self.assertEqual(rsa.GCD(13, 39), 13)
        self.assertEqual(rsa.GCD(39, 13), 13)
        self.assertEqual(rsa.GCD(1001, 77), 77)
        self.assertEqual(rsa.GCD(1001, 33), 11)
        self.assertNotEqual(rsa.GCD(1002, 33), 11)

    def test_rsa_coprime(self):
        r = rsa.coprime(13)
        self.assertEqual(rsa.GCD(13, r), 1)
        r = rsa.coprime(29)
        self.assertEqual(rsa.GCD(29, r), 1)
        r = rsa.coprime(7)
        self.assertEqual(rsa.GCD(7, r), 1)
        r = rsa.coprime(2)
        self.assertEqual(rsa.GCD(2, r), 1)
   
    def test_is_prime(self):
        self.assertTrue(rsa.is_prime(13))
        self.assertTrue(rsa.is_prime(37))
        self.assertTrue(rsa.is_prime(2))
        self.assertTrue(rsa.is_prime(3))
        self.assertTrue(rsa.is_prime(29))
        self.assertTrue(rsa.is_prime(7))
        self.assertTrue(rsa.is_prime(11))
        self.assertFalse(rsa.is_prime(22))
        self.assertFalse(rsa.is_prime(4))
        self.assertFalse(rsa.is_prime(9))
        self.assertFalse(rsa.is_prime(50))
        self.assertTrue(rsa.is_prime(661))
        self.assertFalse(rsa.is_prime(662))

    def test_mod_inv(self):
        self.assertEqual(rsa.mod_inverse(3, 17), 6)
        self.assertEqual(rsa.mod_inverse(55, 123), 85)
        self.assertEqual(rsa.mod_inverse(45, 223), 114)
        self.assertEqual(rsa.mod_inverse(3, 30), 0)

    def test_encrypt(self):
        self.assertEqual(rsa.encrypt(35, 7, 943), 545)

    def test_decrypt(self):
        self.assertEqual(rsa.decrypt(545, 503, 943), 35)

unittest.main()
