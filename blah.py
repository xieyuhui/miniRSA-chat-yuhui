import os
import socket
from rsa import RSA
from threading import Thread

modN = 0
keyE = 0

def read(s, rsa):
    global modN
    global keyE
    while True:
        encrypted =  s.recv(1024)
        if len(encrypted) == 0: 
            print "Chat ended by other user"
            s.close()
            os._exit(os.EX_OK)
        encrypted = encrypted[1:]
        encrypted = encrypted[:-1]
        encrypted = encrypted.split(",")
        encrypted = [int(i) for i in encrypted]
        print rsa.decrypt(encrypted, rsa.publicKeyN, rsa.privateKeyD)

def write(s, rsa):
    while True:
        typed = raw_input("")
        if typed == ".bye":
            break
        encrypted = rsa.encrypt(typed, keyE, modN)
        s.send(str(encrypted))

def main():
    global modN
    global keyE
    nthPrime = input("Enter nth prime: ")
    mthPrime = input("Enter mth prime: ")
    while nthPrime == mthPrime:
        print "You cannot use the same number"
        mthPrime = input("Enter mth prime: ")
    rsa = RSA(nthPrime, mthPrime)
    rsa.generateKeys()
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect(("localhost", 12346))
    print "Type .bye to exit"
    #receive keys
    keys = s.recv(2048)
    keys = keys.split("#")
    modN = int(keys[0])
    keyE = int(keys[1])
    #send keys
    publicKeys = str(rsa.publicKeyN) + "#" + str(rsa.publicKeyE)
    s.send(publicKeys)
    x = Thread(target=read, args=(s, rsa))
    t = Thread(target=write, args=(s, rsa))
    x.start()
    t.start()
    t.join()        
    s.close()
    os._exit(os.EX_OK)

main()
