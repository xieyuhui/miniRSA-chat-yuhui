import os
import sys
from threading import Thread
import socket
from rsa import RSA

modN = 0
keyE = 0
showEncrypted = False

def read(s, rsa):
    global showEncrypted
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
        if showEncrypted:
            print encrypted
        print rsa.decrypt(encrypted, rsa.publicKeyN, rsa.privateKeyD) 

def write(s, rsa):
    global showEncrypted
    global modN
    global keyE
    while True:
        typed = raw_input("")
        if typed == ".bye":
            break
        if typed == "_showkeys_":
            print "Other's key N", modN
            print "Other's key E", keyE
            print "My key N", rsa.publicKeyN
            print "My key E", rsa.publicKeyE
            print "My private key D", rsa.privateKeyD
            continue
        if typed == "_showenc_":
            showEncrypted = True
            continue
        if typed == "_hideenc_":
            showEncrypted = False
            continue
        msg = rsa.encrypt(typed, keyE, modN)
        if showEncrypted:
            print msg
        s.send(str(msg))

def main():
    global modN
    global keyE
    if len(sys.argv) != 2:
        print "Usage: python server.py PORT_NUM"
        os._exit(os.EX_OK)
    port = int(sys.argv[1])
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind(("", port))
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
    #read keys
    keys = channel.recv(2048)
    keys = keys.split("#")
    modN = int(keys[0])
    keyE = int(keys[1])
    #send keys to client
    publicKeys = str(rsa.publicKeyN) + "#" + str(rsa.publicKeyE)
    channel.send(publicKeys) 
    t = Thread(target=read, args=(channel, rsa))
    x = Thread(target=write, args=(channel, rsa))
    x.start()
    t.start()
    x.join()
    channel.close()
    os._exit(os.EX_OK)

main()
