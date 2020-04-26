package aron.utcn.licenta.service.impl;

import java.io.OutputStream;
import java.util.Enumeration;

import org.springframework.stereotype.Component;

import aron.utcn.licenta.service.ArduinoService;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

@Component
public class ArduinoServiceImpl implements ArduinoService {

	private Enumeration portList;
	private CommPortIdentifier portId;
	private SerialPort serialPort;
	private OutputStream outputStream;
	
	@Override
	public void displayOnLCD(String message) {
		portList = CommPortIdentifier.getPortIdentifiers();
		
	    while (portList.hasMoreElements()) {
	        portId = (CommPortIdentifier) portList.nextElement();
	        if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
	             if (portId.getName().equals("COM3")) {
	                try {
	                    serialPort = (SerialPort)portId.open("SimpleWriteApp", 2000);
	                    Thread.sleep(1000);
	                    outputStream = serialPort.getOutputStream();
	                    serialPort.setSerialPortParams(9600,
	                        SerialPort.DATABITS_8,
	                        SerialPort.STOPBITS_1,
	                        SerialPort.PARITY_NONE);

	                    outputStream.write(message.getBytes());

	                    outputStream.close();
	                    serialPort.close();
	                } catch(Exception e) {
	                	System.out.println("Problem with sending data on serial port");
	                }
	            }
	        }
	    }
		
	}

	@Override
	public void activateBarrier() {
		// TODO Auto-generated method stub
		
	}

}
