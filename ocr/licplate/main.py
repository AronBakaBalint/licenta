from cv2 import cv2
import easyocr
import socket
import os

BASE_DIR = os.path.dirname(os.path.realpath(__file__))
SOCKET_PORT = 9877

filePath = os.path.join(BASE_DIR, 'img\\licplate.jpg')

print('License Plate Detector Started\n')

img = cv2.imread(filePath)
reader = easyocr.Reader(['en'])
result = reader.readtext(img)

text = ""
for x in result:
    text += x[-2]

clientSocket = None
try:
    clientSocket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    clientSocket.connect((socket.gethostname(), SOCKET_PORT))
    clientSocket.send(bytes(text, 'UTF-8'))
except ConnectionRefusedError:
    print('Receiver socket is not open\n')
finally:
    clientSocket.close()

os.remove(filePath)
