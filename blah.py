import socket
import rsa
from threading import Thread

def read(s):
    while True:
        print s.recv(1024)

def write(s):
    while True:
        typed = raw_input("> ")
        s.send(typed)

def main():
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    s.connect(("localhost", 12342))
    x = Thread(target=read, args=(s,))
    t = Thread(target=write, args=(s,))
    x.start()
    t.start()
    x.join()
    t.join()        

main()
