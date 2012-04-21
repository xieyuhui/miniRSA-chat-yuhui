#!/usr/bn/python
import random

class RSA():
    def __init__(self, x, y):
        self.nthprimeone = x
        self.nthprimetwo = y
    
    def generateKeys(self):
        self.primeOne = self.getNthPrime(self.nthprimeone)
        self.primeTwo = self.getNthPrime(self.nthprimetwo)
        self.publicKeyN, self.publicKeyE, self.privateKeyD = self.newKey(self.primeOne, self.primeTwo)

    def GCD(self, a, b):
        if a > b: a, b = b, a
        while True:
            if b % a == 0: return a
            a, b = b%a, a
    
    def isprime(self, n):
        if n == 1:
            return False
        for x in range(2, n):
            if n % x == 0:
                return False
        else:
            return True

    def getNthPrime(self, n):
        x = 1
        number_of_primes = 0
        while True:
            x += 1
            if self.isprime(x):
                number_of_primes += 1
                if number_of_primes == n:
                    return x
             
    def coprime(self, x):
        r = random.randint(1, x)
        while not self.GCD(x, r) == 1: 
            r = random.randint(1, x)
        return r

    def modulo(self, a, b, c):
        return (a ** b) 

    def modExp(self, a, d, n):
        if d == 0:
            return 1
        mask = self.int2baseTwo(d)
        template = [1]*len(mask)
        template[0] = a
        for i in range(1, len(mask)):
            template[i] = template[i - 1]**2 % n
        x = 1
        for i in range(len(mask)):
            if mask[i] == 1:
                x = x*template[i] % n
        return x

    def int2baseTwo(self, x):
        a = []
        while x > 0:
            a.append(x % 2)
            x = x//2
        return a

    def extendedEuclid(self, a,b):
        #a is the smaller number
        if b > a:
            if b%a == 0:
                return(a, 1, 0)
            else: 
                z, x, y = self.extendedEuclid(b%a, a)
                return(z, y - (b//a)*x,x)
        #a is bigger number
        if a > b:
            if a%b == 0:
                return(b, 1, 0)
            else: 
                z, x, y = extendedEuclid(a%b, b)
                return(z, x, y - (a//b)*x)

    def modInv(self, a, m):
        if a > m: a, m = m, a #make 'a' the smaller number
        gcf, x, y = self.extendedEuclid(a, m)
            #same as x mod n (p 89 in book)
        if gcf == 1: return x % m
        else: return 0

    def string2numList(self, strn):
        nums = []
        for i in range(len(strn)):
            nums.append(ord(strn[i]))
        return nums

    def numList2string(self, L):
        stL = []
        for i in range(len(L)):
            stL.append(chr(L[i]))
        return ''.join(stL)

    def numList2blocks(self, L, n):
        sparesNeeded = -len(L) % n
        for i in range(sparesNeeded):
            L.append(random.randint(1, 127))
        blocks = []
        hold = 0
        for i in range(len(L)):
            hold = hold + L[i]
            if i % n == -1 % n:
                blocks.append(hold)
                hold = 0
            hold = hold << 8
        return blocks

    def newKey(self, p, q):
        n = p*q
        totient = (p - 1)*(q - 1)
        e = self.coprime(totient)
        d = self.modInv(e, totient)
        return n, e, d 

    def blocks2numList(self, blocks, n):
        L = [0]*(n*len(blocks))
        for b in range(len(blocks)):
            blk = blocks[b]
            for i in range(1, n + 1):
                L[b*n + n - i] = blk % 128
                blk = blk - L[b*n + n - i]
                blk = blk >> 8
        return L

    def encrypt(self, message, e, modN):
        numL = self.string2numList(message)
        blockSize = 1
        blocks = self.numList2blocks(numL, blockSize)
        #e = self.publicKeyE
        #modN = self.publicKeyN
        secret = []
        for i in range(len(blocks)):
            secret.append(self.modExp(blocks[i], e, modN))
        return secret 

    def decrypt(self, secret, modN, d):
        #d = self.privateKeyD
        #modN = self.publicKeyN
        blockSize = 1
        blocks = []
        for i in range(len(secret)):
            blocks.append(self.modExp(secret[i], d, modN))
        numL = self.blocks2numList(blocks, blockSize)
        message = self.numList2string(numL)
        return message
