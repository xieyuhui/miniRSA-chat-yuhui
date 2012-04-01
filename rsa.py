#!/usr/bin/python
import random

def main():
    print GCD(24, 64)

def GCD(a, b):
    if a > b: a, b = b, a
    while True:
        if b % a == 0: return a
        a, b = b%a, a

def is_prime(x):
    if x == 1:
        return False
    for i in xrange(2, x):
        if x % i == 0: return False
    return True

def coprime(x):
    r = random.randint(x, x + 14)
    while not GCD(x, r) == 1: r = random.randint(x, x + 14)
    return r

def modulo(a, b, c):
    return (a ** b) 

def extendedEuclid(a,b):
    #a is the smaller number
    if b > a:
        if b%a == 0:
            return(a, 1, 0)
        else: 
            z, x, y = extendedEuclid(b%a, a)
            return(z, y - (b//a)*x,x)
    #a is bigger number
    if a > b:
        if a%b == 0:
            return(b, 1, 0)
        else: 
            z, x, y = extendedEuclid(a%b, b)
            return(z, x, y - (a//b)*x)

def mod_inverse(a, m):
    if a > m: a, m = m, a #make 'a' the smaller number
    gcf, x, y = extendedEuclid(a, m)
        #same as x mod n (p 89 in book)
    if gcf == 1: return x % m
    else: return 0

def encrypt(val, e, c):
    return (val ** e) % c

def decrypt(val, d, c):
    return (val ** d) % c

main()
