from threading import Thread
import socket
from rsa import RSA

def __init(self, x, y, host="", port=29292):
    self.primeOne = x
    self.primeTwo = y
    self.host = host
    self.port = port

def genRSA(self):
    self.rsa = RSA(x, y)

def read(s, rsa):
    while True:
        print s.recv(1024)

def write(s, rsa):
    while True:
        typed = raw_input("> ")
        msg = rsa.encrypt(typed)
        s.send(str(msg))

def main():
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.bind(("", 12342))
    s.listen(1)
    nthPrime = input("Enter nth prime: ")
    mthPrime = input("Enter mth prime: ")
    rsa = RSA(nthPrime, mthPrime)
    rsa.generateKeys()
    print "waiting to accept"
    while True:
        channel, details = s.accept()
        print "accepted"
        t = Thread(target=read, args=(channel, rsa))
        x = Thread(target=write, args=(channel, rsa))
        x.start()
        t.start()
        x.join()
        t.join()
    #channel.close()
    print "hey"


main()
