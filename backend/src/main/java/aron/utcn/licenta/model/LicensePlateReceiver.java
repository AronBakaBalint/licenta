package aron.utcn.licenta.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.springframework.stereotype.Component;

import aron.utcn.licenta.service.ParkingSpotManagementService;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LicensePlateReceiver implements Runnable {

	private static ServerSocket server;
	
	private static final int QR_CODE_PORT = 9876;
	
	private static final int NP_READER_PORT = 9877;
	
	private final ParkingSpotManagementService parkingPlaceService;
	
	@Override
	public void run() {
		try {
			waitForLicensePlate();
			waitForQRCode();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void waitForLicensePlate() throws IOException, ClassNotFoundException {
		server = new ServerSocket(NP_READER_PORT);
		while(true) {
			Socket socket = server.accept();
			BufferedReader stdIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String message = stdIn.readLine();
			System.out.println("Received: " + message);
			parkingPlaceService.handleScannedCode(message.toLowerCase());
			stdIn.close();
			socket.close();
		}
	}
	
	private void waitForQRCode() throws IOException, ClassNotFoundException {
		server = new ServerSocket(QR_CODE_PORT);
		while(true) {
			Socket socket = server.accept();
			ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
			String message = (String)ois.readObject();
			parkingPlaceService.handleScannedCode(message.toLowerCase());
			ois.close();
			socket.close();
		}
	}

}
