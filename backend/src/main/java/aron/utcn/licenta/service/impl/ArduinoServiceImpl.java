package aron.utcn.licenta.service.impl;

import java.io.OutputStream;
import java.util.Enumeration;

import org.springframework.stereotype.Component;

import aron.utcn.licenta.exception.LCDNotFoundException;
import aron.utcn.licenta.service.ArduinoService;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

@Component
public class ArduinoServiceImpl implements ArduinoService {

	private static Enumeration portList;
	private static CommPortIdentifier portId;
	private static SerialPort serialPort;
	private static OutputStream outputStream;
	
	static {
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
	                } catch(Exception e) {
	                	System.out.println("Problem with sending data on serial port");
	                }
	            }
	        }
	    }
	}
	
	@Override
	public void displayOnLCD(String message) {
		try {
			outputStream.write(message.getBytes());
		} catch (Exception e) {
			System.out.println("Hardware not found!");
		}
	}

	@Override
	public void activateBarrier() throws LCDNotFoundException {
			displayOnLCD("welcome");
	}

}
