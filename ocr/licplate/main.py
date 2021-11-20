import sys
from cv2 import cv2
import easyocr
import socket

img = cv2.imread(sys.argv[1])
reader = easyocr.Reader(['en'])
result = reader.readtext(img)

text = ""
for x in result:
    text += x[-2]
print(text)

clientSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
clientSocket.connect(("127.0.0.1", 9090))
clientSocket.send(bytes(text))
