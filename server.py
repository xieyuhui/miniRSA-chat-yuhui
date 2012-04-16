import os
import sys
from threading import Thread
import socket
from rsa import RSA

modN = 0
keyE = 0

def read(s, rsa):
    while True:
        encrypted = s.recv(1024)
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
        msg = rsa.encrypt(typed, keyE, modN)
        s.send(str(msg))

def main():
    global modN
    global keyE
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind(("", 12346))
    s.listen(1)
    nthPrime = input("Enter nth prime: ")
    mthPrime = input("Enter mth prime: ")
    while nthPrime == mthPrime:
        print "You cannot use the same number"
        mthPrime = input("Enter mth prime: ")
    rsa = RSA(nthPrime, mthPrime)
    rsa.generateKeys()
    print "waiting to accept"
    channel, details = s.accept()
    print "Type .bye to exit"
    #send keys to client
    publicKeys = str(rsa.publicKeyN) + "#" + str(rsa.publicKeyE)
    channel.send(publicKeys) 
    #read keys
    keys = channel.recv(2048)
    keys = keys.split("#")
    modN = int(keys[0])
    keyE = int(keys[1])
    t = Thread(target=read, args=(channel, rsa))
    x = Thread(target=write, args=(channel, rsa))
    x.start()
    t.start()
    x.join()
    channel.close()
    os._exit(os.EX_OK)

main()
